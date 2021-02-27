package com.phungdung.gnoc2.common.business;

import com.phungdung.gnoc2.common.dto.JsonResponseBO;
import com.phungdung.gnoc2.common.dto.RequestInputBO;

public interface CommonBusiness {

    JsonResponseBO getDataJson(RequestInputBO requestInputBO);
}
