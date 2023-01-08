package com.quanta.vi.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/11/21
 */
@Data
public class UserLoginParam {
    @Email(message = "邮箱格式错误")
    private String email;
    @Length(min = 6, max = 50, message = "密码格式错误")
    private String password;
    private String code;
}
