package se.danielmartensson.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:application.properties")
public class MailConfiguration {
	
    @Value("${configuration.MailConfiguration.host}")
    private String host;
    @Value("${configuration.MailConfiguration.port}")
    private int port;
    @Value("${configuration.MailConfiguration.username}")
    private String username;
    @Value("${configuration.MailConfiguration.password}")
    private String password;
	
	@Bean
	public JavaMailSender getJavaMailSender() {

	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();	    
	    mailSender.setHost(host);
	    mailSender.setPort(port);
	    mailSender.setUsername(username);
	    mailSender.setPassword(password);
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
}