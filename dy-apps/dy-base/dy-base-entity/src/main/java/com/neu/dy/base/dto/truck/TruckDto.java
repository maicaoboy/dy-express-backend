package com.neu.dy.base.dto.truck;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * TruckDto
 */
@Data
public class TruckDto implements Serializable {
    private static final long serialVersionUID = 4541866216281387846L;
    /**
     * id
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 车辆类型id
     */
    @ApiModelProperty("车辆类型id")
    private String truckTypeId;

    @ApiModelProperty("车辆类型名称")
    private String truckTypeName;
    /**
     * 所属车队id
     */
    @ApiModelProperty("所属车队id")
    private String fleetId;
    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    private String brand;
    /**
     * 车牌号码
     */
    @ApiModelProperty("车牌号码")
    private String licensePlate;
    /**
     * GPS设备id

    @ApiModelProperty("GPS设备id（暂时未使用）")
    private String deviceGpsId;
     */
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


    @ApiModelProperty("车次编号")
    private List<String> transportTripsId;

    /**
     * 状态 0：禁用 1：正常
     */
    @ApiModelProperty("状态")
    private Integer status;
}