package com.phungdung.gnoc2.controllers;

import com.phungdung.gnoc2.business.UsersBusiness;
import com.phungdung.gnoc2.common.dto.Datatable;
import com.phungdung.gnoc2.common.dto.UsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/users")
public class UsersControllers {
    @Autowired
    protected UsersBusiness usersBusiness;

    @PostMapping(path = "/getAllUsers")
    public ResponseEntity<Datatable> getAllUsers(@RequestBody UsersDTO userDTO) {
        Datatable datatable = usersBusiness.getAllUsers(userDTO);
        return new ResponseEntity<>(datatable, HttpStatus.OK);
    }

}
