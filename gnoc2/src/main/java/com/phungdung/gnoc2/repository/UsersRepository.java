package com.phungdung.gnoc2.repository;

import com.phungdung.gnoc2.common.dto.Datatable;
import com.phungdung.gnoc2.common.dto.UsersDTO;

public interface UsersRepository {
    Datatable getAllUsers(UsersDTO usersDTO);
}
