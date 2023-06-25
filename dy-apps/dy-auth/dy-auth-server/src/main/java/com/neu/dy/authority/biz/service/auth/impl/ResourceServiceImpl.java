package com.neu.dy.authority.biz.service.auth.impl;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.authority.biz.service.auth.ResourceService;
import com.neu.dy.authority.dto.auth.ResourceQueryDTO;
import com.neu.dy.authority.entity.auth.Resource;
import com.neu.dy.base.id.CodeGenerate;
import com.neu.dy.common.constant.CacheKey;
import com.neu.dy.database.mybatis.conditions.Wraps;
import com.neu.dy.exception.BizException;
import com.neu.dy.utils.StrHelper;
import com.neu.dy.authority.biz.dao.auth.ResourceMapper;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
/**
 * 业务实现类
 * 资源
 */
@Slf4j
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {
    @Autowired
    private CacheChannel cache;
    @Autowired
    private CodeGenerate codeGenerate;
    /**
     * 查询用户的可用资源
     */
    @Override
    public List<Resource> findVisibleResource(ResourceQueryDTO resourceQueryDTO) {
        //查询当前用户可访问的资源
        List<Resource> visibleResource = baseMapper.findVisibleResource(resourceQueryDTO);
        return visibleResource;
    }

    @Override
    public void removeByMenuId(List<Long> menuIds) {
        List<Resource> resources = super.list(Wraps.<Resource>lbQ().in(Resource::getMenuId, menuIds));
        if (resources.isEmpty()) {
            return;
        }
        List<Long> idList = resources.stream().mapToLong(Resource::getId).boxed().collect(Collectors.toList());
        super.removeByIds(idList);
    }

    @Override
    public boolean save(Resource resource) {
        resource.setCode(StrHelper.getOrDef(resource.getCode(), codeGenerate.next()));
        if (super.count(Wraps.<Resource>lbQ().eq(Resource::getCode, resource.getCode())) > 0) {
            throw BizException.validFail("编码[%s]重复", resource.getCode());
        }
        super.save(resource);
        return true;
    }

    @Override
    public List<Long> findMenuIdByResourceId(List<Long> resourceIdList) {
        return baseMapper.findMenuIdByResourceId(resourceIdList);
    }
}