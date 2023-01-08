package com.quanta.vi.exception;

import com.alibaba.fastjson.JSON;
import com.quanta.vi.bean.JsonResponse;
import com.quanta.vi.constants.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author quanta
 * @date 2021/12/5
 */
@Slf4j
@Order(0)
@ControllerAdvice(basePackages = "com.quanta.vi.controller")
public class GlobalExceptionHandler {

    private static final String ERROR_MESSAGE_TEMPLATE = "%s\n[Msg]%s\n[File]%s\n[LogId]%s\n[Url]%s\n[Ip]%s\n[Args]%s\n[Token]%s";

    @Value("${project.isDebug}")
    private boolean isDebug;

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    JsonResponse<Object> errorResult(Exception e) throws IOException {
        log.error(e.getMessage());
        if (isDebug) { // 本地调试只输出错误信息
            e.printStackTrace();
            // 本地调试返回错误内容
            return JsonResponse.error(e.getMessage());
        }
        // 线上环境仅返回服务器错误
        return JsonResponse.error(ResultCode.SERVER_ERROR.getMsg());
    }

    // 数据校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody JsonResponse<Object> validationErrorResult(MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();
        StringBuffer sb = new StringBuffer();
        if (result.getFieldErrorCount() > 0) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
               sb.append(fieldError.getDefaultMessage());
            }
        }
        return JsonResponse.paramError(String.format("参数校验错误[%s]", sb));
    }

    // 数据校验异常
    @ExceptionHandler(ValidationException.class)
    public @ResponseBody JsonResponse<Object> validationErrorResult(ValidationException e){
        return JsonResponse.paramError(String.format("参数校验错误[%s]", e.getMessage()));
    }

    // 自定义异常处理类
    @ExceptionHandler(ApiException.class)
    public @ResponseBody
    JsonResponse<Object> apiErrorResult(ApiException e) {
        return JsonResponse.fail(e.getMessage());
    }

    private String createErrorMessage(Exception e) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String logId = (String) request.getAttribute("requestId");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String currentDate = formatter.format(date);
        String msg = String.format(ERROR_MESSAGE_TEMPLATE,
                currentDate,
                e.getMessage(),
                e.getStackTrace()[0].getFileName() + "." + e.getStackTrace()[0].getMethodName() + " (line: " + e.getStackTrace()[0].getLineNumber() + ")",
                logId,
                request.getRequestURL().toString(),
                request.getHeader("X-Real-IP"),
                request.getAttribute("args"),
                request.getHeader("Token"));
        Map<String, Object> req = new HashMap<>();
        req.put("msgtype", "text");
        Map<String, String> content = new HashMap<>();
        content.put("content", msg);
        req.put("text", content);
        return JSON.toJSONString(req);
    }
}
