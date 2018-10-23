package com.xiangzi.util;

public class JSONException extends RuntimeException {

	private static final long serialVersionUID = 2495237804754097036L;

	public JSONException(){
        super();
    }
    
    public JSONException(String message){
        super(message);
    }

    public JSONException(String message, Throwable cause){
        super(message, cause);
    }
    
}