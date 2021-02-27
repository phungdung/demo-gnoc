package com.phungdung.gnoc2.common.repository;


import com.phungdung.gnoc2.common.dto.BaseDTO;
import com.phungdung.gnoc2.common.dto.RequestInputBO;

import java.util.List;
import java.util.Map;

public interface CommonRepository {
    BaseDTO getSQLbyCode(RequestInputBO requestInputBO);

    List<Map<String, Object>> getDataFromSQL(String sql, Map<String, Object> params);
}
