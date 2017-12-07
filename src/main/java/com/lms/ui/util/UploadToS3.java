package com.lms.ui.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class UploadToS3 {
	static Logger log = Logger.getLogger(UploadToS3.class);
	
	private static String bucketName     = System.getenv("BUCKET_NAME");//"adhello-lambda-bucket";
	private  String keyName        = ""; //path to file in s3 bucket
	private  String uploadFileName = "/Users/sanjeet.roy/projects/ilms/UI/"; //Local File Path
	
	public void upload(String fileName) throws IOException {
		log.debug("Uploading Local File to S3");
		keyName = fileName;
		uploadFileName += fileName;
		
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            log.info("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
            s3client.putObject(new PutObjectRequest(
            		                 bucketName, keyName, file));
            log.info("Done Uploading !!");

         } catch (AmazonServiceException ase) {
            log.error("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            log.error("Error Message:    " + ase.getMessage());
            log.error("HTTP Status Code: " + ase.getStatusCode());
            log.error("AWS Error Code:   " + ase.getErrorCode());
            log.error("Error Type:       " + ase.getErrorType());
            log.error("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
        	log.error("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
        	log.error("Error Message: " + ace.getMessage());
        }
    }
	
	public boolean upload(InputStream is,String fileName,int length) throws IOException {
		log.debug("Uploading inputStream to S3");
		
		keyName = fileName;
		
		if(bucketName == null){
			bucketName = "adhello-lambda-bucket";
		}
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("text/csv");
		metadata.setContentLength(length);
		
        AmazonS3 s3client = new AmazonS3Client(); //new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            log.info("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
            s3client.putObject(new PutObjectRequest(
            		                 bucketName, keyName, is,metadata));
            log.info("Done Uploading !!");
            
            return true;

         } catch (AmazonServiceException ase) {
        	 log.error("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
        	 log.error("Error Message:    " + ase.getMessage());
        	 log.error("HTTP Status Code: " + ase.getStatusCode());
        	 log.error("AWS Error Code:   " + ase.getErrorCode());
        	 log.error("Error Type:       " + ase.getErrorType());
        	 log.error("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
        	log.error("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
        	log.error("Error Message: " + ace.getMessage());
        }
        
        return false;
    }
}
