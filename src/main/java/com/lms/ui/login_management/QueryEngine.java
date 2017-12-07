package com.lms.ui.login_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;


public class QueryEngine {
	static Logger log = Logger.getLogger(QueryEngine.class);
	
	public void insertInUsers(String email,String pass,String status){
		
		log.info("Inserting User = " + email + " in DB");
		String query = "insert into users (email,password,status) values (?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		
		try{
			con = DbConnection.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, pass);
			ps.setString(3, status);
		
			ps.executeUpdate();
			log.info("insertion Successfull");
		}
		catch(SQLException e){
			log.error("Exception " + e);
		}finally{
			try{
				ps.close();
				con.close();
			}catch(SQLException e){
				log.error("Exception " + e);
			}
		}
	}
	
	public void insertInLoginStatus(String jti,String status){
		
		log.info("Inserting jti = " + jti + " in DB");
		String query = "insert into loginStatus (jti,status) values (?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		
		try{
			con = DbConnection.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, jti);
			ps.setString(2, status);
		
			ps.executeUpdate();
			log.info("insertion Successfull");
		}
		catch(SQLException e){
			log.error("Exception " + e);
		}finally{
			try{
				ps.close();
				con.close();
			}catch(SQLException e){
				log.error("Exception " + e);
			}
		}
	}
	
	public String getLoginStatus(String jti){
		
		String query = "select * from loginStatus where jti = ?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String dbPass = "";
		String status = "";
		
		try{
			con = DbConnection.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, jti);
			rs = ps.executeQuery();
			
			if(rs.next()){
				status = rs.getString("status");
				log.info("Login status = " +status);
				return status;
			}
		}catch(SQLException e){
			log.error("Exception " + e);
		}finally{
			try{
				rs.close();
				ps.close();
				con.close();
			}catch(SQLException e){
				log.error("Exception " + e);
			}
		}
		
		return "logout";
	}
	
	public void logout(String jti){
		
		String query = "update loginStatus set status=? where jti=?";
		Connection con = null;
		PreparedStatement ps = null;
		
		try{
			con = DbConnection.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, "logout");
			ps.setString(2, jti);
			
			ps.executeUpdate();
			log.info("Jti has been SuccessFully Updated");
		}catch(SQLException e){
			log.error("Exception " + e);
		}finally{
			try{
				ps.close();
				con.close();
			}catch(SQLException e){
				log.error("Exception " + e);
			}
		}
	}
	
	public void deleteJti(String jti){
		log.info("Deleting jti = "+ jti + " from DB");
		String query = "delete from loginStatus where jti=?";
		Connection con = null;
		PreparedStatement ps = null;
		
		try{
			con = DbConnection.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, jti);
			
			ps.executeUpdate();
			log.info("Jti has been Successfully deleted");
		}catch(SQLException e){
			log.error("Exception " + e);
		}finally{
			try{
				ps.close();
				con.close();
			}catch(SQLException e){
				log.error("Exception " + e);
			}
		}
	}
	
	public String getPassword(String email){
		log.debug("Getting Password for " + email);
		String query = "select * from users where email = ?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String dbPass = null;
		
		try{
			con = DbConnection.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, email);
			
			rs = ps.executeQuery();
			if(rs.next()){
				dbPass = rs.getString("password");
				log.debug("Password Fetched Successfully");
			}
		}catch(SQLException e){
			log.error("Exception " + e);
		}finally{
			try{
				rs.close();
				ps.close();
				con.close();
			}catch(SQLException e){
				log.error("Exception " + e);
			}
		}
		return dbPass;
	}
	
	public boolean exist(String email){
		log.info("Checking Existence for " + email);
		String query = "select * from users where email = ?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DbConnection.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, email);
			rs = ps.executeQuery();
			if(rs.next()){
				log.debug("user Exist in DB");
				return true;		
			}
		}catch(SQLException e){
			log.error("Exception " + e);
		}finally{
			try{
				rs.close();
				ps.close();
				con.close();
			}catch(SQLException e){
				log.error("Exception " + e);
			}
		}
		return false;
	}
}
