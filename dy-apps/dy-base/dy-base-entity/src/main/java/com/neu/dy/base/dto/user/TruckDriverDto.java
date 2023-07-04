package com.neu.dy.base.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * TruckDriverDto,与entity一致
 */
@Data
public class TruckDriverDto implements Serializable {
    private static final long serialVersionUID = 4960262265247824283L;
    /**
     * id
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 用户Id
     */
    @ApiModelProperty("用户id")
    private String userId;
    /**
     * 所属车队id
     */
    @ApiModelProperty("所属车队")
    private String fleetId;
    /**
     * 年龄
     */
    @ApiModelProperty("年龄")
    private Integer age;
    /**
     * 照片
     */
    @ApiModelProperty("照片？")
    private String picture;
    /**
     * 驾龄
     */
    @ApiModelProperty("驾龄")
    private Integer drivingAge;
}