package com.quanta.vi.service;

import com.quanta.vi.dto.EditPasswordParam;
import com.quanta.vi.dto.EditUserParam;
import com.quanta.vi.dto.UserConfigParam;
import com.quanta.vi.dto.UserLoginParam;
import com.quanta.vi.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quanta.vi.vo.UserInfoVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
public interface UserService extends IService<User> {
    // 注册
    public void register(UserLoginParam user);

    // 验证邮箱
    public void checkEmail(String code);

    // 登陆
    public String login(UserLoginParam user);

    // 获取用户数据
    public UserInfoVo getUserInfo(Long uid);

    // 编辑用户配置
    public void editUserConfig(Long uid, UserConfigParam userConfigParam);

    // 编辑用户信息
    public void editUser(Long uid, EditUserParam editUserParam);

    // 退出登陆
    public void logout(Long uid);

    void editPassword(long uid, EditPasswordParam editPasswordParam);
}
