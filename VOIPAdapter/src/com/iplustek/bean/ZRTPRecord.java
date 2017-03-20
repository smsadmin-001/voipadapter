package com.iplustek.bean;

import java.sql.Timestamp;

public class ZRTPRecord {
	private String file_name;
	private Timestamp create_time;
	
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	
	public ZRTPRecord(String file_name, Timestamp create_time){
		this.setFile_name(file_name);
		this.setCreate_time(create_time);
	}
	
}
