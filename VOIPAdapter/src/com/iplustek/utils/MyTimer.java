package com.iplustek.utils;

import java.util.Date;

public class MyTimer {
	private long previous_time;
	private long now_time;
	
	public void setPreTime(Date date){
		previous_time = date.getTime();
	}
	public void setNowTime(Date date){
		now_time = date.getTime();
	}
	public String getPeriodTime(){
		if(previous_time>=now_time)
			return "0ms";
		long dur = now_time-previous_time;
		String result = "";
		if(dur>1000){
			if(dur>60000){
				result = Integer.toString((int)dur/60000)+"min "+Integer.toString((int)(dur%60000)/1000)+"s "+dur%1000+"ms";
			}else{
				result = Integer.toString((int)(dur%60000)/1000)+"s "+dur%1000+"ms";
			}
		}
		else{
			result = dur+"ms";
		}
		return result;
	}

}
