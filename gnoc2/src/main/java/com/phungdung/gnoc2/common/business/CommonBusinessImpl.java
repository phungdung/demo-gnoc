package com.phungdung.gnoc2.common.business;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.phungdung.gnoc2.common.repository.CommonRepository;
import com.phungdung.gnoc2.common.dto.BaseDTO;
import com.phungdung.gnoc2.common.dto.JsonResponseBO;
import com.phungdung.gnoc2.common.dto.ParameterBo;
import com.phungdung.gnoc2.common.dto.RequestInputBO;
import com.phungdung.gnoc2.common.untils.DateUtils;
import com.phungdung.gnoc2.common.untils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@Transactional
public class CommonBusinessImpl implements CommonBusiness {

    @Autowired
    protected CommonRepository commonRepository;

    @Override
    public JsonResponseBO getDataJson(RequestInputBO requestInputBO) {
        JsonResponseBO response = new JsonResponseBO();
        try {
            response.setReceiverTime(DateUtils.getSysDateTime());
            response.setStatus(1);
            if (StringUtils.isStringNotNullOrEmtry(requestInputBO.getCode())) {
                response.setDetailError("SQL code is null");
                return response;
            }
            BaseDTO baseDTO = commonRepository.getSQLbyCode(requestInputBO);
            if (baseDTO != null && StringUtils.isStringNullOrEmtry(baseDTO.getSqlQuery())) {
                response.setDetailError("SQL code disable or not exist");
            }
            String content = createDataJson(response, requestInputBO, baseDTO.getSqlQuery(), baseDTO.getTimeOffset());
            if (requestInputBO.getCompressData() == 1) {
                response.setDataJson(content);
            } else {
                response.setDataJson(content);
            }
            response.setSendTime(DateUtils.getSysDateTime());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setDetailError(e.toString());
        } finally {
            log.info("[" + requestInputBO.getCode() + "]");
        }
        return response;
    }

    private String createDataJson(JsonResponseBO response, RequestInputBO requestInputBO, String sqlQuery, String dateFormart) throws Exception {
        Map<String, Object> mapParam = new HashMap<>();
        if (requestInputBO.getParmas() != null && !requestInputBO.getParmas().isEmpty()) {
            for (ParameterBo bo : requestInputBO.getParmas()) {
                if (StringUtils.isStringNullOrEmtry(bo.getValue())) {
                    mapParam.put(bo.getName(), null);
                } else {
                    if (StringUtils.isStringNotNullOrEmtry(bo.getSeparator())) {
                        String[] tmps = bo.getValue().split(bo.getSeparator());
                        if (tmps.length > 0) {
                            List<Object> lst = new ArrayList<>();
                            for (String value : tmps) {
                                if (StringUtils.isStringNullOrEmtry(bo.getType()) && "NUMBER".equalsIgnoreCase(bo.getType())) {
                                    if (value.matches("-?\\d+\\.?\\d*")) {
                                        if (value.contains(".")) {
                                            lst.add(Double.parseDouble(value.trim()));
                                        } else {
                                            lst.add(Long.parseLong(value.trim()));
                                        }
                                    }
                                } else if (StringUtils.isStringNotNullOrEmtry(bo.getType()) && "DATE".equalsIgnoreCase(bo.getType())) {
                                    if (StringUtils.isStringNullOrEmtry(bo.getFormat())) {
                                        lst.add(DateUtils.converstStringToDate(value.trim(), bo.getFormat()));
                                    } else {
                                        lst.add(new Date(Long.valueOf(value.trim())));
                                    }
                                } else {
                                    lst.add(value.trim());
                                }
                            }
                            mapParam.put(bo.getName(), lst);
                        }
                    } else {
                        String value = bo.getValue();
                        if (StringUtils.isStringNullOrEmtry(bo.getType()) && "NUMBER".equalsIgnoreCase(bo.getType())) {
                            if (value.matches("-?\\d+\\.?\\d*")) {
                                if (value.contains(".")) {
                                    mapParam.put(bo.getName(), Double.parseDouble(value.trim()));
                                } else {
                                    mapParam.put(bo.getName(), Long.parseLong(value.trim()));
                                }
                            }
                        } else if (StringUtils.isStringNotNullOrEmtry(bo.getType()) && "DATE".equalsIgnoreCase(bo.getType())) {
                            if (StringUtils.isStringNullOrEmtry(bo.getFormat())) {
                                mapParam.put(bo.getName(), DateUtils.converstStringToDate(value.trim(), bo.getFormat()));
                            } else {
                                mapParam.put(bo.getName(), new Date(Long.valueOf(value.trim())));
                            }
                        } else {
                            mapParam.put(bo.getName(), value.trim());
                        }
                    }
                }
            }
        }
        log.info("[" + requestInputBO.getCode() + "] mapParam: " + mapParam);
        log.info("[" + requestInputBO.getCode() + "] excuteQuery ...");
        List<Map<String, Object>> lst = commonRepository.getDataFromSQL(sqlQuery, mapParam);
        log.info("[" + requestInputBO.getCode() + "] output size " + lst.size());
        response.setTatolDataJson(lst.size());
        log.info("[" + requestInputBO.getCode() + "] conver json ...");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if (dateFormart != null && !dateFormart.trim().isEmpty()) {
            simpleDateFormat = new SimpleDateFormat(dateFormart);
        }
        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(sw);
        jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
        jsonGenerator.writeStartObject();
        jsonGenerator.writeArrayFieldStart("data");
        for (Map<String, Object> row : lst) {
            jsonGenerator.writeStartObject();
            for (String key : row.keySet()) {
                String column = key.toLowerCase();
                if (row.get(key) != null) {
                    Object value = row.get(key);
                    try {
                        if (value instanceof String) {
                            jsonGenerator.writeStringField(column, value.toString());
                        } else if (value instanceof Timestamp) {
                            Timestamp time = (Timestamp) value;
                            Date date = new Date(time.getTime());
                            jsonGenerator.writeStringField(column, simpleDateFormat.format(date));
                        } else if (value instanceof java.sql.Date) {
                            Date date = new Date(((Date) value).getTime());
                            jsonGenerator.writeStringField(column, simpleDateFormat.format(date));
                        } else {
                            if (value != null && value.toString().matches("-?\\d+\\.?\\d*")) {
                                if (value.toString().contains(".")) {
                                    jsonGenerator.writeNumberField(column, Double.parseDouble(value.toString()));
                                } else {
                                    jsonGenerator.writeNumberField(column, Long.parseLong(value.toString()));
                                }
                            }
                        }
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                        throw new Exception("Error json write value " + value + ". " + ex.toString());
                    }
                }
            }
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();
        jsonGenerator.close();

        String content = sw.getBuffer().toString();
        sw.close();
        log.info("[" + requestInputBO.getCode() + "] Convert json success: OK");
        return content;
    }
}
