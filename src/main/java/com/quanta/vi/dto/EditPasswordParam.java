package com.quanta.vi.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/12/6
 */
@Data
public class EditPasswordParam {
    @NotNull
    @Length(min = 6, max = 50, message = "密码格式错误")
    String oldPassword;
    @NotNull
    @Length(min = 6, max = 50, message = "密码格式错误")
    String newPassword;
}
