package com.neu.dy.base.controller.transportline;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.neu.dy.auth.client.utils.JwtTokenClientUtils;
import com.neu.dy.base.R;
import com.neu.dy.base.biz.service.transportline.IDyTransportLineTypeService;
import com.neu.dy.base.dto.transportline.TransportLineTypeDto;
import com.neu.dy.base.entity.transportline.DyTransportLineType;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import com.neu.dy.base.common.PageResponse;
import com.neu.dy.base.common.Result;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TransportLineTypeController
 */
@RestController
@RequestMapping("base/transportLine/type")
@Api(tags = "线路类型管理")
public class TransportLineTypeController {
    @Autowired
    private IDyTransportLineTypeService transportLineTypeService;

    /**
     * 添加线路类型
     *
     * @param dto 线路类型信息
     * @return 线路类型信息
     */
    @PostMapping("")
    @ApiOperation(value = "添加线路类型")
    public R saveTransportLineType(@RequestBody TransportLineTypeDto dto, HttpServletRequest request ) {
        DyTransportLineType dyTransportLineType = new DyTransportLineType();
        BeanUtils.copyProperties(dto, dyTransportLineType);
        String jwtKeyName = request.getHeader("name");
        dyTransportLineType.setUpdater(jwtKeyName);
        dyTransportLineType = transportLineTypeService.saveTransportLineType(dyTransportLineType);
//        BeanUtils.copyProperties(dyTransportLineType, dto);
        return R.success();
    }

    /**
     * 根据id获取线路类型详情
     *
     * @param id 线路类型id
     * @return 线路类型详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取线路类型详情")
    public TransportLineTypeDto fineById(@PathVariable(name = "id") String id) {
        DyTransportLineType dyTransportLineType = transportLineTypeService.getById(id);
        TransportLineTypeDto dto = new TransportLineTypeDto();
        BeanUtils.copyProperties(dyTransportLineType, dto);
        return dto;
    }

    /**
     * 获取线路类型分页数据
     *
     * @param page       页码
     * @param pageSize   页尺寸
     * @param typeNumber 类型编号
     * @param name       类型名称
     * @param agencyType 机构类型
     * @return 线路类型分页数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取线路类型分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeNumber", value = "线路编码", dataType = "String", required = false),
            @ApiImplicitParam(name = "name", value = "线路名称", dataType = "String", required = false),
            @ApiImplicitParam(name = "agencyType", value = "机构类型（初始机构或终止机构类型）", dataType = "Integer", required = false)
    })
    public PageResponse<TransportLineTypeDto> findByPage(@RequestParam(name = "page") Integer page,
                                                         @RequestParam(name = "pageSize") Integer pageSize,
                                                         @RequestParam(name = "typeNumber", required = false) String typeNumber,
                                                         @RequestParam(name = "name", required = false) String name,
                                                         @RequestParam(name = "agencyType", required = false) Integer agencyType) {
        IPage<DyTransportLineType> transportLineTypePage = transportLineTypeService.findByPage(page, pageSize, typeNumber, name, agencyType);
        List<TransportLineTypeDto> dtoList = new ArrayList<>();
        transportLineTypePage.getRecords().forEach(dyTransportLineType -> {
            TransportLineTypeDto dto = new TransportLineTypeDto();
            BeanUtils.copyProperties(dyTransportLineType, dto);
            dtoList.add(dto);
        });
        return PageResponse.<TransportLineTypeDto>builder().items(dtoList).pagesize(pageSize).page(page)
                .counts(transportLineTypePage.getTotal()).pages(transportLineTypePage.getPages()).build();
    }

    /**
     * 获取线路类型列表
     *
     * @param ids 线路类型id列表
     * @return 线路类型列表
     */
    @GetMapping("")
    @ApiOperation(value = "根据id集合获取线路类型列表")
    public List<TransportLineTypeDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids) {
        return transportLineTypeService.findAll(ids).stream().map(dyTransportLineType -> {
            TransportLineTypeDto dto = new TransportLineTypeDto();
            BeanUtils.copyProperties(dyTransportLineType, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 更新线路类型信息
     *
     * @param id  线路类型id
     * @param dto 线路类型信息
     * @return 线路类型信息
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新线路类型信息")
    public R update(@PathVariable(name = "id") String id, @RequestBody TransportLineTypeDto dto) {
        dto.setId(id);
        DyTransportLineType dyTransportLineType = new DyTransportLineType();
        BeanUtils.copyProperties(dto, dyTransportLineType);
        dyTransportLineType.setLastUpdateTime(LocalDateTime.now());
        transportLineTypeService.updateById(dyTransportLineType);
        return R.success();
    }

    /**
     * 删除线路类型
     *
     * @param id 线路类型id
     * @return 返回信息
     */
    @ApiOperation(value = "删除线路类型")
    @DeleteMapping("/{id}")
    public R disable(@PathVariable(name = "id") String id) {
        transportLineTypeService.disableById(id);
        return R.success();
    }
}
