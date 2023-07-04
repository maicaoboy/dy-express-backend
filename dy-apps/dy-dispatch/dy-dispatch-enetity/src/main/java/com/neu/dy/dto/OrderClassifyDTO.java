package com.neu.dy.dto;

import com.neu.dy.order.entitiy.Order;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 封装原始订单信息
 */
@Data
@Builder
public class OrderClassifyDTO {

    /**
     * 分组信息
     * 可继续扩展
     */
    private String startAgencyId;
    private String endAgencyId;
    private Integer orderType;
    private String goodsType;
    private String currentAgencyId;

    public String groupBy() {
        if (StringUtils.isNotBlank(this.startAgencyId) && StringUtils.isNotBlank(this.endAgencyId) && StringUtils.isNotBlank(this.currentAgencyId)) {
            return this.startAgencyId + "#" + this.endAgencyId + "#" + this.currentAgencyId;
        } else {
            return "ERROR";
        }
    }

    /**
     * 原订单
     */
    private Order order;

}
