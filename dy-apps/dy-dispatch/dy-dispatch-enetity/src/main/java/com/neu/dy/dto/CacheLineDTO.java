package com.neu.dy.dto;

import com.neu.dy.entity.CacheLineEntity;
import lombok.Data;

import java.util.List;

@Data
public class CacheLineDTO extends CacheLineEntity {

    private List<CacheLineDetailDTO> cacheLineDetailDTOS;

}
