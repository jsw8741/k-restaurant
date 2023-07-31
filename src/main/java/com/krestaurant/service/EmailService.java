package com.krestaurant.service;

public interface EmailService {
	public void sendSimpleMessage(String to, String tempPassword)throws Exception;
}
