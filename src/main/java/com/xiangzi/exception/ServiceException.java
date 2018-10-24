package com.xiangzi.exception;

public class ServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 1037612837019575552L;

	public ServiceException(String msg) {
		super(msg);
	}
	
}
