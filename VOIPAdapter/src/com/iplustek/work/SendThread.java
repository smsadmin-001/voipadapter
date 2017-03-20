package com.iplustek.work;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jn.powpiping.client.BatchLoadOpt;
import org.jn.powpiping.client.DataType;
import com.iplustek.bean.*;
import com.iplustek.db.DBOper;
import com.iplustek.utils.PropertyLoader;
import com.iplustek.utils.VirtualData;

public class SendThread extends Thread{
	private BatchLoadOpt loadOpt = new BatchLoadOpt();			
	private PropertyLoader propertyLoader;
	private DBOper db_operator;
	private Map<String, String> types = new HashMap<String, String>(); 					
	private Map<String, ByteBuffer> dataset = new HashMap<String, ByteBuffer>();		
	private Logger logger;
	private boolean m_tract;
	public SendThread() throws Exception{
		propertyLoader = PropertyLoader.getInstance();
		db_operator = new DBOper();
		logger = Logger.getLogger(this.getClass());
		m_tract = propertyLoader.isM_tract();
		 //
		types.put("file_name", "string");
		types.put("engine_type", "int");	
		types.put("result", "string");	
		types.put("score", "float");	
		types.put("side_type", "int");	
		types.put("create_time", "string");	
		loadOpt.setDataType(DataType.AVRO);		
		loadOpt.setObjectName("SR_SIMPLE_ENGINE_RESULT");		
		loadOpt.setColumns(types);	
		//
		byte[] bytes = new byte[1];
		ByteBuffer buff = ByteBuffer.wrap(bytes);
		dataset.put("data", buff);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				sendAvro();
				updateStatus();
				if(m_tract){
					logger.info("send avro one time and sleep for "+propertyLoader.getM_send_interval());
				}
				sleep(propertyLoader.getM_send_interval());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void sendAvro(){
		List<SimpleEngineResult> result_lst = null;
		try {
			result_lst = db_operator.getSimpleEngineResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.toString());
		}
		boolean is_empty = result_lst.isEmpty();
		if(is_empty){
			if(m_tract)
				logger.info("no new result");
			return;
		}
		if((!is_empty)&&m_tract){
			logger.info("queried result list size is "+result_lst.size());
			VirtualData.showSendDataResult(result_lst);
			logger.info("begin to put data to map");
		}
		for(int i=0; i<result_lst.size();i++){
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("file_name", result_lst.get(i).getM_file_name());
			properties.put("engine_type",Integer.toString(result_lst.get(i).getM_engine_type()));
			properties.put("result",result_lst.get(i).getM_result());
			properties.put("score",Float.toString(result_lst.get(i).getM_score()));
			properties.put("side_type",Integer.toString(result_lst.get(i).getM_side_type()));
			properties.put("create_time",result_lst.get(i).getM_create_time());
			loadOpt.addRecord(properties, dataset);	
			if(m_tract){
				logger.info("add record to loadOpt and filename is "+result_lst.get(i).getM_file_name());
			}
		}
		if(m_tract){
			logger.info("begin submit to user");
		}
		//loadOpt.sendStruct(propertyLoader.getM_send_ip(), propertyLoader.getM_send_port());
		if(m_tract){
			logger.info("have sent the struct");
		}
		
	}
	
	private void updateStatus(){
		try {
			db_operator.updateDoneFailRecord();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.toString());
		}
		if(m_tract){
			logger.info("have update the done faile record");
		}
	}
	
	

}
