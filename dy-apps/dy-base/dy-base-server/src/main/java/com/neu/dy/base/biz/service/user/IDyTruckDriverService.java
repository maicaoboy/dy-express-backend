package com.neu.dy.base.biz.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.user.DyTruckDriver;

import java.util.List;

/**
 * <p>
 * 司机表 服务类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
public interface IDyTruckDriverService extends IService<DyTruckDriver> {
    /**
     * 添加司机
     *
     * @param dyTruckDriver 司机信息
     * @return 司机信息
     */
    DyTruckDriver saveTruckDriver(DyTruckDriver dyTruckDriver);

    /**
     * 获取司机基本信息列表
     *
     * @param userIds 司机id列表
     * @return 司机基本信息列表
     */
    List<DyTruckDriver> findAll(List<String> userIds,String fleetId);

    /**
     * 获取司机基本信息
     *
     * @param userId 司机id
     * @return 司机基本信息
     */
    DyTruckDriver findOne(String userId);


    /**
     * 统计司机数量
     * @param fleetId 车队id
     * @return 司机数量
     */
    Integer count(String fleetId);

    /**
     * 获取司机分页数据
     *
     * @param page         页码
     * @param pageSize     页尺寸
     * @param fleetId  车队id
     * @return 司机分页数据
     */
    IPage<DyTruckDriver> findByPage(Integer page, Integer pageSize, String fleetId);
}
