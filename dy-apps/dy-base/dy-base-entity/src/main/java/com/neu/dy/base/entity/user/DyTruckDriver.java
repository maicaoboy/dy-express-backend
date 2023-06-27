package com.neu.dy.base.entity.user;

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
@TableName("dy_truck_driver")
public class DyTruckDriver implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 用户id，来自用户表
     */
    private String userId;

    /**
     * 所属车队id
     */
    private String fleetId;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 图片
     */
    private String picture;

    /**
     * 驾龄
     */
    private Integer drivingAge;


}
