package com.lms.ui.login_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.log4j.Logger;


public class DbConnection {
	static Logger log = Logger.getLogger(DbConnection.class);
	
	static DbConnection connection;
	private static String path = "/Users/sanjeet.roy/.dbcred/IlmsUiUsersCred.txt";
	//private static String path = "/Users/sanjeet.roy/.dbcred/ilmsUiRdsCred.txt";
	//private static String path = "/home/centos/.dbCred/ilmsUiRdsCred.txt";
	
	private DbConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	public static String getConnectionCred(){
		
		log.info("Getting Db Credentials");
		String user = System.getenv("DB_USER");
		log.info("Got Db User Name from Env Variable: user = " + user);
		String pass = System.getenv("DB_PASS");
		
		if(user == null && pass== null){
			BufferedReader br = null;
			FileReader fr = null;
			user = "";
			pass = "";
			
			try{
				fr = new FileReader(path);
				br = new BufferedReader(fr);
				user = br.readLine();
				pass = br.readLine();
				
			}catch(IOException e){
				log.error("Exception " + e);
			}
		}
		
		
		return user + ":" + pass;
	}
	
	public static Connection getConnection(){
		if(connection == null){
			connection = new DbConnection();
		}
		
		String cred = getConnectionCred();
		String[] credArr = cred.split(":");
		String user = credArr[0];
		String pass = credArr[1];
		
		try{
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IlmsUiUsers?autoReconnect=true&useSSL=false",user,pass);
			//Connection con = DriverManager.getConnection("jdbc:mysql://auditdatabase.c7iiqzkohnal.us-east-1.rds.amazonaws.com:3306/ilmsUi?autoReconnect=true&useSSL=false",user,pass);
			log.info("Successfully Got the Connection to DB");
			return con;
		}
		catch(Exception e){
			log.error("Exception = " + e.toString());
		}
		return null;
	}
}
