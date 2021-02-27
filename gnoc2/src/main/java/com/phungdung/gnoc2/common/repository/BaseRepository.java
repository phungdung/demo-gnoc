package com.phungdung.gnoc2.common.repository;

import com.phungdung.gnoc2.common.dto.Datatable;
import com.phungdung.gnoc2.common.untils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public abstract class BaseRepository<TDTO, T> {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplateLarge;

  @PersistenceContext(type = PersistenceContextType.EXTENDED)
  private EntityManager entityManager;

  protected JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplateNormal() {
    return namedParameterJdbcTemplate;
  }

  protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    namedParameterJdbcTemplateLarge.getJdbcTemplate().setFetchSize(1000);
    return namedParameterJdbcTemplateLarge;
  }

  protected EntityManager getEntityManager() {
    return entityManager;
  }

  public Datatable getListDataTableBySqlQuery(String sqlQuery, Map<String, Object> parameters
          , int page, int pageSize, Class<?> mappedClass, String sortName, String sortType) {
    log.info("---Start request to search data {} ---");
    Date startTime = new Date();
    Datatable dataReturn = new Datatable();
    String sqlQueryResult = " SELECT * from ( "
            + " SELECT * from ( SELECT a.*, rownum indexRow FROM ( "
            + " SELECT * FROM ( " + sqlQuery + " ) ";
    if (StringUtils.isStringNotNullOrEmtry(sortName)) {
      Field[] fields = mappedClass.getDeclaredFields();
      Map<String, Object> mapField = new HashMap<>();
      for (Field field : fields) {
        mapField.put(field.getName(), field.getName());
      }
      if ("desc".equalsIgnoreCase(sortType)) {
        sqlQueryResult += " ORDER BY " + mapField.get(sortName) + " desc ";
      } else {
        sqlQueryResult += " ORDER BY " + mapField.get(sortName) + " asc ";
      }
    }
    sqlQueryResult += " ) a rownum <((:p_page_number * :p_page_length) + 1 ))" +
            " where indexRow >= (((:p_page_number -1) * :p_page_length) + 1) "
            + " ) T_TABLE_TOTAL ) T_TABLE_NAME_TOTAL ";
    parameters.put("p_page_number", page);
    parameters.put("p_page_length", pageSize);
    List<?> list = getNamedParameterJdbcTemplateNormal().query(sqlQueryResult, parameters, BeanPropertyRowMapper.newInstance(mappedClass));
    int count = 0;
    if (list.isEmpty()) {
      dataReturn.setTotal(count);
    } else {
      try {
        Object object = list.get(0);
        Field field = object.getClass().getSuperclass().getDeclaredField("totalRow");
        field.setAccessible(true);
        count = Integer.parseInt(field.get(object).toString());
        dataReturn.setTotal(count);
      } catch (NoSuchFieldException e1) {
        log.error(e1.getMessage(), e1);
      } catch (IllegalAccessException e2) {
        log.error(e2.getMessage(), e2);
      }
    }
    if (pageSize > 0) {
      if (count % pageSize == 0) {
        dataReturn.setPages(count / pageSize);
      } else {
        dataReturn.setPages((count / pageSize) + 1);
      }
    }
    dataReturn.setData(list);
    log.info("----END SEARCH : TIME " + (new Date().getTime() - startTime.getTime()) + " miliseconds");
    return dataReturn;
  }

  public List<T> findAll(Class<T> tClass) {
    String sqlQuery = " SELECT * FROM " + tClass.getSimpleName() + " t";
    return getEntityManager().createQuery(sqlQuery).getResultList();
  }

  public List<T> findByMultillParam(Class<T> tClass, Object... params) {
    Map<String, Object> mapParams = new HashMap<>();
    String sqlQuery = " SELECT * FROM " + tClass.getSimpleName() + " t WHERE 1=1 ";
    if (params.length > 0) {
      for (int i = 0; i < params.length; i++) {
        if (i % 2 == 0) {
          sqlQuery += " AND t." + params[i] + "=:p_" + params[i] + " ";
          mapParams.put("p_" + params[i], params[i + 1]);
        }
      }
    }
    Query query = entityManager.createQuery(sqlQuery);
    for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }
    return query.getResultList();
  }

  public int deleteByMultillParam(Class<T> tClass, Object... params) {
    Map<String, Object> mapParams = new HashMap<>();
    String sqlQuery = " DELETE FROM " + tClass.getSimpleName() + " t WHERE 1=1 ";
    if (params.length > 0) {
      for (int i = 0; i < params.length; i++) {
        if (i % 2 == 0) {
          sqlQuery += " AND t." + params[i] + "=:p_" + params[i] + " ";
          mapParams.put("p_" + params[i], params[i + 1]);
        }
      }
    }
    Query query = entityManager.createQuery(sqlQuery);
    for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }
    return query.executeUpdate();
  }

  public boolean checkUnique(Class<T> tClass, String uniqueFied, Object uniqueValue, String idField, Long idValue) {
    String sqlQuery = " SELECT t FROM " + tClass.getSimpleName() + " t WHERE 1=1 ";
    if (uniqueValue instanceof String) {
      sqlQuery += " AND lower(t." + uniqueFied + ") = :p_" + uniqueFied + "";
    } else {
      sqlQuery += " AND t." + uniqueFied + " = :p_" + uniqueFied + "";
    }
    sqlQuery += " AND t." + idField + " <> :p_" + idField;
    Query query = entityManager.createQuery(sqlQuery);
    if (uniqueValue instanceof String) {
      query.setParameter("p_" + uniqueFied, uniqueValue.toString());
    } else {
      query.setParameter("p_" + uniqueFied, uniqueValue);
    }
    query.setParameter("p_" + idField, idValue);
    List<T> lst = query.getResultList();
    return lst == null || lst.size() == 0;
  }
}
