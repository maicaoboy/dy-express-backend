/**
 * Copyright (c) 2019 联智合创 All rights reserved.
 * <p>
 * http://www.witlinked.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.neu.dy.dispatch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.entity.CacheLineDetailEntity;

import java.util.List;

/**
 * 缓冲线路子表
 *
 * @author
 */
public interface CacheLineDetailService extends IService<CacheLineDetailEntity> {

    List<CacheLineDetailEntity> findByCacheLineId(String cacheLineId);
}
