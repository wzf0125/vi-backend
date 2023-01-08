package com.quanta.vi.utils;

import com.quanta.vi.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * @description 发送邮件工具类
 * @author quanta
 * @date 2021/11/9
 */
@Component
public class MailUtils {
    final
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;
    @Value("${host.address}")
    private String hostAddress;

    public MailUtils(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * 发送邮件基础方法
     * @param recipients 收件人数组
     * @param title 邮件标题
     * @param content 邮件正文
     * @param isHtml 是否html格式
     */
    private void sendMail(String[] recipients, String title, String content, boolean isHtml) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMailMessage, true, "utf-8");
            messageHelper.setFrom(mailFrom, "[越南单词本-极速版]");
            messageHelper.setTo(recipients);
            messageHelper.setSubject(title);
            messageHelper.setText(content, isHtml);
            javaMailSender.send(mimeMailMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new ApiException("邮件发送出错！");
        }
    }


    /**
     * 生成邮箱验证码，码长6位，字母与数字混合
     * @return 6位验证码
     */
    public String getVerificationCode() {
        // 增大数字权重，去除部分相似字母
        final String CHARACTERS = "0123456789012345678901234567890123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            sb.append(CHARACTERS.charAt((int) (Math.random() * (CHARACTERS.length()))));
        }
        return sb.toString();
    }

    public void sendVerificationCode(String recipient, String code) {
        String title = "[越南单词本-极速版]邮箱验证";
        String url = hostAddress + "/user/checkVerificationCode?code=" + code;
        String template = "你正在注册越南单词本小程序，请点击以下链接验证邮箱<a href= %s>%s</a>";
        String content = String.format(template, url, url);
        this.sendMail(new String[]{recipient}, title, content, true);
    }
}
