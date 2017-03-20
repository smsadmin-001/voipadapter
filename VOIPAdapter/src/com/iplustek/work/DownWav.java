package com.iplustek.work;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.iplustek.bean.VoiceRecord;
import com.iplustek.db.DBOper;
import com.iplustek.utils.FileHelper;
import com.iplustek.utils.PropertyLoader;
import com.iplustek.utils.VirtualData;
import com.scistor.ue.proxy.idfs.UEC_Idfs_Client;

public class DownWav {
	private SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private String file_name;
	private PropertyLoader pl;
	private UEC_Idfs_Client client = null;
	private byte[] bf;
	private Logger logger;
	private boolean m_tract;
	private DBOper dboper;
	public DownWav(String file_name){
		this.file_name = file_name;
		logger = Logger.getLogger(this.getClass());
		try {
			pl = PropertyLoader.getInstance();
			m_tract = pl.isM_tract();
			dboper = new DBOper();
			//client = new UEC_Idfs_Client(pl.getM_get_ip()+":"+pl.getM_get_port());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean download(String thread_name){
		try{
			//bf = client.idfsReadFile(pl.getM_nosql_table_name(), file_name);
			bf= VirtualData.idfsReadFile(pl.getM_nosql_table_name(), file_name);
			if(null == bf){
				if(m_tract){
					logger.info(thread_name+" can not find small file_name "+ file_name);
					dboper.updateNosqlProcessStatusOnce(file_name, pl.getM_down_fail_status());
				}
				return false;
			}
			else{
				if(m_tract){
					logger.info(thread_name+" find small file file_name "+ file_name +", length "+bf.length);
					dboper.updateNosqlProcessStatusOnce(file_name, pl.getM_doing_status());
					dboper.insertOneVoiceRecord(new VoiceRecord("file_name", date_format.format(new Date())));
				}
				FileHelper.createFile(pl.getM_save_path()+file_name, bf);
				if(m_tract){
					logger.info(thread_name+" save wav file, whose file_name is "+ file_name);
				}
				return true;
			}
		} catch(Exception e){
			logger.error(e.toString());
			return false; 
		}finally{
			try {
				//client.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
