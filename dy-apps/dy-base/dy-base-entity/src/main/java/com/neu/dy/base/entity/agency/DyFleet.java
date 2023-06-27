package com.neu.dy.base.entity.agency;

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
@TableName("dy_fleet")
public class DyFleet implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 车队名称
     */
    private String name;

    /**
     * 车队编号
     */
    private String fleetNumber;

    /**
     * 所属机构
     */
    private String agencyId;

    /**
     * 负责人
     */
    private String manager;
    /**
     * 状态 0：禁用 1：正常
     */
    private Integer status;
}
