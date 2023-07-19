package com.neu.dy.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.neu.dy.api.AgencyScopeFeign;
import com.neu.dy.api.AreaApi;
import com.neu.dy.authority.entity.common.Area;
import com.neu.dy.base.R;
import com.neu.dy.base.common.CustomIdGenerator;
import com.neu.dy.base.dto.angency.AgencyScopeDto;

import com.neu.dy.order.dto.OrderDTO;
import com.neu.dy.order.dto.OrderSearchDTO;
import com.neu.dy.order.entitiy.Order;
import com.neu.dy.order.entitiy.fact.AddressCheckResult;
import com.neu.dy.order.entitiy.fact.AddressRule;
import com.neu.dy.order.enums.OrderPaymentStatus;
import com.neu.dy.order.enums.OrderPickupType;
import com.neu.dy.order.enums.OrderStatus;
import com.neu.dy.order.future.DyCompletableFuture;
import com.neu.dy.order.mapper.OrderMapper;
import com.neu.dy.order.service.OrderService;
import com.neu.dy.utils.BaiduMapUtils;
import com.neu.dy.utils.EntCoordSyncJob;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;


/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Autowired
    private AreaApi areaApi;

    @Autowired
    private AgencyScopeFeign agencyScopeFeign;

    private void exceptionHappend(String s) {
        try {
            throw new Exception(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成订单
     * @param order 订单信息
     * @return
     */
    @Override
    public Order saveOrder(Order order) {
        order.setId(idGenerator.nextId(order) + "");
        order.setCreateTime(LocalDateTime.now());
        order.setPaymentStatus(OrderPaymentStatus.UNPAID.getStatus());
        if (OrderPickupType.NO_PICKUP.getCode() == order.getPickupType()) {
            order.setStatus(OrderStatus.OUTLETS_SINCE_SENT.getCode());
        } else {
            order.setStatus(OrderStatus.PENDING.getCode());
        }
        save(order);
        return order;
    }

    @Override
    public IPage<Order> findByPage(Integer page, Integer pageSize, Order order) {
        Page<Order> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(order.getId())) {
            lambdaQueryWrapper.like(Order::getId, order.getId());
        }
        if (order.getStatus() != null) {
            lambdaQueryWrapper.eq(Order::getStatus, order.getStatus());
        }
        if (order.getPaymentStatus() != null) {
            lambdaQueryWrapper.eq(Order::getPaymentStatus, order.getPaymentStatus());
        }
        //发件人信息
        if (StringUtils.isNotEmpty(order.getSenderName())) {
            lambdaQueryWrapper.like(Order::getSenderName, order.getSenderName());
        }
        if (StringUtils.isNotEmpty(order.getSenderPhone())) {
            lambdaQueryWrapper.like(Order::getSenderPhone, order.getSenderPhone());
        }
        if (StringUtils.isNotEmpty(order.getSenderProvinceId())) {
            lambdaQueryWrapper.eq(Order::getSenderProvinceId, order.getSenderProvinceId());
        }
        if (StringUtils.isNotEmpty(order.getSenderCityId())) {
            lambdaQueryWrapper.eq(Order::getSenderCityId, order.getSenderCityId());
        }
        if (StringUtils.isNotEmpty(order.getSenderCountyId())) {
            lambdaQueryWrapper.eq(Order::getSenderCountyId, order.getSenderCountyId());
        }
        //收件人信息
        if (StringUtils.isNotEmpty(order.getReceiverName())) {
            lambdaQueryWrapper.like(Order::getReceiverName, order.getReceiverName());
        }
        if (StringUtils.isNotEmpty(order.getReceiverPhone())) {
            lambdaQueryWrapper.like(Order::getReceiverPhone, order.getReceiverPhone());
        }
        if (StringUtils.isNotEmpty(order.getReceiverProvinceId())) {
            lambdaQueryWrapper.eq(Order::getReceiverProvinceId, order.getReceiverProvinceId());
        }
        if (StringUtils.isNotEmpty(order.getReceiverCityId())) {
            lambdaQueryWrapper.eq(Order::getReceiverCityId, order.getReceiverCityId());
        }
        if (StringUtils.isNotEmpty(order.getReceiverCountyId())) {
            lambdaQueryWrapper.eq(Order::getReceiverCountyId, order.getReceiverCountyId());
        }
        lambdaQueryWrapper.orderBy(true, false, Order::getId);
        return page(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<Order> findAll(List<String> ids) {
        return null;
    }

    @Override
    public IPage<Order> pageLikeForCustomer(OrderSearchDTO orderSearchDTO) {
        return null;
    }

    /**
     * 计算订单预计时间
     * @param orderDTO
     * @return
     */
    @Override
    public Integer calculatetime(OrderDTO orderDTO) {
        //地址解析失败，无法获取时间
        if("sender error msg".equals(orderDTO.getSenderAddress()) || "receiver error msg".equals(orderDTO.getReceiverAddress())){
            return 0;
        }
        //调用百度地图接口计算订单时间
        String orgin =BaiduMapUtils.getCoordinate(orderDTO.getSenderAddress());
        String destination =BaiduMapUtils.getCoordinate(orderDTO.getReceiverAddress());
        int time = BaiduMapUtils.getTime(orgin,destination);
        return time;
    }

    /**
     * 计算订单价格
     * @param orderDTO
     * @return
     */
    @Override
    public Map calculateAmount(OrderDTO orderDTO) {
        //调用百度地图接口计算订单距离
        orderDTO = this.getDistance(orderDTO);

        if("sender error msg".equals(orderDTO.getSenderAddress()) || "receiver error msg".equals(orderDTO.getReceiverAddress())){
            //地址解析失败，直接返回
            Map map = new HashMap();
            map.put("amount","0");
            map.put("errorMsg","无法计算订单距离和订单价格，请输入真实地址");
            map.put("orderDto",orderDTO);
            return map;
        }

        //使用Drools规则引擎计算价格
        KieSession kieSession = ReloadDroolsRulesService.kieContainer.newKieSession();
        //封装计算订单价格所需要的参数,fact对象和规则引擎相关
        AddressRule addressRule = new AddressRule();
        addressRule.setTotalWeight(orderDTO.getOrderCargoDto().getTotalWeight().doubleValue()); //从购买物品dto中获取总重量
        addressRule.setDistance(orderDTO.getDistance().doubleValue()); //设置订单距离

        //将对象加入到工作内存中
        kieSession.insert(addressRule);

        AddressCheckResult addressCheckResult = new AddressCheckResult();
        kieSession.insert(addressCheckResult);

        int i = kieSession.fireAllRules();
        System.out.println("触发了" + i + "条规则");
        kieSession.destroy();

        //TODO 订单价格
        if(!addressCheckResult.isPostCodeResult()){
            System.out.println("规则匹配失败，订单价格为：" + addressCheckResult.getResult());
            orderDTO.setAmount(new BigDecimal("25.0"));

            Map map = new HashMap();
            map.put("orderDto",orderDTO);
            map.put("amount",new BigDecimal("25.0"));

            return map;
        }

        if(addressCheckResult.isPostCodeResult()){
            System.out.println("规则匹配成功，订单价格为：" + addressCheckResult.getResult());
            orderDTO.setAmount(new BigDecimal(addressCheckResult.getResult()));

            Map map = new HashMap();
            map.put("orderDto",orderDTO);
            map.put("amount",addressCheckResult.getResult());

            return map;
        }

        return null;
    }


    /**
     * 调用百度地图服务接口，根据寄件人地址和收件人地址计算订单距离
     * @param orderDTO
     * @return
     */
    public OrderDTO getDistance(OrderDTO orderDTO){
        //调用百度地图服务接口获取寄件人地址对应的坐标经纬度
        String begin = BaiduMapUtils.getCoordinate(orderDTO.getSenderAddress());
        if(begin == null){
            orderDTO.setSenderAddress("sender error msg");
            return orderDTO;
        }

        //调用百度地图服务接口获取收件人地址对应的坐标经纬度
        String end = BaiduMapUtils.getCoordinate(orderDTO.getReceiverAddress());
        if(end == null){
            orderDTO.setReceiverAddress("receiver error msg");
            return orderDTO;
        }

        Double distance = BaiduMapUtils.getDistance(begin, end);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String distanceStr = decimalFormat.format(distance/1000);

        orderDTO.setDistance(new BigDecimal(distanceStr));

        return orderDTO;
    }

    @Override
    @SneakyThrows
    public String caculateAgencyId(Order order){
        StringBuffer stringBuffer = new StringBuffer(); //线程安全

        Long provinceId = Long.valueOf(order.getReceiverProvinceId());
        Long cityId = Long.valueOf(order.getReceiverCityId());
        Long countyId = Long.valueOf(order.getReceiverCountyId());

        Set<Long> areaSet = new HashSet<>();
        areaSet.add(provinceId);
        areaSet.add(cityId);
        areaSet.add(countyId);

        //调用Feign接口获取对应省市区Area对象
        CompletableFuture<Map<String, Area>> future = DyCompletableFuture.areaMapFutureByCodes(areaApi, null, areaSet);
        Map<String, Area> areaMap = future.get();

        //根据id获取对应的区域Area对象
        String provinceName = areaMap.get(provinceId.toString()).getName();
        String cityName = areaMap.get(cityId.toString()).getName();
        String countyName = areaMap.get(countyId.toString()).getName();

        stringBuffer.append(provinceName).append(cityName).append(countyName).append(order.getReceiverAddress());

        String location =  stringBuffer.toString();


        List<AgencyScopeDto> agencyScopes = agencyScopeFeign.findAllAgencyScopeFix(order.getSenderCountyId() + "", null, null, null);;
        if(agencyScopes == null || agencyScopes.size() == 0){
            exceptionHappend("根据区域无法从机构范围获取网点信息列表");
        }
        String agencyId = null;
        //遍历机构范围集合
        for (AgencyScopeDto agencyScopeDto : agencyScopes){
            System.out.println("stepinto");
            List<List<Map>> mutiPoints = agencyScopeDto.getMutiPoints();
            //遍历某个机构下的保存的业务访问坐标值
            for(List<Map> maps : mutiPoints){
                String[] originArray = location.split(",");
                //判断某个点是否在指定区域范围内
                boolean flag = EntCoordSyncJob.isInScope(maps, Double.parseDouble(areaMap.get(countyId.toString()).getLng()), Double.parseDouble(areaMap.get(countyId.toString()).getLat()));
                if (flag) {
                    return agencyId = agencyScopeDto.getAgencyId();
                }
            }
        }

        return null;
    }

    public static List<List<Map>> parseData(String inputData) {
        List<List<Map<String, Object>>> result = JSON.parseObject(inputData, new TypeReference<List<List<Map<String, Object>>>>(){});
        return (List<List<Map>>)(List<?>)result;
    }






//    private CustomIdGenerator idGenerator;
//
//    @Override
//    public Order saveOrder(Order order) {
//        order.setId(idGenerator.nextId(order) + "");
//        order.setCreateTime(LocalDateTime.now());
//        order.setPaymentStatus(OrderPaymentStatus.UNPAID.getStatus());
//        if (OrderPickupType.NO_PICKUP.getCode() == order.getPickupType()) {
//            order.setStatus(OrderStatus.OUTLETS_SINCE_SENT.getCode());
//        } else {
//            order.setStatus(OrderStatus.PENDING.getCode());
//        }
//        save(order);
//        return order;
//    }
//
//    @Override
//    public IPage<Order> findByPage(Integer page, Integer pageSize, Order order) {
//        Page<Order> iPage = new Page(page, pageSize);
//        LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        if (StringUtils.isNotEmpty(order.getId())) {
//            lambdaQueryWrapper.like(Order::getId, order.getId());
//        }
//        if (order.getStatus() != null) {
//            lambdaQueryWrapper.eq(Order::getStatus, order.getStatus());
//        }
//        if (order.getPaymentStatus() != null) {
//            lambdaQueryWrapper.eq(Order::getPaymentStatus, order.getPaymentStatus());
//        }
//        //发件人信息
//        if (StringUtils.isNotEmpty(order.getSenderName())) {
//            lambdaQueryWrapper.like(Order::getSenderName, order.getSenderName());
//        }
//        if (StringUtils.isNotEmpty(order.getSenderPhone())) {
//            lambdaQueryWrapper.like(Order::getSenderPhone, order.getSenderPhone());
//        }
//        if (StringUtils.isNotEmpty(order.getSenderProvinceId())) {
//            lambdaQueryWrapper.eq(Order::getSenderProvinceId, order.getSenderProvinceId());
//        }
//        if (StringUtils.isNotEmpty(order.getSenderCityId())) {
//            lambdaQueryWrapper.eq(Order::getSenderCityId, order.getSenderCityId());
//        }
//        if (StringUtils.isNotEmpty(order.getSenderCountyId())) {
//            lambdaQueryWrapper.eq(Order::getSenderCountyId, order.getSenderCountyId());
//        }
//        //收件人信息
//        if (StringUtils.isNotEmpty(order.getReceiverName())) {
//            lambdaQueryWrapper.like(Order::getReceiverName, order.getReceiverName());
//        }
//        if (StringUtils.isNotEmpty(order.getReceiverPhone())) {
//            lambdaQueryWrapper.like(Order::getReceiverPhone, order.getReceiverPhone());
//        }
//        if (StringUtils.isNotEmpty(order.getReceiverProvinceId())) {
//            lambdaQueryWrapper.eq(Order::getReceiverProvinceId, order.getReceiverProvinceId());
//        }
//        if (StringUtils.isNotEmpty(order.getReceiverCityId())) {
//            lambdaQueryWrapper.eq(Order::getReceiverCityId, order.getReceiverCityId());
//        }
//        if (StringUtils.isNotEmpty(order.getReceiverCountyId())) {
//            lambdaQueryWrapper.eq(Order::getReceiverCountyId, order.getReceiverCountyId());
//        }
//        lambdaQueryWrapper.orderBy(true, false, Order::getId);
//        return page(iPage, lambdaQueryWrapper);
//    }
//
//    @Override
//    public List<Order> findAll(List<String> ids) {
//        LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        if (ids != null && ids.size() > 0) {
//            lambdaQueryWrapper.in(Order::getId, ids);
//        }
//        lambdaQueryWrapper.orderBy(true, false, Order::getId);
//        return list(lambdaQueryWrapper);
//    }
//
//    @Override
//    public IPage<Order> pageLikeForCustomer(OrderSearchDTO orderSearchDTO) {
//
//        Integer page = orderSearchDTO.getPage();
//        Integer pageSize = orderSearchDTO.getPageSize();
//
//        IPage<Order> ipage = new Page<>(page, pageSize);
//
//        LambdaQueryWrapper<Order> orderQueryWrapper = new LambdaQueryWrapper<>();
//        orderQueryWrapper.eq(StringUtils.isNotEmpty(orderSearchDTO.getId()), Order::getId, orderSearchDTO.getId());
//        orderQueryWrapper.like(StringUtils.isNotEmpty(orderSearchDTO.getKeyword()), Order::getId, orderSearchDTO.getKeyword());
//        orderQueryWrapper.eq(StringUtils.isNotEmpty(orderSearchDTO.getMemberId()), Order::getMemberId, orderSearchDTO.getMemberId());
//        orderQueryWrapper.eq(StringUtils.isNotEmpty(orderSearchDTO.getReceiverPhone()), Order::getReceiverPhone, orderSearchDTO.getReceiverPhone());
//        orderQueryWrapper.orderByDesc(Order::getCreateTime);
//        return page(ipage, orderQueryWrapper);
//    }
//
//    //@Autowired
//    //private KieContainer kieContainer;
//
//    /**
//     * 计算订单价格
//     * @param orderDTO
//     * @return
//     */
//    public Map calculateAmount(OrderDTO orderDTO) {
//        //计算订单距离
//        orderDTO = this.getDistance(orderDTO);
//
//        if("sender error msg".equals(orderDTO.getSenderAddress()) || "receiver error msg".equals(orderDTO.getReceiverAddress())){
//            //地址解析失败，直接返回
//            Map map = new HashMap();
//            map.put("amount","0");
//            map.put("errorMsg","无法计算订单距离和订单价格，请输入真实地址");
//            map.put("orderDto",orderDTO);
//            return map;
//        }
//
//        KieSession session = ReloadDroolsRulesService.kieContainer.newKieSession();
//        //设置Fact对象
//        AddressRule addressRule = new AddressRule();
//        addressRule.setTotalWeight(orderDTO.getOrderCargoDto().getTotalWeight().doubleValue());
//        addressRule.setDistance(orderDTO.getDistance().doubleValue());
//
//        //将对象加入到工作内存
//        session.insert(addressRule);
//
//        AddressCheckResult addressCheckResult = new AddressCheckResult();
//        session.insert(addressCheckResult);
//
//        int i = session.fireAllRules();
//        System.out.println("触发了" + i + "条规则");
//        session.destroy();
//
//        if(addressCheckResult.isPostCodeResult()){
//            System.out.println("规则匹配成功,订单价格为：" + addressCheckResult.getResult());
//            orderDTO.setAmount(new BigDecimal(addressCheckResult.getResult()));
//
//            Map map = new HashMap();
//            map.put("orderDto",orderDTO);
//            map.put("amount",addressCheckResult.getResult());
//
//            return map;
//        }
//
//        return null;
//    }
//
//    /**
//     * 调用百度地图服务接口，根据寄件人地址和收件人地址计算订单距离
//     * @param orderDTO
//     * @return
//     */
//    public OrderDTO getDistance(OrderDTO orderDTO){
//        //调用百度地图服务接口获取寄件人地址对应的坐标经纬度
//        String begin = BaiduMapUtils.getCoordinate(orderDTO.getSenderAddress());
//        if(begin == null){
//            orderDTO.setSenderAddress("sender error msg");
//            return orderDTO;
//        }
//
//        //调用百度地图服务接口获取收件人地址对应的坐标经纬度
//        String end = BaiduMapUtils.getCoordinate(orderDTO.getReceiverAddress());
//        if(end == null){
//            orderDTO.setReceiverAddress("receiver error msg");
//            return orderDTO;
//        }
//
//        Double distance = BaiduMapUtils.getDistance(begin, end);
//        DecimalFormat decimalFormat = new DecimalFormat("#.##");
//        String distanceStr = decimalFormat.format(distance/1000);
//
//        orderDTO.setDistance(new BigDecimal(distanceStr));
//
//        return orderDTO;
//    }
}
