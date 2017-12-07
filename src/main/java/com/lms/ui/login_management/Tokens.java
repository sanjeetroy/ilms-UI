package com.lms.ui.login_management;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.log4j.Logger;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class Tokens {
	static Logger log = Logger.getLogger(Tokens.class);
	
	public String getCsrfToken() throws NoSuchAlgorithmException{
		log.debug("Generating CSRF token");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[8];
        sr.nextBytes(salt);
        return toHex(salt);
	}
	
	private String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
	
	public String getJti() throws NoSuchAlgorithmException{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[64];
        sr.nextBytes(salt);
        return toHex(salt);
	}
}
