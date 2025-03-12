package com.what.spring.pojo.user;

import com.what.spring.util.Utils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NameAndPassword {
    @Size(min = 4, max = 32, message = "用户名称的长度只能是4~32")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @Size(min = 8, max = 16, message = "用户名称的长度只能是8~16")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Email(message = "非法的邮箱格式")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    public void getMd5() {
        this.password = Utils.md5(this.password);
    }
}
