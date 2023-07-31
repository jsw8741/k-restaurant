package com.krestaurant.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.mail.javamail.*;

// 이메일 전송을 위한 JavaMailSender를 설정하는 클래스
@Configuration
@PropertySource("classpath:email.properties") // 클래스패스에 있는 email.properties 파일에서 프로퍼티 값을 가져온다~
public class EmailConfig {

	// 프로퍼티 값들 필드에 주입받기
	@Value("${mail.smtp.port}")
	private int port;
	@Value("${mail.smtp.socketFactory.port}")
	private int socketPort;
	@Value("${mail.smtp.auth}")
	private boolean auth;
	@Value("${mail.smtp.starttls.enable}")
	private boolean starttls;
	@Value("${mail.smtp.starttls.required}")
	private boolean startlls_required;
	@Value("${mail.smtp.socketFactory.fallback}")
	private boolean fallback;
	@Value("${AdminMail.id}")
	private String id;
	@Value("${AdminMail.password}")
	private String password;
	
	// JavaMailSender의 구현부인 JavaMailSenderImpl을 빈으로 등록하고 설정값을 주입
	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost("smtp.naver.com");
		javaMailSender.setUsername(id);
		javaMailSender.setPassword(password);
		javaMailSender.setPort(port);
		javaMailSender.setJavaMailProperties(getMailProperties());
		javaMailSender.setDefaultEncoding("UTF-8");
		return javaMailSender;
	}

	// 이메일 속성을 설정하는 Properties 객체를 반환
	private Properties getMailProperties() {
		Properties pt = new Properties();
		pt.put("mail.smtp.socketFactory.port", socketPort);
		pt.put("mail.smtp.auth", auth);
		pt.put("mail.smtp.starttls.enable", starttls);
		pt.put("mail.smtp.starttls.required", startlls_required);
		pt.put("mail.smtp.socketFactory.fallback", fallback);
		pt.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		return pt;
	}
}