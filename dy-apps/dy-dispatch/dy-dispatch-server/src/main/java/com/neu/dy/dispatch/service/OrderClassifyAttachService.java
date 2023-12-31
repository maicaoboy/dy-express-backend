/**
 * Copyright (c) 2019 联智合创 All rights reserved.
 * <p>
 * http://www.witlinked.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.neu.dy.dispatch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.entity.OrderClassifyAttachEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车次车辆司机订单分类关联
 *
 * @author
 */
@Service
public interface OrderClassifyAttachService extends IService<OrderClassifyAttachEntity> {

    List<OrderClassifyAttachEntity> findAttachByClassifyId(String classifyId);

}
