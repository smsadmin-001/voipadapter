package com.iplustek.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBSource {
	private String url;
	private String usr;
	private String pwd;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsr() {
		return usr;
	}
	public void setUsr(String usr) {
		this.usr = usr;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public DBSource(String driver, String url, String usr, String pwd){
		this.url = url;
		this.usr = usr;
		this.pwd = pwd;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		try {
			return DriverManager.getConnection(url,usr,pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
