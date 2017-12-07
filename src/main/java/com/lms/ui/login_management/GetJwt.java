package com.lms.ui.login_management;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.log4j.Logger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// import org.slf4j.LoggerFactory;
// import org.slf4j.Logger;

public class GetJwt {
	static Logger log = Logger.getLogger(GetJwt.class);
	private static String path = "/Users/sanjeet.roy/.vsaq_secret.txt";
	//private static String path = "/home/centos/.dbCred/secret.txt";
	
	public String getToken(String email,String jti,String csrf){
		log.debug("Generating Jwt Token");
		String secret = getSecret();
		byte[] secretByte = null;
		
		if(secret != null){
			try{
				secretByte = secret.getBytes("UTF-8");
			}catch(UnsupportedEncodingException e){
				log.error("Exception = " +e);
			}
		}
		
		String jwt = Jwts.builder()
				  .setExpiration(new Date(System.currentTimeMillis() + 1800000))
				  .claim("email", email)
				  .claim("jti", jti)
				  .claim("csrf", csrf)
				  .signWith(SignatureAlgorithm.HS256,secretByte)
				  .compact();
				
		return jwt;
	}
	
	public Jws<Claims> verifyToken(String token) throws Exception{
		log.debug("Verifying Token");
		String secret = getSecret();
		byte[] secretByte = null;
		
		if(secret != null){
			try{
				secretByte = secret.getBytes("UTF-8");
			}catch(UnsupportedEncodingException e){
				log.error("Exception " + e);
			}
		}
		
		Jws<Claims> claims = Jwts.parser()
				  .setSigningKey(secretByte)
				  .parseClaimsJws(token);

	    return claims;
	}
	
	public String getJtiFromCookie(String token) throws Exception{
		Jws<Claims> claims = verifyToken(token);
		String jti = (String) claims.getBody().get("jti");
		return jti;
	}
	public String getCsrfFromCookie(String token) throws Exception{
		Jws<Claims> claims = verifyToken(token);
		String csrf = (String) claims.getBody().get("csrf");
		return csrf;
	}
	
	public String getEmailFromCookie(String token) throws Exception{
		Jws<Claims> claims = verifyToken(token);
		String email = (String) claims.getBody().get("email");
		return email;
	}
	
	public String getClaimsFromCookie(String token) throws Exception{
		Jws<Claims> claims = verifyToken(token);
		String values = "";
		
		String email = (String) claims.getBody().get("email");
		String jti = (String) claims.getBody().get("jti");
		String csrf = (String) claims.getBody().get("csrf");
		
		values = email + ":" + jti + ":" + csrf;
		
		return values;
	}
	
	public  static String getSecret(){
		
		log.debug("Getting Secret from Evn Variable");
		String secret = System.getenv("JWT_SECRET");
		
		if(secret == null){
			log.debug("Getting Secret from Evn Variable FAILED");
			BufferedReader br = null;
			FileReader fr = null;
			
			
			try{
				fr = new FileReader(path);
				br = new BufferedReader(fr);
				secret = br.readLine();
				
			}catch(IOException e){
				log.error("Exception " + e);
			}
		}
		
		return secret;
	}
}
