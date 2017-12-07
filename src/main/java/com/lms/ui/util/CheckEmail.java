package com.lms.ui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;


public class CheckEmail {
	
	static Logger log = Logger.getLogger(CheckEmail.class);
	
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
    	    Pattern.compile("^[A-Z0-9._%+-]+@appdirect.com$", Pattern.CASE_INSENSITIVE);
	
	private static final Pattern VALID_CSV_EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9._%+-]+.[A-Z]{2,3}$", Pattern.CASE_INSENSITIVE);
	
	public boolean validate(String emailStr) {
		log.debug("Validating Email Address " + emailStr);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
	}
	
	public boolean validateCsvEmail(String emailStr) {
		log.debug("Validating CSV Email = " + emailStr);
        Matcher matcher = VALID_CSV_EMAIL .matcher(emailStr);
        return matcher.find();
	}
}
