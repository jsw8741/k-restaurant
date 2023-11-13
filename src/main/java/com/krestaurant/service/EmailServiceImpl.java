package com.krestaurant.service;

import java.util.UUID;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl  implements EmailService{

	private final JavaMailSender emailSender;
	    
	    public static String createTempPassword() {
			// 랜덤 UUID 생성
			UUID uuid = UUID.randomUUID();

			// UUID를 문자열로 바꾸고 - 제거, 앞 8자리만 뽑기
			String str = uuid.toString().replace("-", "");
			return str.substring(0, 8);
		}
	 
	    private MimeMessage createMessage(String to, String tempPassword)throws Exception{
	        MimeMessage  message = emailSender.createMimeMessage();
	 
	        message.addRecipients(RecipientType.TO, to);//보내는 대상
	        message.setSubject("이메일 인증 테스트");//제목
	 
	        String msgg="";
	        msgg+= "<div style='margin:20px;'>";
	        msgg+= "<h1> 안녕하세요 k-restaurant 입니다. </h1>";
	        msgg+= "<br>";
	        msgg+= "<p>아래 코드를 복사해 입력해주세요<p>";
	        msgg+= "<br>";
	        msgg+= "<p>감사합니다.<p>";
	        msgg+= "<br>";
	        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
	        msgg+= "<h3 style='color:blue;'>임시 비밀번호입니다.</h3>";
	        msgg+= "<div style='font-size:130%'>";
	        msgg+= "CODE : <strong>";
	        msgg+= tempPassword+"</strong><div><br/> ";
	        msgg+= "</div>";
	        message.setText(msgg, "utf-8", "html");//내용
	        message.setFrom(new InternetAddress("jsw8741@naver.com","k-restaurant"));//보내는 사람
	 
	        return message;
	    }
	    
	    @Override
	    public void sendSimpleMessage(String to, String tempPassword)throws Exception {
	        // TODO Auto-generated method stub
	        MimeMessage message = createMessage(to, tempPassword);
	        System.out.println(message.getContent());
	        try{//예외처리
	            emailSender.send(message);
	        }catch(MailException es){
	            es.printStackTrace();
	            throw new IllegalArgumentException();
	        }
	    }

}
