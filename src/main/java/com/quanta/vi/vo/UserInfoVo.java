package com.quanta.vi.vo;

import com.quanta.vi.entity.User;
import com.quanta.vi.entity.UserConfig;
import lombok.Data;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/11/23
 */
@Data
public class UserInfoVo {
    User user;
    UserConfig userConfig;

    public UserInfoVo() {
    }

    public UserInfoVo(User user, UserConfig userConfig) {
        this.user = user;
        this.userConfig = userConfig;
    }
}
