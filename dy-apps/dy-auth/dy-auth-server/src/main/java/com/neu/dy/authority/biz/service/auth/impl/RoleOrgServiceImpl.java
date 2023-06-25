package com.neu.dy.authority.biz.service.auth.impl;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.authority.biz.dao.auth.RoleOrgMapper;
import com.neu.dy.authority.biz.service.auth.RoleOrgService;
import com.neu.dy.authority.entity.auth.RoleOrg;
import com.neu.dy.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * 业务实现类
 * 角色组织关系
 */
@Slf4j
@Service
public class RoleOrgServiceImpl extends ServiceImpl<RoleOrgMapper, RoleOrg> implements RoleOrgService {
    @Override
    public List<Long> listOrgByRoleId(Long id) {
        List<RoleOrg> list = super.list(Wraps.<RoleOrg>lbQ().eq(RoleOrg::getRoleId, id));
        List<Long> orgList = list.stream().mapToLong(RoleOrg::getOrgId).boxed().collect(Collectors.toList());
        return orgList;
    }
}