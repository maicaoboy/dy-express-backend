package com.neu.dy.base.dto.truck;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * TruckTypeDto
 */
@Data
public class TruckTypeDto implements Serializable {
    private static final long serialVersionUID = -2762539095338170845L;
    /**
     * id
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 车辆类型名称
     */
    @ApiModelProperty("车辆类型名称")
    private String name;
    /**
     * 准载重量
     */
    @ApiModelProperty("准载重量")
    private BigDecimal allowableLoad;
    /**
     * 准载体积
     */
    @ApiModelProperty("准载体积")
    private BigDecimal allowableVolume;
    /**
     * 长
     */
    @ApiModelProperty("长")
    private BigDecimal measureLong;
    /**
     * 宽
     */
    @ApiModelProperty("宽")
    private BigDecimal measureWidth;
    /**
     * 高
     */
    @ApiModelProperty("高")
    private BigDecimal measureHigh;
    /**
     * 货物类型id列表
     */
    @ApiModelProperty("货物类型id1列表")
    private List<String> goodsTypeIds;
}