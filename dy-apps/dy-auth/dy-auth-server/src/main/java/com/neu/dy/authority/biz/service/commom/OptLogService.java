package com.neu.dy.authority.biz.service.commom;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.authority.entity.common.OptLog;
import com.neu.dy.log.entity.OptLogDTO;

/**
 * @Classname OptLogService
 * @Description 操作日志
 * @Version 1.0.0
 * @Date 2023/6/25 14:26
 * @Created by maicaoboy
 */
public interface OptLogService extends IService<OptLog> {
    /**
     * 保存日志
     */
    boolean save(OptLogDTO entity);
}
