package com.phungdung.gnoc2.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO {
    private String sqlQuery;
    private Map<String, Object> parametes;
    private Integer page;
    private Integer pageSize;
    private String sortType;
    private String sortName;
    private Integer totalRow;
    private Integer indexRow;
    private String searchAll;
    private String timeOffset;
}
