package com.phungdung.gnoc2.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterBo {
    private String name;
    private String value;
    private String type;
    private String separator;
    private String format;

    public ParameterBo() {
    }

    public ParameterBo(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public ParameterBo(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public ParameterBo(String name, String value, String type, String separator) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.separator = separator;
    }

    public ParameterBo(String name, String value, String type, String separator, String format) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.separator = separator;
        this.format = format;
    }
}
