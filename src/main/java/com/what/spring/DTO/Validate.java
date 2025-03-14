package com.what.spring.DTO;

import com.what.spring.myInterface.email.Atrribute;
import lombok.Data;

@Data
public class Validate {

    @Atrribute(value = "username")
    private String userName;

    private String email;

    private String verificationUrl;

}
