package com.quanta.vi.controller;

import com.quanta.vi.bean.JsonResponse;
import com.quanta.vi.constants.Roles;
import com.quanta.vi.exception.ApiException;
import com.quanta.vi.interceptor.RequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/12/6
 */
@Slf4j
@RestController
@RequestMapping("/common")
@RequiredPermission({Roles.ROLE_USER})
public class CommonController {
    @Value("${file.path}")
    String resourcePath;

    /**
     * [C001]上传图片
     * POST /common/upload
     * 接口ID：53126176
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-53126176
     */
    @PostMapping("/upload")
    public JsonResponse<Object> upload(@RequestParam("files") List<MultipartFile> files) throws Exception {
        if (files == null) {
            throw new ApiException("文件不能为空");
        }

        String targetPath = resourcePath + "head" + "/";
        List<String> fileName = new ArrayList<>();
        files.forEach(e -> {
            if (e == null) {
                throw new ApiException("文件读取错误");
            }
            // 旧文件名
            String olName = e.getOriginalFilename();
            // 提取文件后缀
            String suffix = olName.substring(olName.lastIndexOf("."));
            // 新文件名
            String newName = UUID.randomUUID().toString().replace("-", "") + suffix;
            // 浏览器可访问路径
            String url = "/static/head/" + newName;
            // 访问文件列表
            fileName.add(url);
            try {
                e.transferTo(new File(targetPath, newName));
            } catch (Exception err) {
                log.error(err.getMessage());
                throw new ApiException("文件保存出错");
            }
        });

        return JsonResponse.success(fileName);
    }
}
