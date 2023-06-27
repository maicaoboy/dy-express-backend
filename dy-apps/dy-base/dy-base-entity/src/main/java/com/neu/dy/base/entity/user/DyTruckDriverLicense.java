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
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("dy_truck_driver_license")
public class DyTruckDriverLicense implements Serializable {
    private static final long serialVersionUID = 6323283009431070954L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 准驾车型
     */
    private String allowableType;

    /**
     * 初次领证日期
     */
    private LocalDate initialCertificateDate;

    /**
     * 有效期限
     */
    private String validPeriod;

    /**
     * 驾驶证号
     */
    private String licenseNumber;

    /**
     * 驾龄
     */
    private Integer driverAge;

    /**
     * 驾驶证类型
     */
    private String licenseType;

    /**
     * 从业资格证信息
     */
    private String qualificationCertificate;

    /**
     * 入场证信息
     */
    private String passCertificate;

    /**
     * 图片
     */
    private String picture;
}
