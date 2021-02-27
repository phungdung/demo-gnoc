package com.phungdung.gnoc2.common.models;

import com.phungdung.gnoc2.common.dto.UsersDTO;
import com.phungdung.gnoc2.common.untils.DateUtils;
import com.phungdung.gnoc2.common.untils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersEntity implements Serializable {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE_KEY.EMPLOYEE)
//    @SequenceGenerator(name = Constant.SEQUENCE_KEY.EMPLOYEE, sequenceName = "SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERS_ID", nullable = false)
    private Long usersId;

    @Column(name = "USERS_NAME")
    private String usersName;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    @Column(name = "UPDATE_USER")
    private String updateUser;

    @Column(name = "PASS_WORD")
    private String passWord;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;


    public UsersDTO toDTO() {
        return new UsersDTO(StringUtils.isStringNotNullOrEmtry(usersId) ? usersId.toString() : null,
                usersName,
                fullName,
                StringUtils.isStringNullOrEmtry(createTime) ? null : DateUtils.converstDateToString(createTime, "dd/MM/yyyy HH:mm:ss"),
                createUser,
                StringUtils.isStringNullOrEmtry(updateTime) ? null : DateUtils.converstDateToString(updateTime, "dd/MM/yyyy HH:mm:ss"),
                updateUser,
                passWord,
                StringUtils.isStringNullOrEmtry(status) ? null : status.toString(),
                email, phoneNumber);
    }
}
