package com.phungdung.gnoc2.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RequestInputBO {
    private String countryCode;
    private String code;
    private List<ParameterBo> parmas;
    private String query;
    private int compressData;
}
