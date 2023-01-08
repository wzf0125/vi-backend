package com.quanta.vi.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.quanta.vi.constants.UserDefaultData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
@Getter
@Setter
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id（用户）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * openid
     */
    @TableField("open_id")
    private String openId;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 加密盐值
     */
    @TableField("salt")
    private String salt;

    /**
     * 头像路径
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 是否是微信登陆
     */
    @TableField("is_wx_user")
    private Integer isWxUser;

    /**
     * 是否已经通过邮箱验证
     */
    @TableField("is_valid")
    private Integer isValid;

    /**
     * 逻辑删除（1表示删除，0表示未删除）
     */
    @TableLogic
    private Integer isDeleted;

    public User() {
    }

    public User(String password, String salt, String email) {
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.username = UserDefaultData.defaultUsername;
        this.avatar = UserDefaultData.defaultAvatar;
        this.isValid = UserDefaultData.defaultValidCode;
        this.isWxUser = UserDefaultData.defaultIsWxUserCode;
    }

    public User(String openId) {
        this.openId = openId;
        this.username = UserDefaultData.defaultUsername;
        this.avatar = UserDefaultData.defaultAvatar;
        this.isValid = UserDefaultData.defaultValidCode;
        this.isWxUser = UserDefaultData.wxUserCode;
    }

    public User(Long id, String username, String avatar) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
    }
}
