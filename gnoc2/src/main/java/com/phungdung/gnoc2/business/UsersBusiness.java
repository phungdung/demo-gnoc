package com.phungdung.gnoc2.business;

import com.phungdung.gnoc2.common.dto.Datatable;
import com.phungdung.gnoc2.common.dto.UsersDTO;

public interface UsersBusiness {
    Datatable getAllUsers(UsersDTO usersDTO);
}
