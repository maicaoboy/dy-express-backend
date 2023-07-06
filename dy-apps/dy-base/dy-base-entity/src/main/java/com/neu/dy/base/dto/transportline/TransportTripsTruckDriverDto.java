package com.neu.dy.base.dto.transportline;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * TransportTripsTruckDriverDto
 */
@Data
@ApiModel(value = "TransportTripsTruckDriverDto", description = "车次信息")
public class TransportTripsTruckDriverDto implements Serializable {
    private static final long serialVersionUID = -477835165829525987L;
    /**
     * id
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 车辆id
     */
    @ApiModelProperty("车辆id")
    private String truckId;
    /**
     * 车次id
     */
    @ApiModelProperty("车次id")
    private String transportTripsId;
    /**
     * 司机Id
     */
    @ApiModelProperty("司机Id")
    private String userId;
}