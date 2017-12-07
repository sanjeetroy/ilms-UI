package com.lms.ui.util;

import java.security.NoSuchAlgorithmException;

import com.lms.ui.login_management.Tokens;

public class TokenService {
	
	public String getCsrfToken(){
		Tokens tokens = new Tokens();
		String csrf = "vkfW4yXncIuiwZ3BCHYz4zlM2QANSCwtKC5vfRB7MSE";
		try{
			csrf = tokens.getCsrfToken();
		}catch(NoSuchAlgorithmException e){
			System.out.println(e);
		}
		return csrf;
	}
	
	public String getJtiToken(){
		Tokens tokens = new Tokens();
		String jti = "eyJleHAiOjE1MTE5NTc2MjMsIm5hbWUiOiJzYW5qZWV0In0";
		try{
			jti = tokens.getJti();
		}catch(NoSuchAlgorithmException e){
			System.out.println(e);
		}
		return jti;
	}
	
	
	
}
