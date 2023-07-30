package com.neu.dy.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neu.dy.api.AgencyScopeFeign;
import com.neu.dy.api.AreaApi;
import com.neu.dy.authority.entity.common.Area;
import com.neu.dy.base.R;
import com.neu.dy.base.dto.angency.AgencyScopeDto;
import com.neu.dy.dispatch.future.DyCompletableFuture;
import com.neu.dy.dispatch.service.OrderClassifyOrderService;
import com.neu.dy.dispatch.service.OrderClassifyService;
import com.neu.dy.dispatch.service.TaskOrderClassifyService;
import com.neu.dy.dispatch.utils.IdUtils;
import com.neu.dy.dto.OrderClassifyDTO;
import com.neu.dy.dto.OrderClassifyGroupDTO;
import com.neu.dy.entity.OrderClassifyEntity;
import com.neu.dy.entity.OrderClassifyOrderEntity;
import com.neu.dy.feign.OrderFeign;
import com.neu.dy.order.dto.OrderSearchDTO;
import com.neu.dy.order.entitiy.Order;
import com.neu.dy.order.enums.OrderStatus;
import com.neu.dy.utils.EntCoordSyncJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskOrderClassifyServiceImpl implements TaskOrderClassifyService {

    @Autowired
    private OrderFeign orderFeign;

    @Autowired
    private AreaApi areaApi;

    @Autowired
    private AgencyScopeFeign agencyScopeFeign;

    @Autowired
    private OrderClassifyService orderClassifyService;

    @Autowired
    private OrderClassifyOrderService orderClassifyOrderService;


    /**
     * 异常处理
     * @param s
     */
    private void exceptionHappend(String s) {
        try {
            throw new Exception(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 订单分类
     * @param agencyId 机构id（网点或者转运中心的id）
     * @param jobId 定时任务id
     * @param logId 日志id
     * @return
     */
    @Override
    public List<OrderClassifyGroupDTO> execute(String agencyId, String jobId, String logId) {
        //存放当前机构的所有订单(分为新订单和中转订单)
        List<OrderClassifyDTO> orderClassifyDTOS = new ArrayList<>();
        //存放分类之后的订单信息
        List<OrderClassifyGroupDTO> orderClassifyGroupDTOS = new ArrayList<>();
        //将新订单放入集合中
        orderClassifyDTOS.addAll(buildNewOrder(agencyId));
        orderClassifyDTOS.addAll(buildTransferOrder(agencyId));

        //进行订单分类,起始机构，目的机构和当前机构全部相同
        Map<String, List<OrderClassifyDTO>> orderClassifyDTOGroup = orderClassifyDTOS.stream().collect(Collectors.groupingBy(OrderClassifyDTO::groupBy));
        OrderClassifyGroupDTO.OrderClassifyGroupDTOBuilder builder = OrderClassifyGroupDTO.builder();
        //进行对象转换，将当前map对象转换为返回值对象
        orderClassifyDTOGroup.forEach((key,value)->{
            builder.key(key);
            List<Order> orders = value.stream().map(item -> {
                return item.getOrder();
            }).collect(Collectors.toList());
            builder.orders(orders);
            OrderClassifyGroupDTO orderClassifyGroupDTO = builder.build();
            orderClassifyGroupDTOS.add(orderClassifyGroupDTO);
        });
        //将分类结果保存到数据库
        saveRecord(orderClassifyGroupDTOS,jobId,logId);
        return orderClassifyGroupDTOS;
    }

    /**
     * 保存订单分类结果
     * @param orderClassifyGroupDTOS
     * @param jobId
     * @param logId
     */
    private void saveRecord(List<OrderClassifyGroupDTO> orderClassifyGroupDTOS, String jobId, String logId) {
        orderClassifyGroupDTOS.forEach(item -> {
            if (item.isNew()) {
                log.info("新订单 保存分组信息");
                OrderClassifyEntity entity = new OrderClassifyEntity();
                entity.setClassify(item.getKey());
                entity.setJobId(jobId);
                entity.setJobLogId(logId);
                if (!entity.getClassify().equals("ERROR")) {
                    entity.setStartAgencyId(item.getStartAgencyId());
                    entity.setEndAgencyId(item.getEndAgencyId());
                }
                entity.setTotal(item.getOrders().size());
                entity.setId(IdUtils.get());

                List<OrderClassifyOrderEntity> orderClassifyOrders = item.getOrders().stream().map((order) -> {
                    OrderClassifyOrderEntity orderClassifyOrderEntity = new OrderClassifyOrderEntity();
                    orderClassifyOrderEntity.setOrderId(order.getId());
                    orderClassifyOrderEntity.setOrderClassifyId(entity.getId());
                    orderClassifyOrderEntity.setId(IdUtils.get());
                    return orderClassifyOrderEntity;
                }).collect(Collectors.toList());

                item.setId(entity.getId());
                orderClassifyService.save(entity);
                orderClassifyOrderService.saveBatch(orderClassifyOrders);
            } else {
                log.info("中转订单，查询分组信息");
                List<String> orderIds = item.getOrders().stream().map(order -> order.getId()).collect(Collectors.toList());
                log.info("当前分组的订单id：{}", orderIds);
                LambdaQueryWrapper<OrderClassifyOrderEntity> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(OrderClassifyOrderEntity::getOrderId, orderIds);
                List<OrderClassifyOrderEntity> orderClassifyOrders = orderClassifyOrderService.list(wrapper);
                // 不出意外只会查到一个订单分类id
                Set<String> orderClassifyIds = orderClassifyOrders.stream().map(orderClassifyOrderEntity -> orderClassifyOrderEntity.getOrderClassifyId()).collect(Collectors.toSet());
                log.info("查询订单分组id:{}", orderClassifyIds);
                if (CollectionUtils.isEmpty(orderClassifyIds)) {
                    log.error("中转订单异常:{}", orderIds);
                    return;
                }
                item.setId(orderClassifyIds.iterator().next());
            }
        });
    }

    /**
     * 获取所有中转订单
     * @param agencyId
     * @return
     */
    private List<OrderClassifyDTO> buildTransferOrder(String agencyId) {
        OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
        //订单状态为运输中
        orderSearchDTO.setStatus(OrderStatus.IN_TRANSIT.getCode());
        //订单当前所在机构
        orderSearchDTO.setCurrentAgencyId(agencyId);
        List<Order> orders  = (List<Order>) orderFeign.list(orderSearchDTO).getData();
        log.info("查询中转状态订单：{}条",orders.size());

        OrderClassifyDTO.OrderClassifyDTOBuilder builder = OrderClassifyDTO.builder();
        builder.currentAgencyId(agencyId);
        List<OrderClassifyDTO> orderClassifyDTOS = orders.stream().map(item->{
            builder.order(item);
            //起始机构
            builder.startAgencyId(getStartAgencyId(item));
            //目的地机构
            builder.endAgencyId(getEndAgencyId(item));
            builder.orderType(item.getOrderType());
            return builder.build();
        }).collect(Collectors.toList());
        return orderClassifyDTOS;
    }

    /**
     * 获取起始机构id
     * @param order
     * @return
     */
    private String getStartAgencyId(Order order) {
        //获取收件人详细地址（包含省市区）
        String address = senderFullAddress(order);
        if(StringUtils.isBlank(address)){
            exceptionHappend("发件人地址不能为空");
        }
        //调用百度地图工具类，根据地址获取对应的经纬度坐标
        String location = EntCoordSyncJob.getCoordinate(address);
        if(StringUtils.isBlank(location)){
            exceptionHappend("发件人地址不正确");
        }
        log.info("根据地址{}获取对应的坐标值：{}",address,location);

        //根据经纬度坐标获取对应的曲线检查通过百度地图获取的区域是否和下单时选择的区域一直
        Map map = EntCoordSyncJob.getLocationByPosition(location);
        if(ObjectUtils.isEmpty(map)){
            exceptionHappend("根据经纬度获取区域信息为空");
        }

        //根据adcode(区域编码查询我们系统中的区域信息)
        String adcode = (String) map.get("adcode"); //区域编码，对应areacode
        R<Area> areaR = areaApi.getByCode("adcode" + "000000");
        Area area = areaR.getData();
        if(area == null){
            exceptionHappend("没有查询到区域数据");
        }
        if(!order.getSenderCountyId().equals(area.getId())){
            exceptionHappend("收获地址区域id和根据坐标计算出的区域不一致");
        }
        //查询当前区县下的所有网点
        R<List<AgencyScopeDto>> r = agencyScopeFeign.findAllAgencyScope(area.getId() + "", null, null, null);
        List<AgencyScopeDto> agencyScopes = r.getData();
        if(agencyScopes == null || agencyScopes.size() == 0){
            exceptionHappend("根据区域无法从机构范围获取网点信息列表");
        }
        //计算当前区域下所有网点距离发件人最近的网点
        R<String> caculate = caculate(agencyScopes, location);
        return caculate.getData();
    }

    /**
     * 获取发件人地址详细信息，用于计算起始机构网点
     * @param order
     * @return
     */
    @SneakyThrows
    private String senderFullAddress(Order order) {
        StringBuffer stringBuffer = new StringBuffer(); //线程安全

        Long provinceId = Long.valueOf(order.getSenderProvinceId());
        Long cityId = Long.valueOf(order.getSenderCityId());
        Long countyId = Long.valueOf(order.getSenderCountyId());

        Set<Long> areaSet = new HashSet<>();
        areaSet.add(provinceId);
        areaSet.add(cityId);
        areaSet.add(countyId);

        //调用Feign接口获取对应省市区Area对象
        CompletableFuture<Map<Long, Area>> future = DyCompletableFuture.areaMapFuture(areaApi, null, areaSet);
        Map<Long, Area> areaMap = future.get();

        //根据id获取对应的区域Area对象
        String provinceName = areaMap.get(provinceId).getName();
        String cityName = areaMap.get(cityId).getName();
        String countyName = areaMap.get(countyId).getName();

        stringBuffer.append(provinceName).append(cityName).append(countyName).append(order.getSenderAddress());

        return stringBuffer.toString();
    }

    /**
     * 获取指定机构下的新订单
     * @param agencyId
     * @return
     */
    @SneakyThrows
    private List<OrderClassifyDTO> buildNewOrder(String agencyId){
        OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
        //订单状态为网点入库
        orderSearchDTO.setStatus(OrderStatus.OUTLETS_WAREHOUSE.getCode());
        //订单当前所在机构
        orderSearchDTO.setCurrentAgencyId(agencyId);
        List<Order> orders  = orderFeign.list(orderSearchDTO).getData();
        log.info("查询【网点入库】状态订单：{}条",orders.size());

        OrderClassifyDTO.OrderClassifyDTOBuilder builder = OrderClassifyDTO.builder();
        builder.currentAgencyId(agencyId);
        List<OrderClassifyDTO> orderClassifyDTOS = orders.stream().map(item->{
            builder.order(item);
            //起始机构
            builder.startAgencyId(agencyId);
            //目的地机构
            builder.endAgencyId(getEndAgencyId(item));
            builder.orderType(item.getOrderType());
            return builder.build();
        }).collect(Collectors.toList());
        return orderClassifyDTOS;
    }

    /**
     * 获取订单目的地机构（网点）id
     *
     * @param order
     * @return
     */
    private String getEndAgencyId(Order order){
        //获取收件人详细地址（包含省市区）
        String address = receiverFullAddress(order);
        if(StringUtils.isBlank(address)){
            exceptionHappend("收件人地址不能为空");
        }
        //调用百度地图工具类，根据地址获取对应的经纬度坐标
        String location = EntCoordSyncJob.getCoordinate(address);
        if(StringUtils.isBlank(location)){
            exceptionHappend("收件人地址不正确");
        }
        log.info("根据地址{}获取对应的坐标值：{}",address,location);

        //根据经纬度坐标获取对应的曲线检查通过百度地图获取的区域是否和下单时选择的区域一直
        Map map = EntCoordSyncJob.getLocationByPosition(location);
        if(ObjectUtils.isEmpty(map)){
            exceptionHappend("根据经纬度获取区域信息为空");
        }

        //根据adcode(区域编码查询我们系统中的区域信息)
        String adcode = (String) map.get("adcode"); //区域编码，对应areacode
        R<Area> areaR = areaApi.getByCode(adcode);
        Area area = areaR.getData();
        if(area == null){
            exceptionHappend("没有查询到区域数据");
        }
        if(!order.getReceiverCountyId().equals(area.getAreaCode())){
            exceptionHappend("收获地址区域id和根据坐标计算出的区域不一致");
        }
        //查询当前区县下的所有网点
        R<List<AgencyScopeDto>> r = agencyScopeFeign.findAllAgencyScope(area.getAreaCode() + "", null, null, null);
        List<AgencyScopeDto> agencyScopes = r.getData();
        if(agencyScopes == null || agencyScopes.size() == 0){
            exceptionHappend("根据区域无法从机构范围获取网点信息列表");
        }
        //计算当前区域下所有网点距离收件人最近的网点
        //TODO:
        R<String> caculate = caculate(agencyScopes, location);
        return caculate.getData();
    }

    /**
     * 计算当前区域下所有网点距离收件人最近的网点
     * @param agencyScopes
     * @param location
     * @return
     */
    private R<String> caculate(List<AgencyScopeDto> agencyScopes, String location){
        //遍历机构范围集合
        for (AgencyScopeDto agencyScopeDto : agencyScopes){
            List<List<Map>> mutiPoints = agencyScopeDto.getMutiPoints();
            //遍历某个机构下的保存的业务访问坐标值
            for(List<Map> maps : mutiPoints){
                String[] originArray = location.split(",");
                //判断某个点是否在指定区域范围内
                boolean flag = EntCoordSyncJob.isInScope(maps, Double.parseDouble(originArray[0]), Double.parseDouble(originArray[1]));
                if (flag) {
                    return R.success(agencyScopeDto.getAgencyId());
                }
            }
        }
        return R.fail(5000, "获取网点失败");
    }

    public static List<List<Map>> parseData(String inputData) {
        List<List<Map>> data = new ArrayList<>();

        // Remove leading and trailing brackets
        String trimmedData = inputData.substring(1, inputData.length() - 1);

        // Split by comma to get sublists
        String[] sublistStrings = trimmedData.split(",");

        for (String sublistString : sublistStrings) {
            // Remove leading and trailing brackets
            String trimmedSublist = sublistString.trim().substring(1, sublistString.length() - 1);

            // Split by comma to get longitude and latitude
            String[] coordinateStrings = trimmedSublist.split(",");

            List<Map> sublist = new ArrayList<>();

            for (String coordinateString : coordinateStrings) {
                // Split by colon to get longitude and latitude values
                String[] coordinateValues = coordinateString.trim().split(":");

                // Create a map to store longitude and latitude
                Map coordinateMap = new HashMap<>();
                coordinateMap.put("longitude", Double.parseDouble(coordinateValues[0]));
                coordinateMap.put("latitude", Double.parseDouble(coordinateValues[1]));

                sublist.add(coordinateMap);
            }

            data.add(sublist);
        }

        return data;
    }


    /**
     * 获取坐标值
     * @param map
     * @return
     */
    private String getPoint(Map map){
        String lng = map.get("lng").toString();
        String lat = map.get("lat").toString();
        return lng + "," +lat;
    }

    /**
     * 根据订单获取对应的完整收件人地址信息
     * @param order
     * @return
     */
    @SneakyThrows
    private String receiverFullAddress(Order order){
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
//        CompletableFuture<Map<Long, Area>> future = DyCompletableFuture.areaMapFuture(areaApi, null, areaSet);
//        Map<Long, Area> areaMap = future.get();

        //根据id获取对应的区域Area对象
        String provinceName = areaMap.get(provinceId.toString()).getName();
        String cityName = areaMap.get(cityId.toString()).getName();
        String countyName = areaMap.get(countyId.toString()).getName();

        stringBuffer.append(provinceName).append(cityName).append(countyName).append(order.getReceiverAddress());

        return stringBuffer.toString();
    }


    //    public static void main(String[] args) {
//        List<AgencyScopeDto> agencyScopeList = new ArrayList<>();
//        AgencyScopeDto dto1 = new AgencyScopeDto();
//        dto1.setAgencyId("1");
//        List<List<Map>> points1 = new ArrayList<>();
//        List<Map> maps = new ArrayList<>();
//        Map point1 = new HashMap();
//        point1.put("lng",116.337566);
//        point1.put("lat",40.069744);
//        Map point2 = new HashMap();
//        point2.put("lng",116.362215);
//        point2.put("lat",40.0741);
//
//        maps.add(point1);
//        maps.add(point2);
//
//        points1.add(maps);
//
//        dto1.setMutiPoints(points1);
//
//        AgencyScopeDto dto2 = new AgencyScopeDto();
//        dto2.setAgencyId("2");
//        List<List<Map>> points2 = new ArrayList<>();
//        List<Map> maps2 = new ArrayList<>();
//        Map point3 = new HashMap();
//        point3.put("lng",116.3344);
//        point3.put("lat",40.067);
//        Map point4 = new HashMap();
//        point4.put("lng",116.311215);
//        point4.put("lat",40.10741);
//
//        maps2.add(point3);
//        maps2.add(point4);
//
//        points2.add(maps2);
//
//        dto2.setMutiPoints(points2);
//
//        agencyScopeList.add(dto1);
//        agencyScopeList.add(dto2);
//        String location = "116.349936,40.066258";
//        System.out.println(new TaskOrderClassifyServiceImpl().caculate(agencyScopeList,location).getData());
//    }
}
