package cn.fzu.edu.furever_home.tools.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSender {
    private final JavaMailSender mailSender;
    private final Environment env;

    public void sendPlain(String to, String subject, String content, String from) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(content);
        String defaultFrom = env.getProperty("spring.mail.username", "");
        String realFrom = (from != null && !from.isEmpty()) ? from : defaultFrom;
        if (realFrom != null && !realFrom.isEmpty()) {
            msg.setFrom(realFrom);
        }
        try {
            mailSender.send(msg);
        } catch (MailException e) {
            throw e;
        } catch (Exception e) {
            throw new MailSendException("邮件发送失败", e);
        }
    }

    public void sendVerificationCode(String to, String code, String from) {
        sendPlain(to, "验证码", "您的验证码是: " + code + "，10分钟内有效。", from);
    }
}