package com.neu.dy.base.entity.base;

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

/**
 * @Classname DyGoodsType
 * @Description 货物类型
 * @Version 1.0.0
 * @Date 2023/6/20 21:48
 * @Created by maicaoboy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("dy_goods_type")
public class DyGoodsType implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 货物类型名称
     */
    private String name;
    /**
     * 默认重量，单位：千克
     */
    private BigDecimal defaultWeight;
    /**
     * 默认体积，单位：方
     */
    private BigDecimal defaultVolume;
    /**
     * 说明
     */
    private String remark;
    /**
     * 状态 0：禁用 1：正常
     */
    private Integer status;
}
