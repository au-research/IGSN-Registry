package org.csiro.igsn.utilities;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailUtils {
    private JavaMailSender mailSender;
    final Logger log = Logger.getLogger(MailUtils.class);
    private String fromEmail;

    public MailUtils(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:../applicationContext.xml");
        this.mailSender = (JavaMailSender) ctx.getBean("MyMailSender");
        this.fromEmail = "services@ands.org.au";
    }

    public void sendMail(String subject, String bodyText, String toAddress) {
        try {

            MimeMessage message = this.mailSender.createMimeMessage();
            message.setSubject(subject);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(this.fromEmail);
            helper.setTo(toAddress);
            helper.setText(bodyText, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            log.error(ex.getMessage());
        }
    }

    public void sendSuccessEmail(String eventType, String toEmail, String igsn){
        String subject = "IGSN " +  StringUtils.capitalize(eventType) + " Successfully " + igsn;
        this.sendMail(subject, getBodyText(igsn),toEmail);
    }

    private String getBodyText(String igsn){
        String handleUrl = "http://hdl.handle.net/";
        String igsnUrl = handleUrl + igsn;
        String AAFLoginUrl = Config.get("AAF_RAPID_URL");
        return "<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01//EN' 'http://www.w3.org/TR/html4/strict.dtd'>\n"
                + "<html>"
                + "<head>"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>IGSN Event Notification email</title></head><body>"
                 + "<p>Your IGSN has been successfully minted through the ANDS IGSN service.</p>"
                 + "<p>If the visibility of the metadata is set to public, it will be viewable at the following URL: <a href='" + igsnUrl + "'>" + igsnUrl + "</a></p>"
                 + "<p>To make changes to your IGSN you will need to <a href='"+AAFLoginUrl+"'>login</a></p>"
                 + "<p>If you have any questions regarding the service, please visit the ANDS <a href='http://www.ands.org.au/online-services/igsn-service'>website</a>"
                 + " or contact <a href='mailto:services@ands.org.au'>services@ands.org.au</a>.</p>"
                 + "<p><b>Australian National Data Service</b><br/>"
                 + "E: <a href='mailto:services@ands.org.au'>services@ands.org.au</a> | W: <a href='http://ands.org.au'>ands.org.au</a></p>"
                 + "<p>Physical address: 9 Liversidge Street, Australian National University, Acton, ACT 2601<br/>"
                 + "Postal address: 101 Liversidge Street, Australian National University, Acton, ACT 2601<br/>"
                 + "<a href='http://www.ands.org.au/about-us/ands-nectar-rds'>Working in partnership with Nectar and RDS</a></p>"
                 + "<a href='http://www.ands.org.au/about-us/ands-nectar-rds'><img src='http://www.ands.org.au/__data/assets/image/0008/1001600/ands-nectar-rds-200px.png'/></a>"
                 + "<p><i>Please consider the environment before printing this e-mail.<i></body></html>";
    }


}
