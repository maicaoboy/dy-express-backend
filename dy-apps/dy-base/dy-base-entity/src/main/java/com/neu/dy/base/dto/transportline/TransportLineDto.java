package com.neu.dy.base.dto.transportline;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * TransportLineDto
 */
@Data
@ApiModel(value = "TransportLineDto", description = "线路信息")
public class TransportLineDto implements Serializable{
    private static final long serialVersionUID = 1168526376000160339L;
    /**
     * id
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 线路名称
     */
    @ApiModelProperty("线路名称")
    private String name;
    /**
     * 线路编号
     */
    @ApiModelProperty("线路编号")
    private String lineNumber;
    /**
     * 所属机构id
     */
    @ApiModelProperty("所属机构id")
    private String agencyId;
    /**
     * 线路类型id
     */
    @ApiModelProperty("线路类型id")
    private String transportLineTypeId;
    /**
     * 起始地机构id
     */
    @ApiModelProperty("起始地机构id")
    private String startAgencyId;
    /**
     * 目的地机构id
     */
    @ApiModelProperty("目的地机构id")
    private String endAgencyId;
    /**
     * 距离
     */
    @ApiModelProperty("距离")
    private BigDecimal distance;
    /**
     * 成本
     */
    @ApiModelProperty("成本")
    private BigDecimal cost;
    /**
     * 预计时间
     */
    @ApiModelProperty("预计时间")
    private BigDecimal estimatedTime;
    /**
     * 状态 0：禁用 1：正常
     */
    @ApiModelProperty("状态 0：禁用 1：正常")
    private Integer status;
}