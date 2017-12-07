package com.lms.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lms.ui.domain.CsrfToken;
import com.lms.ui.domain.LoginDetail;
import com.lms.ui.domain.Name;
import com.lms.ui.domain.SignUp;
import com.lms.ui.login_management.GetJwt;
import com.lms.ui.service.DbService;
import com.lms.ui.util.AddLineToInputStream;
import com.lms.ui.util.CheckEmail;
import com.lms.ui.util.TokenService;
import com.lms.ui.util.UploadToS3;
import com.lms.ui.util.ValidateCsv;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@Controller
public class UploadController {
	static Logger log = Logger.getLogger(UploadController.class);
    //Save the uploaded file to this folder
    
    @GetMapping("/")
    public String index(@CookieValue(value = "BEARER", defaultValue = "noCookie") String token,Model model,HttpServletResponse response) {
    	
    	log.info("User Trying to log in..");
    	DbService dbService = new DbService();
    	GetJwt getJwt = new GetJwt();
    	
    	String claims ="";
    	String jti = "";
    	String expireToken = "deleted; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT";
    	
    	if(!token.equals("noCookie") && !token.contains("deleted")){
    		try{
    			log.info("Received Active Cookie..");
    			log.info("In Processing login");
    			claims = getJwt.getClaimsFromCookie(token);
            	String[] values = claims.split(":");
            	String csrf = values[2];
            	jti = values[1];
            	String email = values[0];
            	String userName = "";
            	String loginStatus = "";
            	
            	loginStatus = dbService.getLoginStatus(jti);
        		
        		if(loginStatus.equals("login")){
        			
        			log.info("User is already logged in");
        			log.debug("Redirecting to home page.");
        			userName = email.split("@")[0];
        			
        			String jwt = getJwt.getToken(email, jti, csrf);
                	
                	Name name = new Name();
                    name.setName(userName);
                    	
                    CsrfToken csrfTokenNew = new CsrfToken();
                    csrfTokenNew.setValue(csrf);
                    	
                    model.addAttribute("csrfToken",csrfTokenNew);
                    model.addAttribute("name",name);
                    	
                    response.setHeader("Cache-Control","no-cache, no-store");
                    response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
                    response.setHeader("Pragma", "no-cache");
                    response.setHeader("Set-Cookie", "BEARER=" + jwt);
                    
                    return "home";
        		}
            	
    		}catch(ExpiredJwtException e){
    			jti = (String) e.getClaims().get("jti");
    			log.error("Jwt Token Expires");
    			log.info("Logging out jti = " +  jti);
    			dbService.logoutUser(jti);
    		
    	    	response.setHeader("Set-Cookie", "BEARER=" + expireToken);
    	    	log.info("redirecting to Login Page");
    			return "redirect:/";
    			
    		}catch(MalformedJwtException e){
    			log.error("Exception = " + e);
    		}catch(Exception e){
    			log.error("Exception = " + e);
    		}
    	}
    	
    	log.info("No Cookie received ");
    	log.debug("redirecting to login page ");
    	LoginDetail loginDetail = new LoginDetail();
   
    	model.addAttribute("loginDetail", loginDetail);
    	response.setHeader("Cache-Control","no-cache, no-store");
    	response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
    	response.setHeader("Pragma", "no-cache");
    	
        return "index";
    }
    
    @GetMapping("/*")
    public String any(Model model,HttpServletResponse response){
    	LoginDetail loginDetail = new LoginDetail();
    	  
    	log.info("Received Anonymous Url");
    	log.debug("Redirecting to login Page");
    	model.addAttribute("loginDetail", loginDetail);
    	response.setHeader("Cache-Control","no-cache, no-store");
    	response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
    	response.setHeader("Pragma", "no-cache");
    	
        return "index";
    }
    
    @PostMapping("/home")
    public String login(@ModelAttribute LoginDetail loginDetail,Model model,HttpServletResponse response){
    	
    	boolean res = false;
    	String firstName = "";
    	
    	String email = loginDetail.getEmail();
    	String pass = loginDetail.getPassword();
    	
    	log.info("Received Log in Request for " + email);
    	
    	DbService dbService = new DbService();
    	TokenService tokenService = new TokenService();
    	GetJwt getJwt = new GetJwt();
    	
    	if(!email.equals("") && !pass.equals("")){
    		
    		res = dbService.checkUser(email, pass);
    		
    		if(res){
    			log.info("Log in details are Correct.");
    			log.debug("redirecting to Home Page");
    			
        		firstName = email.split("@")[0];
            		
            	String csrf = tokenService.getCsrfToken();
            	String jti = tokenService.getJtiToken();
            	String jwt = getJwt.getToken(email, jti, csrf);
            	
            	dbService.loginUser(jti);
            	Name name = new Name();
                name.setName(firstName);
                	
                CsrfToken csrfToken = new CsrfToken();
                csrfToken.setValue(csrf);
                	
                model.addAttribute("csrfToken",csrfToken);
                model.addAttribute("name",name);
                	
                response.setHeader("Cache-Control","no-cache, no-store");
                response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Set-Cookie", "BEARER=" + jwt);
                	
                return "home";
        	}
        	
    	}
    	
    	log.info("Log in details are In-Correct.");
		log.debug("redirecting to Login-Error Page");
		
    	LoginDetail login = new LoginDetail();
    	model.addAttribute("loginDetail", login);
    	
    	return "login-error";
    }
    
    @PostMapping("/signup")
    public String postSignUp(@ModelAttribute SignUp signUp,Model model){
    	CheckEmail checkEmail = new CheckEmail();
    	
    	String email = signUp.getEmail();
    	log.info("received Sign up Request for " + email);
    	
    	if(checkEmail.validate(email)){
    		String pass = signUp.getPassword();
        	String cpass = signUp.getCpassword();
        	
        	if(pass!=null && !pass.equals("") && pass.equals(cpass)){
        		log.info("Registering user " + email);
        		
        		DbService dbService = new DbService();
            	dbService.insertInUsers(email, pass);
            	
            	return "redirect:/";
        	}else{
        		log.error("Password and Confirm Password are not same");
        		log.debug("Redirecting to signup-Error-Pass Page");
        		SignUp newSignUp = new SignUp();
            	model.addAttribute("signUp",newSignUp);
            	
        		return "signup-error-pass";
        	}
        			
    	}else{
    		log.error("email validation Faild.");
    		log.debug("Redirecting to signup-Error Page");
    		SignUp newSignUp = new SignUp();
        	model.addAttribute("signUp",newSignUp);
        	
    		return "signup-error";
    	}
        	
    }
    
    @GetMapping("/signup")
    public String signup(Model model) {
    	log.info("Received GET request for signup");
    	log.debug("Redirecting to signup Page");
    	
    	SignUp signUp = new SignUp();
    	model.addAttribute("signUp",signUp);
    	
        return "signup";
    }
    
    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@CookieValue(value = "BEARER",defaultValue = "noCookie") String token,@ModelAttribute CsrfToken csrfToken,HttpServletResponse response,@RequestParam("file") MultipartFile file,
                                   Model model) {
    	
    	log.info("Received Request to upload file");
    	if(token.equals("noCookie")){
    		log.info("In-valid Cookie found. ");
    		log.info("Redirecting to login Page");
    		return "redirect:/";
    	}
    	
    	String claims = "";
    	String jti ="";
    	GetJwt getJwt  = new GetJwt();
    	DbService dbService = new DbService();
        
        try{
        	claims = getJwt.getClaimsFromCookie(token);
        	String[] values = claims.split(":");
        	String csrf = values[2];
        	jti = values[1];
        	String email = values[0];
        	String userName = "";
        	
        	String loginStatus = "";
        	userName = email.split("@")[0];
			
        	if (file.isEmpty()) {
            	log.info("No File is selected to upload");
            	log.debug("Redirecting to upload page");
                model.addAttribute("message", "Please select a file to upload");
                
                Name name = new Name();
                name.setName(userName);
                model.addAttribute("name",name);
                
                return "upload";
            }
        	
        	if(csrf.equals(csrfToken.getValue())){
        		log.info("Received a valid Csrf Token");
        		loginStatus = dbService.getLoginStatus(jti);
        		
        		if(loginStatus.equals("login")){
        			try {
        				log.info("User is logged in");
        	            // Get the file and save it somewhere
        	            byte[] bytes = file.getBytes();
        	            int contentLength = bytes.length;
        	            
        	            InputStream inputStream = file.getInputStream();
        	            InputStream inputStreamToUpload  = new ByteArrayInputStream(bytes);;
        	            
        	            //System.out.println(new String(bytes));
        	            String fileName = file.getOriginalFilename();
        	            String contentType = file.getContentType();
        	            
        	            
        	            log.info("ContentType of file = " + file.getContentType());
        	            log.info("File name = " + file.getOriginalFilename());
        	            //Path path = Paths.get(UPLOADED_FOLDER + fileName);
        	            ValidateCsv validateCsv = new ValidateCsv();
        	            
        	            boolean fileNameAndHeaderValidationResult = false;
        	            boolean fileContentValidationResult = false;
        	            
        	            log.info("Validating fileName and headers");
        	            fileNameAndHeaderValidationResult = validateCsv.validateFileNameAndHeaders(fileName, contentType);
        	            log.info("Validation Result = " + fileNameAndHeaderValidationResult);
        	            
        	            if(fileNameAndHeaderValidationResult){
        	            	log.info("Validating file Contents");
            	            fileContentValidationResult = validateCsv.validate(inputStream);
            	            log.info("Validation Result = " + fileContentValidationResult);
        	            }
        	            
        	            
        	            if(fileNameAndHeaderValidationResult && fileContentValidationResult){
        	            	log.info("Both Header and file Content verification are TRUE");
        	            	//Files.write(path, bytes);
            	            
            	            //System.out.println("Content = " + new String(bytes));
            	            UploadToS3 uploadToS3 = new UploadToS3();
            	            
            	            log.info("Adding noOps to InputStream");
            	            AddLineToInputStream addLineToInputStream = new AddLineToInputStream();
            	            InputStream inputStreamAfterLineAdd = addLineToInputStream.addLine("noOps=1\n",inputStreamToUpload);
            	            
            	            log.info("Uploading file = " + fileName + " to s3");
//            	            uploadToS3.upload(fileName);
            	            
            	            boolean uploadRes = uploadToS3.upload(inputStreamAfterLineAdd, fileName, contentLength+8);
            	            
            	            if(uploadRes){
            	            	log.info("Successfully Uploaded File " + fileName);
            	            	model.addAttribute("message",
            	            			"Successfully Uploaded '" + fileName + "'");
            	            }else{
            	            	log.info("Uploading File " + fileName + " to S3 Fails");
            	            	model.addAttribute("message",
                 	                    "Uploading to S3 Fails");
            	            }
            	                    
        	            }else{
        	            	log.info("Validation of file Failed");
        	            	log.info("Skipping file..");
        	            	 model.addAttribute("message",
             	                    "Please Upload a Valid Csv File (Note: Spaces are Note Allowed in file Name)");
        	            }
        	            

        	        } catch (IOException e) {
        	        	log.error("Exception = " + e);
        	            e.printStackTrace();
        	        }
        			
        			userName = email.split("@")[0];
        			
        			String jwt = getJwt.getToken(email, jti, csrf);
                	
                	Name name = new Name();
                    name.setName(userName);
                    
                    log.debug("Generating a new CSRF Token");
                    CsrfToken csrfTokenNew = new CsrfToken();
                    csrfTokenNew.setValue(csrf);
                    	
                    model.addAttribute("csrfToken",csrfTokenNew);
                    model.addAttribute("name",name);
                    	
                    response.setHeader("Cache-Control","no-cache, no-store");
                    response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
                    response.setHeader("Pragma", "no-cache");
                    response.setHeader("Set-Cookie", "BEARER=" + jwt);
                    
                    log.debug("Redirecting to upload");
        	        return "upload";
        		}else{
        			log.info("User is not logged-in ");
        			log.info("Redirecting to login Page");
        			return "redirect:/";
        		}
        	}
        }catch(ExpiredJwtException e){
			jti = (String) e.getClaims().get("jti");
			log.error("Jwt Token Expires");
			log.error("Logging out Jti = " + jti);
			dbService.logoutUser(jti);
			
			log.debug("Redirecting to log in page");
			return "redirect:/";
			
		}catch(Exception e){
        	log.error("Exception = " +e);
        	model.addAttribute("message",
	                    "Something wrong Please Upload Again.");
        	return "redirect:/";
        }
        
        model.addAttribute("message", "Please select a file to upload");
        return "upload";
    }
    
    @PostMapping("/logout")
    public String logout(@CookieValue(value = "BEARER",defaultValue = "noCookie") String token,@ModelAttribute CsrfToken csrfToken,HttpServletResponse response){
    	
    	log.info("Received log-out Request");
    	if(token.equals("noCookie")){
    		log.info("Invalid Cookie Found");
    		log.debug("Redirecting to log-in page");
    		return "redirect:/";
    	}
    	
    	String claims = "";
    	String loginStatus = "";
    	String expireToken = "deleted; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT";
    	String jti = "";
    	String csrf = "";
    	GetJwt getJwt  = new GetJwt();
    	DbService dbService = new DbService();
    	
    	try{
    		claims = getJwt.getClaimsFromCookie(token);
        	String[] values = claims.split(":");
        	csrf = values[2];
        	jti = values[1];
        	
        	if(csrf.equals(csrfToken.getValue())){
        		log.info("Received a valid csrf token");
        		
        		loginStatus = dbService.getLoginStatus(jti);
        		if(loginStatus.equals("login")){
        			log.info("user is logged in");
        			log.info("Going to logout User");
        			log.info("Login Status Has been changed to logout");
        			dbService.logoutUser(jti);
        			response.setHeader("Set-Cookie", "BEARER=" + expireToken);
        			
        			log.debug("Redirecting to login page");
        			return "redirect:/";
        		}
        	}
        	
    	}catch(ExpiredJwtException e){
			jti = (String) e.getClaims().get("jti");
			csrf = (String ) e.getClaims().get("csrf");
			log.error("Jwt Token Expires");
			
			if(csrf.equals(csrfToken.getValue())){
        		loginStatus = dbService.getLoginStatus(jti);
        		if(loginStatus.equals("login")){
        			log.info("user is logged in");
        			log.info("Going to logout User");
        			log.info("Login Status Has been changed to logout");
        			dbService.logoutUser(jti);
        			response.setHeader("Set-Cookie", "BEARER=" + expireToken);
        		}
        	}
			log.debug("Redirecting to login page");
			return "redirect:/";
			
		}catch(Exception e){
    		log.error("Exception = " +e);
    	}
    	log.debug("Redirecting to login page");
    	return "redirect:/";
    }

}
