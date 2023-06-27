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
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("dy_transport_line")
public class DyTransportLine implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 线路名称
     */
    private String name;

    /**
     * 线路编号
     */
    private String lineNumber;

    /**
     * 所属机构
     */
    private String agencyId;
    /**
     * 线路类型
     */
    private String transportLineTypeId;

    /**
     * 起始地机构id
     */
    private String startAgencyId;

    /**
     * 目的地机构id
     */
    private String endAgencyId;

    /**
     * 距离
     */
    private BigDecimal distance;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 预计时间
     */
    private BigDecimal estimatedTime;

    /**
     * 状态 0：禁用 1：正常
     */
    private Integer status;
}
