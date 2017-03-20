package com.iplustek.work;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.iplustek.bean.VoiceRecord;
import com.iplustek.db.DBOper;
import com.iplustek.utils.PropertyLoader;

public class GetThread3 extends Thread{
	private SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private PropertyLoader pl;
	private DBOper db_operator;
	private Logger logger;
	private boolean m_tract;
	private List<DownWav> downList = new ArrayList<DownWav>();
	public GetThread3() throws Exception{
		pl = PropertyLoader.getInstance();
		db_operator = new DBOper();
		logger = Logger.getLogger(this.getClass());
		m_tract = pl.isM_tract();
	}

	
	public void scanNewKey() throws Exception{
		List<VoiceRecord> vr_lst = new ArrayList<VoiceRecord>();
		if(m_tract){
			logger.info("scan for new undone key");
		}
		Set<String> undone_key_set = db_operator.getUndoneKeySet();
		if(m_tract){
			logger.info("undone key set size is "+undone_key_set.size());
		}
		
		for(String key : undone_key_set){
			downList.add(new DownWav(key));
		}
		
		//number of thread
		for(int i=0;i<5;i++){
			DownThread thread = new DownThread(downList,"thread"+i);
			thread.start();
		}
		//db_operator.insertVoiceRecords(vr_lst);
		//db_operator.updateNosqlProcessStatus(undone_key_set, pl.getM_doing_status());
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				if(m_tract){
					logger.info("sleep "+pl.getM_get_interval()+" and then scan for new key");
				}
				sleep(pl.getM_get_interval());
				scanNewKey();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e.toString());
			}
		}
	}
	

}
