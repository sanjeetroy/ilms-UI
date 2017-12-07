package com.lms.ui.service;

import java.security.NoSuchAlgorithmException;

import java.security.spec.InvalidKeySpecException;

import org.apache.log4j.Logger;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import com.lms.ui.login_management.ManageDbPassword;
import com.lms.ui.login_management.QueryEngine;

public class DbService {
	static Logger log = Logger.getLogger(DbService.class);
	
	public boolean checkUser(String email,String pass){
		log.debug("Checking Credential for email = " + email);
		boolean res = false;
		
		QueryEngine queryEngine = new QueryEngine();
		ManageDbPassword manageDbPassword = new ManageDbPassword();
		
		String dbPass = queryEngine.getPassword(email);
		
		if(dbPass != null){
			try{
				res =  manageDbPassword.validatePassword(pass, dbPass);
				log.debug("Credentails are " + res);
			}catch(InvalidKeySpecException e){
				log.error("Exception = " +e);
			}catch(NoSuchAlgorithmException ne){
				log.error("Exception = " + ne);
			}
		}
		
		return res;
	}
	
	public boolean checkForExistance(String email){
		log.debug("Checking for existence of" + email);
		QueryEngine queryEngine = new QueryEngine();
		return queryEngine.exist(email);
	}
	
	public void insertInUsers(String email,String pass){
		log.debug("Inserting user " + email + " in DB");
		QueryEngine queryEngine = new QueryEngine();
		ManageDbPassword manageDbPassword = new ManageDbPassword();
		
		String hashPass = "";
		
		try{
			hashPass = manageDbPassword.getStrongPasswordHash(pass);
		}catch(InvalidKeySpecException e){
			log.error("Exception = " +e);
		}catch(NoSuchAlgorithmException ne){
			log.error("Exception = " + ne);
		}
		
		queryEngine.insertInUsers(email, hashPass, "Active");
	}
	
	public void loginUser(String jti){
		log.debug("Logging jti = " + jti + " in DB");
		
		QueryEngine queryEngine = new QueryEngine();
		queryEngine.insertInLoginStatus(jti, "login");
	}
	
	public void logoutUser(String jti){
		log.debug("Logging out jti = " + jti + " in DB");
		
		QueryEngine queryEngine = new QueryEngine();
		queryEngine.logout(jti);
		
	}
	
	public String getLoginStatus(String jti){
		log.debug("getting Log-in status of jti = " + jti );
		
		QueryEngine queryEngine = new QueryEngine();
		return queryEngine.getLoginStatus(jti);
	}
	
}
