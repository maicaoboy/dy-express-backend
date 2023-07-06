package com.neu.dy.base.dto.transportline;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TransportLineType
 */
@Data
@ApiModel(value = "TransportLineTypeDto", description = "线路类型信息")
public class TransportLineTypeDto implements Serializable {
    private static final long serialVersionUID = -7006861734336133221L;
    /**
     * id
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 线路类型名称
     */
    @ApiModelProperty("线路类型名称")
    private String name;
    /**
     * 线路类型编码
     */
    @ApiModelProperty("线路类型编码")
    private String typeNumber;
    /**
     * 起始地机构类型
     */
    @ApiModelProperty("起始地机构类型")
    private Integer startAgencyType;
    /**
     * 目的地机构类型
     */
    @ApiModelProperty("目的地机构类型")
    private Integer endAgencyType;
    /**
     * 最后更新时间
     */
    @ApiModelProperty("最后更新时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime lastUpdateTime;
    /**
     * 最后更新人id
     */
    @ApiModelProperty("最后更新人id")
    private String updater;
    /**
     * 状态 0：禁用 1：正常
     */
    @ApiModelProperty("状态 0：禁用 1：正常")
    private Integer status;
}