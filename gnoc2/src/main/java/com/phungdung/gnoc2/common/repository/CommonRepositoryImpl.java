package com.phungdung.gnoc2.common.repository;

import com.phungdung.gnoc2.common.dto.BaseDTO;
import com.phungdung.gnoc2.common.dto.RequestInputBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class CommonRepositoryImpl extends BaseRepository implements CommonRepository {

    @Override
    public BaseDTO getSQLbyCode(RequestInputBO requestInputBO) {
        try {
            String sql = "select SQL_TEXT sqlQuery FORMART_DATE timeOffset from CFG_WEB_SERVICE_SQL where status =1 and code=:code";
            Map<String, Object> parameters = new HashMap<>();
            List<BaseDTO> lst = getNamedParameterJdbcTemplateNormal().query(sql, parameters, BeanPropertyRowMapper.newInstance(BaseDTO.class));
            if (!lst.isEmpty()) {
                return lst.get(0);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getDataFromSQL(String sql, Map<String, Object> params) {
        try {
            List<Map<String, Object>> lst = getNamedParameterJdbcTemplate().queryForList(sql, params);
            return lst;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
