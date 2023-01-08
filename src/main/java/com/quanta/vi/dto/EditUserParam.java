package com.quanta.vi.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/12/6
 */
@Data
public class EditUserParam {
    @Length(min = 4, max = 50, message = "用户名非法")
    private String username;

    @Length(min = 10, max = 255, message = "头像长度非法")
    private String avatar;

}
