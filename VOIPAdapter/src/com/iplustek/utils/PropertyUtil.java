package com.iplustek.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	
	public void setProperty(String key, String value){
		Properties prop = new Properties();
		try{
			FileOutputStream out= new FileOutputStream("../etc/time.properties");
			prop.setProperty(key, value);
			prop.store(out, null);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getProperty(String key){
		Properties prop = new Properties();
		String value = "";
		InputStream in= null;
		try{
			in = new BufferedInputStream(new FileInputStream("../etc/time.properties"));
			prop.load(in);
			value = prop.getProperty(key);
			in.close();
			return value;
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
	}
	
	public static void main(String[] args){
		PropertyUtil pl = new PropertyUtil();
		//pl.setProperty("last_date", "2017-03-14 11:27:58.98");
		System.out.println(pl.getProperty("last_time"));
	}

}
