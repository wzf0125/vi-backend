package com.quanta.vi.interceptor;

import com.alibaba.fastjson.JSON;
import com.quanta.vi.bean.JsonResponse;
import com.quanta.vi.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author quanta
 * @description 权限拦截器，根据controller上的RequiredPermission注解检查token是否有需要的权限，并在request中把uid和role传给controller
 * @date 2021/9/24
 */
public class AuthInterceptor implements HandlerInterceptor {

    public static final String TOKEN_HEADER = "Token";

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Authorization,Origin,X-Requested-With,Content-Type,Accept,"
                + "content-Type,origin,x-requested-with,content-type,accept,authorization,token,id,X-Custom-Header,X-Cookie,Connection,User-Agent,Cookie,*");
        response.setHeader("Access-Control-Request-Headers", "Authorization,Origin, X-Requested-With,content-Type,Accept");
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // 无token参数拒绝，放通的接口除外
        if (request.getHeader(TOKEN_HEADER) == null || request.getHeader(TOKEN_HEADER).isEmpty()) {
            response.getWriter().write(JSON.toJSONString(JsonResponse.tokenError("缺少token参数")));
            return false;
        }
        String token = request.getHeader(TOKEN_HEADER);
        int role;
        try {
            // ========单独测试用，要删==========
            Logger logger = LoggerFactory.getLogger(getClass());
            String logInfo = "请求路径：" + request.getRequestURL().toString() + "；Token： " + token;
            logger.error("=============测试标记=================");
            logger.error(logInfo);
            logger.error("=============测试标记=================");
            // ====================================
            role = tokenUtils.getTokenRole(token);
        } catch (Exception e) {
            response.getWriter().write(JSON.toJSONString(JsonResponse.tokenError(e.getMessage())));
            return false;
        }
        if (this.hasPermission(handler, role)) {
            Long uid = tokenUtils.getTokenUid(token);
            // 刷新token
            tokenUtils.refreshToken(token, uid);
            // 权限塞request里传给controller
            request.setAttribute("uid", uid);
            request.setAttribute("role", role);
            return true;
        }
        response.getWriter().write(JSON.toJSONString(JsonResponse.permissionError("权限不足")));
        return false;
    }

    private boolean hasPermission(Object handler, int role) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequiredPermission requiredPermission = handlerMethod.getMethod().getAnnotation(RequiredPermission.class);
            if (requiredPermission == null) {
                requiredPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequiredPermission.class);
            }
            if (requiredPermission != null) {
                for (int permission : requiredPermission.value()) {
                    if (role == permission) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

}
