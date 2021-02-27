package com.phungdung.gnoc2.business;

import com.phungdung.gnoc2.repository.UsersRepository;
import com.phungdung.gnoc2.common.dto.Datatable;
import com.phungdung.gnoc2.common.dto.UsersDTO;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Log4j
public class UsersBusinessImpl implements UsersBusiness {

    @Autowired
    protected UsersRepository usersRepository;

    @Override
    public Datatable getAllUsers(UsersDTO usersDTO) {
        log.info("log info getAllUsers");
        return usersRepository.getAllUsers(usersDTO);
    }
}
