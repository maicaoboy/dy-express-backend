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
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("dy_transport_line_type")
public class DyTransportLineType implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 线路类型名称
     */
    private String name;
    /**
     * 线路类型编码
     */
    private String typeNumber;

    /**
     * 起始地机构类型
     */
    private Integer startAgencyType;

    /**
     * 目的地机构类型
     */
    private Integer endAgencyType;

    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdateTime;

    /**
     * 最后更新人
     */
    private String updater;

    /**
     * 状态  0：禁用   1：正常
     */
    private Integer status;
}
