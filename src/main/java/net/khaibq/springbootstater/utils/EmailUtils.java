package net.khaibq.springbootstater.utils;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.khaibq.springbootstater.dto.email.EmailDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.concurrent.CompletableFuture;

@Component
@AllArgsConstructor
@Slf4j
public class EmailUtils {
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Async
    public CompletableFuture<Boolean> sendEmail(EmailDto emailDto) {
        log.info("sendEmail:  {}", emailDto);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(emailDto.getEmailTo());
            if (emailDto.getEmailCC() != null && emailDto.getEmailCC().length > 0) {
                helper.setCc(emailDto.getEmailCC());
            }
            if (emailDto.getEmailBCC() != null && emailDto.getEmailBCC().length > 0) {
                helper.setBcc(emailDto.getEmailBCC());
            }
            helper.setSubject(emailDto.getSubject());

            Context context = new Context();
            if (emailDto.getParams() != null) {
                emailDto.getParams().keySet().forEach(key -> context.setVariable(key, emailDto.getParams().get(key)));
            }

            if (StringUtils.isNotBlank(emailDto.getTemplate())) {
                String html = templateEngine.process(emailDto.getTemplate(), context);
                helper.setText(html, true);
            } else {
                String text = String.format(emailDto.getContent(), emailDto.getParams());
                helper.setText(text, false);
            }

            if (emailDto.getFiles() != null && emailDto.getFiles().length > 0) {
                for (int i = 0; i < emailDto.getFiles().length; i++) {
                    helper.addAttachment(emailDto.getFileNames()[i], emailDto.getFiles()[i]);
                }
            }

            mailSender.send(message);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.error("Exception, {}",e.getCause() != null ? e.getCause() : e.getMessage());
            return CompletableFuture.completedFuture(false);
        }
    }
}
