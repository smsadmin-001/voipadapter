package com.iplustek.work;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.iplustek.bean.VoiceRecord;
import com.iplustek.db.DBOper;
import com.iplustek.utils.FileHelper;
import com.iplustek.utils.PropertyLoader;
import com.iplustek.utils.VirtualData;
import com.scistor.ue.proxy.idfs.UEC_Idfs_Client;

public class GetThread extends Thread{
	private SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private PropertyLoader pl;
	private DBOper db_operator;
	private Logger logger;
	private boolean m_tract;
	private UEC_Idfs_Client client = null;
	private byte[] bf;
	public GetThread() throws Exception{
		pl = PropertyLoader.getInstance();
		db_operator = new DBOper();
		logger = Logger.getLogger(this.getClass());
		m_tract = pl.isM_tract();
		//client = new UEC_Idfs_Client(pl.getM_get_ip()+":"+pl.getM_get_port());
	}

	private boolean getBytesData(String key){
		try{
			//bf = client.idfsReadFile(pl.getM_nosql_table_name(), key);
			bf= VirtualData.idfsReadFile(pl.getM_nosql_table_name(), key);
			if(null == bf){
				if(m_tract){
					logger.info("can not find small key "+ key);
				}
				return false;
			}
			else{
				if(m_tract){
					logger.info("find small file key "+ key +", length "+bf.length);
				}
				FileHelper.createFile(pl.getM_save_path()+key, bf);
				if(m_tract){
					logger.info("save wav file, whose key is "+ key);
				}
				return true;
			}
		} catch(Exception e){
			logger.error(e.toString());
			return false; 
		}
	}
	
	public void scanNewKey() throws Exception{
		List<VoiceRecord> vr_lst = new ArrayList<VoiceRecord>();
		Set<String> down_fail_set = new HashSet<String>();
		Set<String> down_succ_set = new HashSet<String>();
		if(m_tract){
			logger.info("scan for new undone key");
		}
		Set<String> undone_key_set = db_operator.getUndoneKeySet();
		if(m_tract){
			logger.info("undone key set size is "+undone_key_set.size());
		}
		for(String key : undone_key_set){
			if(getBytesData(key)){
				VoiceRecord vr = new VoiceRecord(key, date_format.format(new Date()));
				vr_lst.add(vr);
				down_succ_set.add(key);
			}else
				down_fail_set.add(key);
		}
		db_operator.insertVoiceRecords(vr_lst);
		db_operator.updateNosqlProcessStatus(down_succ_set, pl.getM_doing_status());
		db_operator.updateNosqlProcessStatus(down_fail_set, pl.getM_down_fail_status());
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
