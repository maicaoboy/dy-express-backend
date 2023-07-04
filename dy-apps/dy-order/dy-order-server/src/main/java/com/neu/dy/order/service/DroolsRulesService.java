package com.neu.dy.order.service;

import com.neu.dy.order.entitiy.fact.AddressRule;

public interface DroolsRulesService {
    /**
     * 根据条件计算订单价格
     * @param addressRule
     * @return
     */
    String calcFee(AddressRule addressRule);
}
