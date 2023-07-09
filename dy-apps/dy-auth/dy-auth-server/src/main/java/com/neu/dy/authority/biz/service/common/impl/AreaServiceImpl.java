package com.neu.dy.authority.biz.service.common.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.authority.biz.dao.common.AreaMapper;
import com.neu.dy.authority.biz.dao.common.OptLogMapper;
import com.neu.dy.authority.biz.service.common.AreaService;
import com.neu.dy.authority.biz.service.common.OptLogService;
import com.neu.dy.authority.entity.common.Area;
import com.neu.dy.authority.entity.common.OptLog;
import com.neu.dy.dozer.DozerUtils;
import com.neu.dy.log.entity.OptLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业务实现类
 * 操作日志
 */
@Slf4j
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
}