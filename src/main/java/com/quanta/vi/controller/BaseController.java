package com.quanta.vi.controller;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    /**
     * 获取目前用户的id和role
     * @return id和role数组
     */
    private Map<String, Object> getCurrentUser() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        Map<String, Object> result = new HashMap<>();
        result.put("uid", request.getAttribute("uid"));
        result.put("role", request.getAttribute("role"));
        return result;
    }

    /**
     * 获取用户uid
     * @return uid
     */
    public long getUid() {
        return (Long) getCurrentUser().get("uid");
    }

    /**
     * 获取用户role
     * @return role
     * */
    public int getRole(){
        return (int)getCurrentUser().get("role");
    }
}
