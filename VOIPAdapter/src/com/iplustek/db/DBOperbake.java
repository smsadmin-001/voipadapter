package com.iplustek.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.iplustek.bean.SimpleEngineResult;
import com.iplustek.bean.VoiceRecord;
import com.iplustek.db.nosql.NosqlExecutor;
import com.iplustek.db.oracle.OracleExecutor;
import com.iplustek.utils.MyTimer;
import com.iplustek.utils.ParseRecord;
import com.iplustek.utils.PropertyLoader;
import com.iplustek.utils.VirtualData;

public class DBOperbake {
	private NosqlExecutor nosql_executor;
	private OracleExecutor oracle_executor;
	private Logger logger;
	private PropertyLoader pl;
	private boolean m_tract;
	private int upt_batch;
	private static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public DBOperbake() throws Exception{
		pl = PropertyLoader.getInstance();
		PropertyConfigurator.configure("../etc/VOIPAdapter-log4j.properties");
		logger = Logger.getLogger(this.getClass());
		if(m_tract){
			logger.info("new nosql db executor");
		}
		DbConnectInfo nosql_info = new DbConnectInfo(pl.getM_nosql_url(), pl.getM_nosql_username(), pl.getM_nosql_password());
		nosql_executor = new NosqlExecutor(nosql_info);
		if(m_tract){
			logger.info("new oracle db executor");
		}
		DbConnectInfo oracle_info = new DbConnectInfo(pl.getM_oracle_url(), pl.getM_oracle_username(), pl.getM_oracle_password());
		oracle_executor = new OracleExecutor(oracle_info);
		m_tract = pl.isM_tract();
		upt_batch = pl.getM_batch_way();
	}
	
	private void updateOracleProcessStatus(Set<String> id_set,String status){
		if(id_set.isEmpty()){
			if(m_tract)
				logger.info("no data to update for oracle");
			return;
		}
		MyTimer timer = new MyTimer();
		timer.setPreTime(new Date());
		String pst_sql = "update "+pl.getM_oracle_table_name()+" set "+pl.getM_send_status_name()+" = "+status+" where "+pl.getM_oracle_id_name()+"= ?";
		List<Object[]> values=new ArrayList<Object[]>();
		for(String id: id_set){
			values.add(new Object[]{id});
		}
		if(m_tract){
			logger.info("start to update oracle");
		}
		try {
			oracle_executor.safeInsertOrUpdate(pst_sql, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.toString());
		}
		timer.setNowTime(new Date());
		if(m_tract){
			logger.info("It spends "+timer.getPeriodTime()+" to update oracle, the data size is "+ id_set.size());
		}
	}
	
	public void updateNosqlProcessStatus(Set<String> id_set,String status){
		if(id_set.isEmpty()){
			if(m_tract)
				logger.info("no data to update for nosql");
			return;
		}
		MyTimer timer = new MyTimer();
		timer.setPreTime(new Date());
		if(upt_batch==0){
			updateNosqlProcessStatusWithoutBatch(id_set, status);
		}else if(upt_batch==1){
			updateNosqlProcessStatusInPstBatch(id_set, status);
		}
		timer.setNowTime(new Date());
		if(m_tract){
			logger.info("it spends "+timer.getPeriodTime()+" to update nosql, the data size is "+ id_set.size());
		}
	}
	
	private void updateNosqlProcessStatusWithoutBatch(Set<String> id_set,String status){
		int resu = 0;
		String sql;
		for(String id: id_set){
			sql = "update "+pl.getM_nosql_table_name()+" set "+pl.getM_nosql_process_status_name()+"="+status+" where "+pl.getM_nosql_id_name()+"= '"+id+"'";
			try {
				resu = nosql_executor.safeUpdate(sql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e.toString());;
			}
			if(m_tract){
				logger.info("update process status to "+status+" where filename is "+id+" and return is "+resu);
			}
		}
	}
	
	public void updateNosqlProcessStatusInPstBatch(Set<String> id_set,String status){
		String pst_sql = "update "+pl.getM_nosql_table_name()+" set "+pl.getM_nosql_process_status_name()+" = "+status+" where "+pl.getM_nosql_id_name()+"= ?";
		System.out.println(pst_sql);
		pst_sql = "update zrtp set speech_process_status = "+status+" where id=?";
		/*List<Object[]> values=new ArrayList<Object[]>();
		for(String id: id_set){
			values.add(new Object[]{id});
		}
		if(m_tract){
			logger.info("start to update nosql");
		}
		try {
			nosql_executor.safeInsertOrUpdate(pst_sql, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.toString());
		}*/
		Connection conBatch = nosql_executor.getBatchConnection();
		PreparedStatement pst;
		try {
			pst = conBatch.prepareStatement(pst_sql);
			int index = 0;
			for(String id:id_set){
				System.out.println(index++);
				pst.setString(1, id);
				pst.addBatch();
			}
			pst.executeBatch();
			conBatch.commit();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<SimpleEngineResult> getSimpleEngineResult() throws Exception{
		//System.out.println(m_db_oracle.getConnection().toString());
		String sql = "select * from "+pl.getM_oracle_table_name()+" where "+pl.getM_oracle_process_status_name()+" = "
				+pl.getM_done_succ_status()+" and "+pl.getM_send_status_name()+" = "+pl.getM_unsend_status()+" and rownum < "+pl.getM_send_steps();
		ResultSet rs = oracle_executor.safeSelect(sql);
		long NID = 0;
		String SFILENAME = "wav";
		Timestamp STARTTIME;
		int NVADRESULT = -1;
		int NVADSCORE = 0;
		int NGIDRESULT = -1;
		int NGIDSCORE = 0;
		String SLIDRESULT = null;
		List<VoiceRecord> record_lst = new ArrayList<VoiceRecord>();
		Set<String> oracle_id_set = new HashSet<String>();
		Set<String> nosql_id_set = new HashSet<String>();

		while(rs.next()){
			if(m_tract)
				logger.info("row is "+rs.getRow());
			NID= rs.getLong("NID");
			SFILENAME= rs.getString("SFILENAME");
			STARTTIME= rs.getTimestamp("STARTTIME");
			NVADRESULT= rs.getInt("NVADRESULT");
			NVADSCORE= rs.getInt("NVADSCORE");
			NGIDRESULT= rs.getInt("NGIDRESULT");
			NGIDSCORE= rs.getInt("NGIDSCORE");
			SLIDRESULT= rs.getString("SLIDRESULT");
			String STR_START_TIME = date_format.format(STARTTIME);
			VoiceRecord vr = new VoiceRecord(NID,SFILENAME,STR_START_TIME,NVADRESULT,NVADSCORE,NGIDRESULT,NGIDSCORE,SLIDRESULT);
			record_lst.add(vr);
			oracle_id_set.add(Long.toString(NID));
			nosql_id_set.add(SFILENAME);
		}
		if(m_tract)
			logger.info("record list size"+record_lst.size());
		oracle_executor.closeMRes();
		oracle_executor.closeMStm();
		List<SimpleEngineResult> result_lst = ParseRecord.ParseMutilResults(record_lst);
		updateOracleProcessStatus(oracle_id_set, pl.getM_have_sent_status());
		updateNosqlProcessStatus(nosql_id_set, pl.getM_done_succ_status());
		return result_lst;
	}
	

	public Set<String> getUndoneKeySet() throws Exception{
		String sql = "select * from "+pl.getM_nosql_table_name()+" where "+pl.getM_nosql_process_status_name()+" = "
				+pl.getM_undone_status()+" or "+pl.getM_nosql_process_status_name()+" is null limit "+pl.getM_get_steps();
		ResultSet rs = nosql_executor.safeSelect(sql);
		Set<String> id_set = new HashSet<String>();
		String id;
		while(rs.next()){
			id = rs.getString(pl.getM_nosql_id_name());
			id_set.add(id);
		}
		if(rs!=null){
			rs.close();
		}
		return id_set;
	}
	
	public void updateDoneFailRecord() throws Exception{
		String sql = "select * from "+pl.getM_oracle_table_name()+" where "+pl.getM_oracle_process_status_name()+" = "
				+pl.getM_done_fail_status()+" and "+pl.getM_send_status_name()+" = "+pl.getM_unsend_status()+" and rownum < "+pl.getM_send_steps();
		//System.out.println(sql);
		Set<String> oacl_id_set = new HashSet<String>();
		Set<String> nosq_id_set = new HashSet<String>();
		ResultSet rs = oracle_executor.safeSelect(sql);
		String orcl_id;
		String nosq_id;
		while(rs.next()){
			orcl_id = rs.getString(pl.getM_oracle_id_name());
			nosq_id = rs.getString("SFILENAME");
			oacl_id_set.add(orcl_id);
			nosq_id_set.add(nosq_id);
		}
		if(rs!=null){
			rs.close();
		}
		updateOracleProcessStatus(oacl_id_set, pl.getM_have_sent_status());
		updateNosqlProcessStatus(nosq_id_set, pl.getM_done_fail_status());
	}
	
	public void insertVoiceRecords(List<VoiceRecord> vr_lst) throws Exception{
		if(vr_lst.isEmpty())
			return;
		String pst_sql = "insert into " + pl.getM_oracle_table_name()+"(NID, SFILENAME, STARTTIME, NCODERATE, NCODEFORMAT) values(SEQ_ID.Nextval, ?, to_date(?,'YYYY-MM-DD HH24:MI:SS'),"+pl.getM_code_rate()+","+pl.getM_code_format()+")";
		List<Object[]> values = new ArrayList<Object[]>();
		for(VoiceRecord vr: vr_lst){
			values.add(new Object[]{vr.getSFILENAME(),vr.getSTARTTIME()});
		}
		oracle_executor.safeInsertOrUpdate(pst_sql, values);
	}
	
	
	public static void main(String[] args) throws Exception{
		DBOper dbo = new DBOper();

		
		/*
		List<SimpleEngineResult> ls = dbo.getSimpleEngineResult();
		System.out.println(ls.size());
		VirtualData.showSendDataResult(ls);
		*/
		
		
		Set<String> id_set = dbo.getUndoneKeySet();
		System.out.println("size:"+id_set.size());
		for(String s: id_set){
			//System.out.println(s);
		}
		MyTimer mt = new MyTimer();
		mt.setPreTime(new Date());
		dbo.updateNosqlProcessStatus(id_set,"888");
		
		
		
		mt.setNowTime(new Date());
		mt.getPeriodTime();
		
	}
	

}
