package com.quanta.vi.utils;

import com.quanta.vi.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;

@Slf4j
@Component
public class FileUtils {
    @Autowired
    MD5Utils md5Utils;

    /**
     * 获取资源文件路径
     */
    public String getResourcePath() {
        try {
            File directory = new File("src/main/resources");
            return directory.getCanonicalPath() + "/";
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("I/O错误");
        }
    }

    /**
     * 自定义获取资源文件路径
     */
    public String getResourcePath(String path) {
        try {
            File directory = new File(path);
            return directory.getCanonicalPath();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("I/O错误");
        }
    }

    /**
     * 获取一个随机文件名
     */
    public String getRandomFileName() {
        String salt = md5Utils.getSalt();
        return System.currentTimeMillis() + salt;
    }

    /**
     * 删除文件
     * */
    public boolean deleteFile(String path){
        return FileSystemUtils.deleteRecursively(new File(path));
    }
}
