package com.lms.ui.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import com.lms.ui.domain.AdUser;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public class ValidateCsv {
	static Logger log = Logger.getLogger(ValidateCsv.class);
	static String column_length = System.getenv("COLUMN_LENGTH");
	public static  int COLUMN_LENGTH;
	public static final int  MIN_EMAIL_LEN = 5;
	
	private static String fileNameRegex = "^[0-9a-zA-Z_]+.csv$";
	private static final Pattern VALID_CSV_FILENAME_REGEX = 
    	    Pattern.compile(fileNameRegex, Pattern.CASE_INSENSITIVE);
	
	public ValidateCsv(){
		if(column_length != null){
			COLUMN_LENGTH =Integer.parseInt(column_length);
		}
		else{
			COLUMN_LENGTH = 19;
		}
	}
	public boolean validateFileNameAndHeaders(String name,String contentType){
		log.info("Validaing file Name and headers");
		boolean res = false;
		
		if(contentType.equals("text/csv")){
		        Matcher matcher = VALID_CSV_FILENAME_REGEX .matcher(name);
		        res = matcher.find();
		        if(res){
		        	log.info("File is Valid");
		        	return true;
		        }
		}
		log.info("File is in-Valid");
		return false;
	}
	
	public boolean validate(InputStream is){
		log.info("Validaing file Contents");
		
        BufferedReader bfReader = null;
        boolean finalResult = true;
        
        try {
            bfReader = new BufferedReader(new InputStreamReader(is));
            
            String line = null;
            String headers = bfReader.readLine();
            String[] headersArr = parseLine(headers);
            
            log.debug("Validating 3rd column (It Must be \"Company Email\") ");
            if(!headersArr[3].equalsIgnoreCase("Company Email")){
            	log.info("3rd Column is not Company");
            	return false;
            }
            
            finalResult = finalResult && validateLength(headersArr.length);
            
            log.debug("Headers = " + headers);
            log.debug("Headres Len = " + headersArr.length);
            
            String[] lineArr = null;
            
            while((line = bfReader.readLine()) != null){
            	
            	lineArr = parseLine(line);
            	log.debug("Line Arr = " + lineArr);
            	log.info("line len = " + lineArr.length);
            	
            	log.debug("validatiing line length");
            	finalResult = finalResult && validateLength(lineArr.length);
            	
            	log.debug("Line length res = " + finalResult);
            	
            	log.debug("validatiing Email ");
            	finalResult = finalResult && validateEmail(lineArr[3]);
            	log.debug("Email res = " + finalResult);
            	
            }
            
        } catch (IOException e) {
        	log.error("Error in readinf file in validation");
            finalResult = false;
        }catch(Exception e){
        	log.error("Exception = " + e);
        	finalResult = false;
        }finally {
            try{
                if(is != null){
                	is.close();
                }
                bfReader.close();
            } catch (Exception ex){
            	finalResult = false;
            	log.error("Exception : "+ ex );
            }
        }
		return finalResult;
	}
	
	public List<AdUser> parse(BufferedReader  br){
		ArrayList<AdUser> adUsers = new ArrayList<AdUser>();
		
        
        try {
			BeanListProcessor<AdUser> rowProcessor = new BeanListProcessor<AdUser>(AdUser.class);

		    CsvParserSettings parserSettings = new CsvParserSettings();
		    parserSettings.setRowProcessor(rowProcessor);
		    parserSettings.setHeaderExtractionEnabled(true);

		    CsvParser parser = new CsvParser(parserSettings);
		    parser.parse(br);

		    adUsers = new ArrayList<AdUser> (rowProcessor.getBeans());
		    
		}catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				System.out.println("Exception: "+ ex);
			}
		}
		return adUsers;
	}
	
	public String[] parseLine(String line){
		log.debug("Parsing line of CSV");
		String[] lineArr = null;
        
        try {

		    CsvParserSettings parserSettings = new CsvParserSettings();
		    parserSettings.getFormat().setLineSeparator("\n");

		    CsvParser parser = new CsvParser(parserSettings);
		    lineArr = parser.parseLine(line);
		    
		}catch (Exception e) {
			log.error("Exception: " + e);
		} 
		return lineArr;
	}
	
	public boolean validateLength(int len){
		if(len==COLUMN_LENGTH){
			return true;
		}
		return false;
	}
	
	public boolean validateEmail(String email){
		String cleanEmail = "";
		CheckEmail checkEmail = new CheckEmail();
		
		 if(email != null){
			 cleanEmail = email.trim().replaceAll(" +", " ");
		 }
		
		if(cleanEmail.length() > MIN_EMAIL_LEN){ // Assuming Email must have ".com","@" in it.
			
			return checkEmail.validateCsvEmail(email);
		}
		return false;
	}
}
