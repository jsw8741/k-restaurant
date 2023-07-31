package com.krestaurant.exception;

public class OutOfStockException extends RuntimeException{
	
		// 재고가 적으면 발생되는 exception
		public OutOfStockException(String message) {
			super(message);
		}
}
