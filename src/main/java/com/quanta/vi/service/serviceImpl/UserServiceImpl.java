package com.quanta.vi.service.serviceImpl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.quanta.vi.constants.CachePrefix;
import com.quanta.vi.constants.Roles;
import com.quanta.vi.constants.UserDefaultData;
import com.quanta.vi.dto.EditPasswordParam;
import com.quanta.vi.dto.EditUserParam;
import com.quanta.vi.dto.UserConfigParam;
import com.quanta.vi.dto.UserLoginParam;
import com.quanta.vi.entity.Statistics;
import com.quanta.vi.entity.User;
import com.quanta.vi.entity.UserConfig;
import com.quanta.vi.exception.ApiException;
import com.quanta.vi.mapper.StatisticsMapper;
import com.quanta.vi.mapper.UserConfigMapper;
import com.quanta.vi.mapper.UserMapper;
import com.quanta.vi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quanta.vi.utils.MD5Utils;
import com.quanta.vi.utils.MailUtils;
import com.quanta.vi.utils.RedisUtils;
import com.quanta.vi.utils.TokenUtils;
import com.quanta.vi.vo.UserInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MailUtils mailUtils;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    UserConfigMapper userConfigMapper;
    @Autowired
    TokenUtils tokenUtils;
    @Autowired
    WxMaService wxMaService;
    @Autowired
    StatisticsMapper statisticsMapper;

    /**
     * 注册
     */
    @Override
    @Transactional
    public void register(UserLoginParam user) {
        if (user.getEmail() == null || user.getPassword() == null || user.getEmail().equals("") || user.getPassword().equals("")) {
            throw new ApiException("邮箱或密码不能为空");
        }
        // 先验证数据库中是否有该邮箱
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", user.getEmail());
        Long c = userMapper.selectCount(wrapper);
        if (c != 0) {
            throw new ApiException("邮箱已存在请使用其他邮箱注册");
        }
        // 加密密码
        String salt = MD5Utils.getSalt();
        String encodePwd = MD5Utils.md5(user.getPassword(), salt);

        // 构建用户数据
        User newUser = new User(encodePwd, salt, user.getEmail());
        int insert = userMapper.insert(newUser);
        if (insert != 1) {
            throw new ApiException("注册失败");
        }
        // 设置默认配置
        UserConfig userConfig = new UserConfig();
        userConfig.setUserId(newUser.getId());
        userConfig.setBookId(1L);
        userConfigMapper.insert(userConfig);

        // 选择默认词书
        Statistics statistics = new Statistics(newUser.getId(), 1L);
        statisticsMapper.insert(statistics);

        // 生成邮箱验证码
        String verificationCode = UUID.randomUUID().toString().replace("-", "");
        // 发送邮件
        mailUtils.sendVerificationCode(user.getEmail(), verificationCode);

        // 放入邮箱验证码
        redisUtils.set(String.format(CachePrefix.VERIFICATION_CODE_PREFIX, verificationCode), newUser.getId());
    }

    /**
     * 验证邮箱
     */
    @Override
    public void checkEmail(String code) {
        Long uid = (Long) redisUtils.get(String.format(CachePrefix.VERIFICATION_CODE_PREFIX, code));
        if (uid == null) {
            throw new ApiException("验证码不存在");
        }
        User user = new User();
        user.setId(uid);
        // 设置邮箱验证状态为通过
        user.setIsValid(UserDefaultData.validCode);
        userMapper.updateById(user);
    }

    /**
     * 登陆
     */
    @Override
    public String login(UserLoginParam userParam) {
        // 微信登陆
        if (userParam.getCode() != null) {
            return loginByWeChat(userParam.getCode());
        }

        // 非微信登陆
        if (userParam.getEmail() == null || userParam.getPassword() == null) {
            throw new ApiException("邮箱或密码不能为空");
        }
        String email = userParam.getEmail();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new ApiException("用户不存在");
        }
        if (!user.getIsValid().equals(UserDefaultData.validCode)) {
            throw new ApiException("请先验证邮箱");
        }
        String pwd = MD5Utils.md5(userParam.getPassword(), user.getSalt());
        if (!pwd.equals(user.getPassword())) {
            throw new ApiException("密码错误");
        }

        return tokenUtils.grantToken(user.getId(), Roles.ROLE_USER);
    }

    @Override
    public UserInfoVo getUserInfo(Long uid) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", uid);
        userQueryWrapper.select("username", "avatar", "email", "is_wx_user");
        User user = userMapper.selectOne(userQueryWrapper);

        QueryWrapper<UserConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", uid);
        wrapper.select("group_size", "first_type", "second_type", "third_type", "timing_duration","book_id");
        UserConfig userConfig = userConfigMapper.selectOne(wrapper);
        return new UserInfoVo(user, userConfig);
    }

    // 编辑用户配置
    @Override
    public void editUserConfig(Long uid, UserConfigParam userConfigParam) {
        UserConfig userConfig = new UserConfig();
        BeanUtils.copyProperties(userConfigParam, userConfig);
        userConfig.setUserId(uid);
        UpdateWrapper<UserConfig> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", uid);
        int row = userConfigMapper.update(userConfig, wrapper);
        if (row != 1) {
            throw new ApiException("修改失败");
        }
    }

    @Override
    public void editUser(Long uid, EditUserParam editUserParam) {
        if (editUserParam.getUsername() == null && editUserParam.getAvatar() == null) {
            throw new ApiException("参数错误");
        }
        userMapper.updateById(new User(uid, editUserParam.getUsername(), editUserParam.getAvatar()));
    }

    // 退出登陆
    @Override
    public void logout(Long uid) {
        tokenUtils.safeExit(uid);
    }

    @Override
    public void editPassword(long uid, EditPasswordParam editPasswordParam) {
        User user = userMapper.selectById(uid);
        if (user == null) {
            throw new ApiException("用户不存在");
        }
        // 判断旧密码是否匹配
        if (!MD5Utils.md5(editPasswordParam.getOldPassword(), user.getSalt()).equals(user.getPassword())) {
            throw new ApiException("原密码错误");
        }

        User user1 = new User();
        user1.setId(uid);
        // 设置新盐值
        user1.setSalt(MD5Utils.getSalt());
        // 设置新密码
        user1.setPassword(MD5Utils.md5(editPasswordParam.getNewPassword(), user1.getSalt()));
        // 更新密码
        userMapper.updateById(user1);
    }

    // 微信登陆
    public String loginByWeChat(String code) {
        String userOpenid;
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            userOpenid = session.getOpenid();
        } catch (Exception e) {
            throw new ApiException("微信服务器错误");
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("open_id", userOpenid);
        User user = userMapper.selectOne(wrapper);

        // 新用户
        if (user == null) {
            // 插入新用户
            user = new User(userOpenid);
            userMapper.insert(user);
            // 设置默认配置
            UserConfig userConfig = new UserConfig();
            userConfig.setUserId(user.getId());
            userConfig.setBookId(1L);
            userConfigMapper.insert(userConfig);

            // 选择默认词书
            Statistics statistics = new Statistics(user.getId(), 1L);
            statisticsMapper.insert(statistics);
        }
        Long uid = user.getId();
        return tokenUtils.grantToken(uid, Roles.ROLE_USER);
    }
}
