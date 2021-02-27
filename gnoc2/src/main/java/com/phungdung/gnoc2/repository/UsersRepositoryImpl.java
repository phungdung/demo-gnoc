package com.phungdung.gnoc2.repository;

import com.phungdung.gnoc2.common.repository.BaseRepository;
import com.phungdung.gnoc2.common.dto.Datatable;
import com.phungdung.gnoc2.common.dto.UsersDTO;
import com.phungdung.gnoc2.common.untils.StringUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Log4j
public class UsersRepositoryImpl extends BaseRepository implements UsersRepository {
    @Override
    public Datatable getAllUsers(UsersDTO usersDTO) {
        Map<String, Object> parameters = new HashMap<>();
        String sql = " SELECT " +
                "    users_id       usersid, " +
                "    users_name     usersname, " +
                "    pass_word      fullname, " +
                "    full_name      fullname, " +
                "    phone_number   phonenumber, " +
                "    status         status, " +
                "    to_char(create_time, 'dd/MM/yyyy HH24:mi:ss') createtime, " +
                "    create_user    createuser, " +
                "    to_char(update_time, 'dd/MM/yyyy HH24:mi:ss') updatetime, " +
                "    update_user    updateuser, " +
                "    email          email " +
                "FROM " +
                "    users " +
                "WHERE " +
                "    1 = 1 ";
        if (usersDTO != null) {
            if (StringUtils.isStringNotNullOrEmtry(usersDTO.getUsersName())) {
                sql += "  AND lower(users_name) LIKE ('%' || lower(:username) ||'%') ";
                parameters.put("usersName", usersDTO.getUsersName());
            }
        }
        return getListDataTableBySqlQuery(sql, parameters, usersDTO.getPage(), usersDTO.getPageSize(), UsersDTO.class, usersDTO.getSortType(), usersDTO.getSortName());
    }
}
