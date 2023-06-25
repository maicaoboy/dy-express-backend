package com.neu.dy.authority.biz.service.commom.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.authority.biz.dao.common.OptLogMapper;
import com.neu.dy.authority.biz.service.commom.OptLogService;
import com.neu.dy.authority.entity.common.OptLog;
import com.neu.dy.dozer.DozerUtils;
import com.neu.dy.log.entity.OptLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname OptLogServiceImpl
 * @Description 业务实现,操作日志
 * @Version 1.0.0
 * @Date 2023/6/25 14:26
 * @Created by maicaoboy
 */
@Slf4j
@Service
public class OptLogServiceImpl extends ServiceImpl<OptLogMapper, OptLog>
        implements OptLogService {
    @Autowired
    DozerUtils dozer;

    @Override
    public boolean save(OptLogDTO entity) {
        return super.save(dozer.map(entity, OptLog.class));
    }
}
