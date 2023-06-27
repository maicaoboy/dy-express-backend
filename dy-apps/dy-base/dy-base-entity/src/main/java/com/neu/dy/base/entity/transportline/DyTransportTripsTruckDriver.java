package com.neu.dy.base.entity.transportline;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("dy_transport_trips_truck_driver")
public class DyTransportTripsTruckDriver implements Serializable {
    private static final long serialVersionUID = 2060686653575483040L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 车辆id
     */
    private String truckId;
    /**
     * 车次id
     */
    private String transportTripsId;
    /**
     * 司机id
     */
    private String userId;
}
