package ru.itis.myscore.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class EmailUtil {

    private final JavaMailSender mailSender;

    private final FreeMarkerConfigurer freemarkerConfigurer;

    private final String CONFIRM_TEMPLATE = "confirm_mail.ftlh";

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String subject, Map<String, String> data) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setSubject(subject);
            messageHelper.setTo(data.get("email"));
            messageHelper.setFrom(from);

            Configuration configuration = freemarkerConfigurer.createConfiguration();
            configuration.setClassForTemplateLoading(this.getClass(), "/mails");
            Template t = configuration.getTemplate(CONFIRM_TEMPLATE);
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, data);

            messageHelper.setText(text, true);
        };

        mailSender.send(preparator);
    }
}
