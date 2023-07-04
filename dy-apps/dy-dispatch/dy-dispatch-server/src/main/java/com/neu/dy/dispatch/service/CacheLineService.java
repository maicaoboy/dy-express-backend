/**
 * Copyright (c) 2019 联智合创 All rights reserved.
 * <p>
 * http://www.witlinked.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.neu.dy.dispatch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.entity.CacheLineEntity;

/**
 * 缓冲线路
 *
 * @author
 */
public interface CacheLineService extends IService<CacheLineEntity> {

    String check(String verifyKey);

    int saveAndUpdate(CacheLineEntity cacheLineEntity);

    Integer deleteOldAndGetNewVersion(String notEqualsLineStart, String notEqualsLineEnd);
}
