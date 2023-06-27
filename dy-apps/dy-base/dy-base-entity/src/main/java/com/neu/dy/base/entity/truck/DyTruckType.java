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
@TableName("dy_truck_type")
public class DyTruckType implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 车辆类型名称
     */
    private String name;

    /**
     * 准载重量
     */
    private BigDecimal allowableLoad;

    /**
     * 准载体积
     */
    private BigDecimal allowableVolume;

    /**
     * 长
     */
    private BigDecimal measureLong;

    /**
     * 宽
     */
    private BigDecimal measureWidth;

    /**
     * 高
     */
    private BigDecimal measureHigh;

    /**
     * 状态 0：禁用 1：正常
     */
    private Integer status;
}
