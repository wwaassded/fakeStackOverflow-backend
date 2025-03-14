package com.what.spring.DTO;

import com.what.spring.myInterface.email.Atrribute;
import com.what.spring.pojo.user.NameAndPassword;
import lombok.Data;

@Data
public class Validate {

    @Atrribute(value = "username")
    private String userName;

    private String email;

    private String verificationUrl;

    public void init(NameAndPassword nameAndPassword, String url) {
        userName = nameAndPassword.getName();
        email = nameAndPassword.getEmail();
        verificationUrl = url;
    }

}
