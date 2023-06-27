package com.neu.dy.base.biz.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.user.DyCourierScope;

import java.util.List;


public interface IDyCourierScopeService extends IService<DyCourierScope> {
    /**
     * 批量保存快递员业务范围
     *
     * @param scopeList 快递员业务范围信息列表
     */
    void batchSave(List<DyCourierScope> scopeList);

    /**
     * 删除快递员业务范围
     *
     * @param areaId 行政区域id
     * @param userId 快递员id
     */
    void delete(String areaId, String userId);

    List<DyCourierScope> findAll(String areaId, String userId);
}
