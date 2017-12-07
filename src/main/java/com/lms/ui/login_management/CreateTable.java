package com.lms.ui.login_management;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.Statement;

import io.jsonwebtoken.ExpiredJwtException;


public class CreateTable {
	private Connection con = DbConnection.getConnection();
	
	public void createUserTable() throws Exception{
		Statement st=con.createStatement();
		String sql="create table users(userId int(11) auto_increment primary key,email varchar(150) unique not null, password text,status varchar(20))";
		try{
			st.execute(sql);
			System.out.println("users Table created"); 
		}catch(Exception e){
			e.printStackTrace();
		}		
		st.close();
	}
	
	public void createLoginStatusTable() throws Exception{
		Statement st=con.createStatement();
		String sql="create table loginStatus(jti varchar(255) primary key not null,status varchar(20) not null)";
		try{
			st.execute(sql);
			System.out.println("loginStatus Table created"); 
		}catch(Exception e){
			e.printStackTrace();
		}		
		st.close();
	}
	
	public void close() throws Exception{
		con.close();
	}
	
	/*public static void main(String[] args) {
		CreateTable createTable = new CreateTable();
		try{
			createTable.createUserTable();
			createTable.createLoginStatusTable();
		}catch(Exception e){
			System.out.println(e);
		}
		
		try{
			GetJwt getJwt = new GetJwt();
			String claims = getJwt.getClaimsFromCookie("deleted; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT");
			System.out.println(claims);
		}catch(Exception e){
			System.out.println(e);
		}
		
		String test = "First Name,Last Name,Full Name,Company Email,Start Date,Employee Type,Departments,Job Title,Division,Reports To Full Name,Reports To Email,Office City,Office Country,Office Location Address 1,Office Location Address 2,Office Location State ID,Office Location Zip,User Status,Subsidiary Company";
		System.out.println(test.split(",").length);
	}*/
}
