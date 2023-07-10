package com.neu.dy.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neu.dy.authority.dto.core.OrgSaveDTO;
import com.neu.dy.authority.dto.core.OrgTreeDTO;
import com.neu.dy.authority.dto.core.OrgUpdateDTO;
import com.neu.dy.authority.entity.core.Org;
import com.neu.dy.base.R;
import com.neu.dy.base.entity.SuperEntity;
import com.neu.dy.log.annotation.SysLog;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "dy-auth-server", path = "/org")
public interface OrgApi {

    @ApiOperation(value = "分页查询组织", notes = "分页查询组织")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "long", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页显示几条", dataType = "long", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/page")
    @SysLog("分页查询组织")
    public R<IPage<Org>> page(Org data);

    @ApiOperation(value = "查询组织", notes = "查询组织")
    @GetMapping("/{id}")
    @SysLog("查询组织")
    public R<Org> get(@PathVariable Long id);


    @ApiOperation(value = "查询系统所有的组织树", notes = "查询系统所有的组织树")
    @GetMapping("/tree")
    @SysLog("查询系统所有的组织树")
    public R<List<OrgTreeDTO>> tree(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "status", required = false) Boolean status);

    @GetMapping
    R<List<Org>> list(@RequestParam(name = "orgType", required = false) Integer paramInteger, @RequestParam(name = "ids", required = false) List<Long> paramList1, @RequestParam(name = "countyId", required = false) Long paramLong1, @RequestParam(name = "pid", required = false) Long paramLong2, @RequestParam(name = "pids", required = false) List<Long> paramList2);

}
