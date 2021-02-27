package com.phungdung.gnoc2.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class JsonResponseBO implements Serializable {
    private int status;
    private String ReceiverTime;
    private String detailError;
    private String dataJson;
    private byte[] byteDataJson;
    private String sendTime;
    private int tatolDataJson;

    public JsonResponseBO() {
        this.status = 0;
    }
}
