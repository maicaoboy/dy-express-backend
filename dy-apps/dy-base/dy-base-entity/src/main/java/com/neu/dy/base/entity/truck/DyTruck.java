package com.neu.dy.base.entity.truck;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("dy_truck")
public class DyTruck implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 车辆类型id
     */
    private String truckTypeId;

    /**
     * 所属车队id
     */
    private String fleetId;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 车牌号码
     */
    private String licensePlate;

    /**
     * GPS设备id（暂时未用到）

    private String deviceGpsId;
     */

    /**
     * 准载重量
     */
    private BigDecimal allowableLoad;

    /**
     * 准载体积
     */
    private BigDecimal allowableVolume;

    /**
     * 车辆行驶证信息id(已经不需要了）

    private String truckLicenseId;
     */
    
    /**
     * 状态 0：禁用 1：正常
     */
    private Integer status;
}
