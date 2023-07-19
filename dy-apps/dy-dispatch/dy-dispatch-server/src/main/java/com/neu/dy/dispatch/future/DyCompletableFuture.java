package com.neu.dy.dispatch.future;

import com.neu.dy.api.*;
import com.neu.dy.authority.entity.auth.User;
import com.neu.dy.authority.entity.common.Area;
import com.neu.dy.authority.entity.core.Org;
import com.neu.dy.base.R;
import com.neu.dy.base.dto.angency.AgencyScopeDto;
import com.neu.dy.base.dto.transportline.TransportLineDto;
import com.neu.dy.base.dto.transportline.TransportTripsDto;
import com.neu.dy.base.dto.truck.TruckDto;
import com.neu.dy.order.dto.OrderCargoDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DyCompletableFuture {
    /**
     * 获取机构数据列表
     *
     * @param api        数据接口
     * @param agencyType 机构类型
     * @param ids        机构id列表
     * @return 执行结果
     */
    public static final java.util.concurrent.CompletableFuture<Map<Long, Org>> agencyMapFuture(OrgApi api, Integer agencyType, Set<String> ids, Long countyId) {
        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            log.info("agencyMapFuture : {} , {} , {}", agencyType, ids, countyId);
            List<Long> idList = ids.stream().filter(item -> StringUtils.isNotBlank(item)).mapToLong(id -> Long.valueOf(id)).boxed().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(idList)) {
                R<List<Org>> result = api.list(agencyType,
                        idList,
                        countyId,
                        null,
                        new ArrayList<>());
                if (result.getIsSuccess()) {
                    return result.getData().stream().collect(Collectors.toMap(Org::getId, org -> org));
                }
            }
            return new HashMap<>();
        });
    }
//
//    public static java.util.concurrent.CompletableFuture<Map<Long, Org>> businessHallMapFuture(OrgApi feign, Set<String> set) {
//        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
//            List<Long> list = set.stream().map(item -> Long.parseLong(item)).collect(Collectors.toList());
//            R<List<Org>> orgRs = feign.listByCountyIds(OrgType.BUSINESS_HALL.getType(), list);
//            List<Org> orgs = orgRs.getData();
//            return orgs.stream().collect(Collectors.toMap(Org::getCountyId, value -> value));
//        });
//    }
//
//    public static java.util.concurrent.CompletableFuture<Map<String, String>> agencyScopeMapFuture(AgencyScopeFeign feign, Set<String> set) {
//        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
//            List<String> list = set.stream().collect(Collectors.toList());
//            List<AgencyScopeDto> agencyScopeDtos = feign.findAllAgencyScope(null, null, null, list);
//            return agencyScopeDtos.stream().collect(Collectors.toMap(AgencyScopeDto::getAreaId, value -> value.getAgencyId()));
//        });
//    }
//
//    public static java.util.concurrent.CompletableFuture<Map<String, OrderCargoDto>> orderCargoMapFuture(CargoFeign feign, Set<String> set) {
//        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
//            List<String> list = set.stream().collect(Collectors.toList());
//            List<OrderCargoDto> orderCargoDtos = feign.list(list);
//            return orderCargoDtos.stream().collect(Collectors.toMap(OrderCargoDto::getOrderId, value -> value));
//        });
//    }
//
    public static java.util.concurrent.CompletableFuture<Map<String, TransportLineDto>> transportLineIdMapFuture(TransportLineFeign api, Set<String> transportLineIdSet) {
        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            List<String> list = transportLineIdSet.stream().collect(Collectors.toList());
            List<TransportLineDto> result = api.findAll(list, null, null);
            return result.stream().collect(Collectors.toMap(TransportLineDto::getId, item -> item));
        });
    }
//
    public static java.util.concurrent.CompletableFuture<Map<String, TransportTripsDto>> tripsMapFuture(TransportTripsFeign api, Set<String> tripsIdSet) {
        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            List<String> list = tripsIdSet.stream().collect(Collectors.toList());
            List<TransportTripsDto> result = api.findAll(null, list);
            return result.stream().collect(Collectors.toMap(TransportTripsDto::getId, item -> item));
        });
    }
//
    public static java.util.concurrent.CompletableFuture<Map<String, TruckDto>> truckMapFuture(TruckFeign api, Set<String> truckIdSet) {
        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            List<String> list = truckIdSet.stream().collect(Collectors.toList());
            List<TruckDto> result = api.findAll(list, null);
            return result.stream().collect(Collectors.toMap(TruckDto::getId, item -> item));
        });
    }

    public static java.util.concurrent.CompletableFuture<Map<Long, User>> driverMapFuture(UserApi api, Set<String> driverIdSet) {
        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            List<Long> list = driverIdSet.stream().filter(item -> StringUtils.isNotBlank(item)).map(item -> Long.parseLong(item)).collect(Collectors.toList());

            R<List<User>> result = api.list(list, null, null, null);
            if (result.getIsSuccess()) {
                return result.getData().stream().collect(Collectors.toMap(User::getId, item -> item));
            }
            return new HashMap<>();
        });
    }

    public static final java.util.concurrent.CompletableFuture<Map<Long, Area>>     areaMapFuture(AreaApi api, Long parentId, Set<Long> areaSet) {
        R<List<Area>> result = api.findAll(parentId, new ArrayList<>(areaSet));
        return java.util.concurrent.CompletableFuture.supplyAsync(() ->
                result.getData().stream().collect(Collectors.toMap(Area::getId, vo -> vo)));
    }

    public static final java.util.concurrent.CompletableFuture<Map<String, Area>> areaMapFutureByCodes(AreaApi api, Long parentId, Set<Long> areaSet) {
        R<List<Area>> result = api.findAllByCodes(parentId, new ArrayList<>(areaSet));
        return java.util.concurrent.CompletableFuture.supplyAsync(() ->
                result.getData().stream().collect(Collectors.toMap(Area::getAreaCode, vo -> vo)));
    }
}
