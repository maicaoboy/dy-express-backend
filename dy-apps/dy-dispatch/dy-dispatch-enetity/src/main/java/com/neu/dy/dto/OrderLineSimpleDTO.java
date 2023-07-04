package com.neu.dy.dto;

import com.neu.dy.entity.CacheLineDetailEntity;
import lombok.Data;

import java.util.List;

@Data
public class OrderLineSimpleDTO {

    private CacheLineDetailEntity cacheLineDetailEntity;

    private List<OrderClassifyGroupDTO> orderClassifyGroupDTOS;

    public OrderLineSimpleDTO(CacheLineDetailEntity cacheLineDetailEntity, List<OrderClassifyGroupDTO> orderClassifyGroupDTOS) {
        this.cacheLineDetailEntity = cacheLineDetailEntity;
        this.orderClassifyGroupDTOS = orderClassifyGroupDTOS;
    }
}
