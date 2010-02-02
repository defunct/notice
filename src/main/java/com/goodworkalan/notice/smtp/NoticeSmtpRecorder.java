package com.goodworkalan.notice.smtp;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import com.goodworkalan.madlib.VariableProperties;
import com.goodworkalan.notice.Recorder;
import com.goodworkalan.waste.FreemarkerGenerator;
import com.goodworkalan.waste.SessionBuilder;
import com.goodworkalan.waste.TextBodyPartBuilder;
import com.goodworkalan.waste.VerpMessage;
import com.goodworkalan.waste.VerpMessageBuilder;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * With a filter decorator, this could be general purpose. Maybe a common
 * language for matching events?
 *
 * @author Alan Gutierrez
 */
public class NoticeSmtpRecorder implements Recorder {
    private String user;
    
    private String password;

    private final Properties properties = new Properties();
    
    public void initialize(String prefix, VariableProperties configuration) {
        for (String property : configuration.getProperty(prefix + "properties", "").split(",")) {
            properties.put(property, configuration.getProperty(prefix + property));
        }
        user = configuration.getProperty(prefix + "user");
        password = configuration.getProperty(prefix + "password");
    }
    
    public void record(Map<String, Object> map) {
        SessionBuilder newSession = new SessionBuilder();
        
        newSession.setUser(user);
        newSession.setPassword(password);
        newSession.getProperties().putAll(properties);
        
        VerpMessageBuilder verper = new VerpMessageBuilder(newSession, "receipt");

        final VerpMessage verp = verper.email("alan@blogometer.com");

        verp.getSession().setDebug(true);
        try {
            verp.getMimeMessage().setFrom(new InternetAddress("Alan Gutierrez <wrinkledog.alerts@gmail.com>"));
            verp.getMimeMessage().addRecipients(Message.RecipientType.TO, "alan@blogometer.com");
            verp.getMimeMessage().setSubject("[Exception] " + map.get("message"));
    
            Configuration configuration = new Configuration();
            configuration.setClassForTemplateLoading(getClass(), "/META-INF/templates");
            Template template = configuration.getTemplate("exception.ftl");
            TextBodyPartBuilder builder = new TextBodyPartBuilder(new FreemarkerGenerator(template));
    
            builder.newBodyPart(verp.getMimeMessage(), map);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        try {
            verp.send();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void flush() {
    }
    
    public void close() {
    }
}
