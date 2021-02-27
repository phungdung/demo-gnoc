package com.phungdung.gnoc2.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UsersDTO extends BaseDTO {
    private String usersId;
    private String usersName;
    private String fullName;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;
    private String passWord;
    private String status;
    private String email;
    private String phoneNumber;

    public UsersDTO(String usersId, String usersName, String fullName, String createTime, String createUser, String updateTime, String updateUser, String passWord, String status, String email, String phoneNumber) {
        this.usersId = usersId;
        this.usersName = usersName;
        this.fullName = fullName;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.passWord = passWord;
        this.status = status;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
