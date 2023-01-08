package com.quanta.vi.controller;

import com.quanta.vi.bean.JsonResponse;
import com.quanta.vi.constants.Roles;
import com.quanta.vi.dto.EditPasswordParam;
import com.quanta.vi.dto.EditUserParam;
import com.quanta.vi.dto.UserConfigParam;
import com.quanta.vi.dto.UserLoginParam;
import com.quanta.vi.interceptor.RequiredPermission;
import com.quanta.vi.service.UserService;
import com.quanta.vi.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/11/21
 */
@RestController
@RequestMapping("/user")
@RequiredPermission({Roles.ROLE_USER})
public class UserController extends BaseController {
    @Autowired
    UserService userService;

    /**
     * [U001]注册
     * POST /user/register
     * 接口ID：50334903
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50334903
     */
    @PostMapping("/register")
    public JsonResponse<Object> register(@RequestBody @Valid UserLoginParam user) {
        userService.register(user);
        return JsonResponse.success();
    }

    /**
     * [U002]登陆
     * POST /user/login
     * 接口ID：50334909
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50334909
     */
    @PostMapping("/login")
    public JsonResponse<Object> login(@RequestBody @Validated UserLoginParam user) {
        String token = userService.login(user);
        Map<String, String> res = new HashMap<>();
        res.put("token", token);
        return JsonResponse.success(res);
    }

    /**
     * [U003]获取用户信息
     * GET /user/information
     * 接口ID：50334936
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50334936
     */
    @GetMapping("/information")
    public JsonResponse<Object> getInformation() {
        UserInfoVo userInfo = userService.getUserInfo(getUid());
        return JsonResponse.success(userInfo);
    }

    /**
     * [U004]修改用户设置
     * PUT /user/information
     * 接口ID：50334950
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50334950
     */
    @PutMapping("/information")
    public JsonResponse<Object> editInformation(@RequestBody @Validated UserConfigParam userConfigParam) {
        userService.editUserConfig(getUid(), userConfigParam);
        return JsonResponse.success();
    }

    /**
     * [U005]修改用户信息
     * PUT /user/information
     * 接口ID：52764687
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-52764687
     */
    @PutMapping("/info")
    public JsonResponse<Object> editInformation(@RequestBody @Validated EditUserParam editUserParam) {
        userService.editUser(getUid(), editUserParam);
        return JsonResponse.success();
    }

    /**
     * [U006]修改密码
     * PUT /user/password
     * 接口ID：50334954
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50334954
     */
    @PutMapping("/password")
    public JsonResponse<Object> password(@RequestBody @Validated EditPasswordParam editPasswordParam) {
        userService.editPassword(getUid(), editPasswordParam);
        return JsonResponse.success();
    }

    /**
     * [U007]退出登陆
     * GET /user/logout
     * 接口ID：50334957
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50334957
     */
    @DeleteMapping("/logout")
    public JsonResponse<Object> logout() {
        userService.logout(getUid());
        return JsonResponse.success();
    }

    /**
     * [U008]验证用户邮箱
     * GET /user/checkVerificationCode
     * 接口ID：50558274
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50558274
     */
    @GetMapping("/checkVerificationCode")
    public JsonResponse<Object> checkVerificationCode(String code) {
        userService.checkEmail(code);
        return JsonResponse.success();
    }


}
