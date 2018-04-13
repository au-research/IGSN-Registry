package org.csiro.igsn.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailUtils {
    private MailSender mailSender;

    @Value("#{configProperties['FROM_EMAIL_ADDRESS']}")
    private String FROM_EMAIL_ADDRESS;

    @Value("#{configProperties['EMAIL_SUBJECT']}")
    private String EMAIL_SUBJECT;

    public MailUtils(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:../applicationContext.xml");
        this.mailSender = (MailSender) ctx.getBean("MyMailSender");
    }

    public void sendSuccessEmail(String toEmail, String igsnUrl){
        this.sendMail("IGSN minted successfully", igsnUrl, toEmail, FROM_EMAIL_ADDRESS);
    }

    private void sendMail(String subject, String content, String toAddress, String fromAddress){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(content);
        message.setTo(toAddress);
        message.setFrom(fromAddress);
        this.mailSender.send(message);
    }

}
