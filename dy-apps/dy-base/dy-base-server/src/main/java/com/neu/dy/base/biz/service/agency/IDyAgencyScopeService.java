package com.neu.dy.base.biz.service.agency;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.base.entity.agency.DyAgencyScope;


import java.util.List;

/**
 * <p>
 * 机构业务范围表  服务类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
public interface IDyAgencyScopeService extends IService<DyAgencyScope> {
    /**
     * 批量保存机构业务范围
     *
     * @param scopeList 机构业务范围信息列表
     */
    void batchSave(List<DyAgencyScope> scopeList);

    /**
     * 删除机构业务范围
     *
     * @param areaId   行政区域id
     * @param agencyId 机构id
     */
    void delete(String areaId, String agencyId);

    List<DyAgencyScope> findAll(String areaId, String agencyId, List<String> agencyIds, List<String> areaIds);
}
