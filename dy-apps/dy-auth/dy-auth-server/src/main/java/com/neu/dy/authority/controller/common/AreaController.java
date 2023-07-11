package com.neu.dy.authority.controller.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neu.dy.authority.biz.service.common.AreaService;
import com.neu.dy.authority.entity.common.Area;
import com.neu.dy.base.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.el.LambdaExpression;
import java.util.List;

@RestController
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @PostMapping("")
    public void save(@RequestBody List<Area> areas){
        areaService.saveBatch(areas);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping({"/{id}"})
    R<Area> get(@PathVariable Long id){
        Area area = areaService.getById(id);
        return R.success(area);
    }

    /**
     * 根据编码获取
     * @param code
     * @return
     */
    @GetMapping({"/code/{code}"})
    R<Area> getByCode(@PathVariable String code){
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(code!=null,Area::getAreaCode,code);
        Area area = areaService.getOne(queryWrapper);
        return R.success(area);
    }

    @GetMapping
    R<List<Area>> findAll(@RequestParam(value = "parentId", required = false) Long parentId, @RequestParam(value = "ids", required = false) List<Long> ids){
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(parentId != null,Area::getParentId,parentId);
        if(ids != null && ids.size() > 0){
            queryWrapper.in(Area::getId,ids);
        }
        List<Area> areaList = areaService.list(queryWrapper);
        return R.success(areaList);
    }
}
