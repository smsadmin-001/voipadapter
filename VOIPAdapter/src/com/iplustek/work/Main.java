package com.iplustek.work;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.iplustek.db.DBSynchronizer;
import com.iplustek.utils.PropertyLoader;

public class Main {
	public static void main(String[] args) throws Exception{
		PropertyConfigurator.configure("../etc/VOIPAdapter-log4j.properties");
		Logger logger = Logger.getLogger(Main.class);
		PropertyLoader ploader = PropertyLoader.getInstance();
		boolean syn_enable = ploader.isM_syn_enable();
		boolean send_enable = ploader.isM_send_enable();
		boolean get_enable = ploader.isM_get_enable();
		
		if(syn_enable){
			//start syn
			DBSynchronizer syner = new DBSynchronizer();
			Thread synThread = new Thread(syner);
			synThread.start();
		}
		
		if(send_enable){
			logger.info("new send thread");
			SendThread sendThread = new SendThread();
			sendThread.start();
		}
		if(get_enable){
			logger.info("new get thread");
			GetThread getThread = new GetThread();
			getThread.start();
		}
	}
}
