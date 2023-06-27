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
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("dy_truck_license")
public class DyTruckLicense implements Serializable {

    private static final long serialVersionUID = 1L;
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
     * 发动机编号
     */
    private String engineNumber;

    /**
     * 注册时间
     */
    private LocalDate registrationDate;

    /**
     * 国家强制报废日期
     */
    private LocalDate mandatoryScrap;

    /**
     * 检验有效期
     */
    private LocalDate expirationDate;

    /**
     * 整备质量
     */
    private BigDecimal overallQuality;

    /**
     * 核定载质量
     */
    private BigDecimal allowableWeight;

    /**
     * 外廓尺寸
     */
    private String outsideDimensions;

    /**
     * 行驶证有效期
     */
    private LocalDate validityPeriod;

    /**
     * 道路运输证号
     */
    private String transportCertificateNumber;

    /**
     * 图片信息
     */
    private String picture;
}
