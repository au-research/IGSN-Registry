package org.csiro.igsn.utilities;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

@Service
public class MailUtils {
    private JavaMailSender mailSender;
    final Logger log = Logger.getLogger(MailUtils.class);
    private String fromEmail;
    private String AAFLoginUrl;
    private String baseUrl;

    public MailUtils(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:../applicationContext.xml");
        this.mailSender = (JavaMailSender) ctx.getBean("MyMailSender");
        this.fromEmail = Config.get("FROM_EMAIL_ADDRESS");
        this.baseUrl = Config.get("BASEURL_VALUE");
        this.AAFLoginUrl = Config.get("AAF_RAPID_URL");
    }

    public void sendMail(String subject, String bodyText, String toAddress) {
        try {

            MimeMessage message = this.mailSender.createMimeMessage();
            message.setSubject(subject);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setTo(toAddress);
            helper.setText(bodyText, true);
            ClassLoader cl = this.getClass().getClassLoader();
            try {
                helper.addInline("ardc_logo.png", new File(cl.getResource("images/ardc_logo.png").getFile()));
            }catch(NullPointerException e){
                log.error("ardc_logo not found");
            }
            try {
                helper.addInline("youtube.gif", new File(cl.getResource("images/youtube.gif").getFile()));
            }catch(NullPointerException e){
                log.error("youtube_logo not found");
            }
            try {
                helper.addInline("twitter.gif", new File(cl.getResource("images/twitter.gif").getFile()));
            }catch(NullPointerException e){
                log.error("twitter_logo not found");
            }
            try {
                helper.addInline("ncris_logo.jpg", new File(cl.getResource("images/ncris_logo.jpg").getFile()));
            }catch(NullPointerException e){
                log.error("ncris_logo not found");
            }
            helper.setFrom(this.fromEmail);


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
        return "<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01//EN' 'http://www.w3.org/TR/html4/strict.dtd'>\n"
                + "<html>"
                + "<head>"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>IGSN Event Notification email</title></head><body>"
                 + "<p>Your IGSN has been successfully minted through the ARDC IGSN service.</p>"
                 + "<p>If the visibility of the metadata is set to public, it will be viewable at the following URL: <a href='" + igsnUrl + "'>" + igsnUrl + "</a></p>"
                 + "<p>To make changes to your IGSN you will need to <a href='"+this.AAFLoginUrl+"'>login</a></p>"
                 + "<p>If you have any questions regarding the service, please visit the ARDC <a href='https://ardc.edu.au/services/identifier/igsn/'>website</a>"
                 + " or contact <a href='mailto:services@ardc.edu.au'>services@ardc.edu.au</a>.</p>"
                 + "<p><b>Australian Research Data Commons (ARDC)</b><br/>"
                 + "E: <a href='mailto:services@ardc.edu.au'>services@ardc.edu.au</a> | W: <a href='https://ardc.edu.au'>ardc.edu.au</a></p>"
                 + "<p>Physical address: 9 Liversidge Street, Australian National University, Acton, ACT 2601<br/>"
                 + "Postal address: 101 Liversidge Street, Australian National University, Acton, ACT 2601<br/>"
                 + "<a href='https://ardc.edu.au'>ardc.edu.au</a></p>"
                 + "<a href='https://ardc.edu.au/about/'><img src='cid:ardc_logo.png' alt='ardc logo'/></a><br/><br/>"
                 + "<a href='https://www.ncris-network.org.au/'><img src='cid:ncris_logo.jpg' alt='ncris logo'/></a>"
                 + "<p><a href='https://twitter.com/ARDC_AU'><img src='cid:twitter.gif' alt='twitter logo'/></a>&nbsp;&nbsp;&nbsp;"
                 + "<a href='https://www.youtube.com/c/ARDC_AU'><img src='cid:youtube.gif' alt='youtube logo'/></a></p>"
                 + "<p><i>ARDC acknowledges the Traditional Owners of the lands<br/>"
                 + "that we live and work on across Australia and pays its respect<br/>"
                 + "to Elders past and present.<i></p>"
                 + "<p><i>Please consider the environment before printing this e-mail.<i></p></body></html>";
    }


}
