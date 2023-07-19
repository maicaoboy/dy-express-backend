package com.neu.dy.base.entity.agency;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.neu.dy.base.handler.MutiPointsTypeHandler;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName(value = "dy_agency_scope", autoResultMap = true)
public class DyAgencyScope implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 机构id
     */
    private String agencyId;

    /**
     * 行政区域id
     */
    private String areaId;

    /**
     * 多边形经纬度坐标集合
     */
//   自定义转化规则
    @TableField(typeHandler= MutiPointsTypeHandler.class)
    private List<List<Map>> mutiPoints;
}