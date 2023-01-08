package com.quanta.vi.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description MD5加密工具类
 * @author gdufs
 * @date 2021/11/28
 */
@Slf4j
@Component
public class MD5Utils {

    // 私有化构造函数，防止new对象
    private MD5Utils(){
    }

    /**
     * MD5加密基础方法
     * @param string 需要加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");

            byte[] bytes = md.digest(string.getBytes());

            return DigestUtils.md5DigestAsHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5加密基础方法
     * @param string 需要加密的字符串
     * @param salt 加密盐值
     * @return 加密后的字符串
     */
    public static String md5(String string, String salt) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(string).append(salt);
            MessageDigest md = MessageDigest.getInstance("md5");

            byte[] bytes = md.digest(sb.toString().getBytes());

            return DigestUtils.md5DigestAsHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成随机的盐值
     * */
    public static String getSalt(){
        String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<10;i++){
            int temp = (int) (Math.random()*(str.length()));
            sb.append(str.charAt(temp));
        }
        return sb.toString();

        }

}
