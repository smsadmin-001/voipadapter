package com.iplustek.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.iplustek.bean.ZRTPRecord;
import com.iplustek.utils.PropertyLoader;
import com.iplustek.utils.PropertyUtil;

public class DBSynchronizer implements Runnable{
	private DBSource dborg;
	private DBSource dbdes;
	private PropertyUtil proUtil = new PropertyUtil();
	private PropertyLoader pl;
	
	public DBSynchronizer(){
		try {
			pl = PropertyLoader.getInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dborg = new DBSource(pl.getM_gbsql_driver(), pl.getM_gbsql_url(), pl.getM_gbsql_username(), pl.getM_gbsql_password());
		this.dbdes = new DBSource(pl.getM_nosql_driver(), pl.getM_nosql_url(), pl.getM_nosql_username(), pl.getM_nosql_password());
	}
	
	public List<ZRTPRecord> getOrgList() throws SQLException{
		String last_time = proUtil.getProperty("last_time");
		Connection conorg = dborg.getConnection();
		String sql = "select file_name, create_time from zrtp where create_time > '"+last_time +"' order by create_time limit 3";
		System.out.println(sql);
		Statement stm = conorg.createStatement();
		ResultSet rst = stm.executeQuery(sql);
		List<ZRTPRecord> zrtp = new ArrayList<ZRTPRecord>();
		String file_name;
		Timestamp ts = null;
		while(rst.next()){
			file_name = rst.getString("file_name");
			ts = rst.getTimestamp("create_time");
			ZRTPRecord record = new ZRTPRecord(file_name,ts);
			zrtp.add(record);
			System.out.println(ts.toString());
		}
		if(!zrtp.isEmpty()){
			System.out.println("Final***"+ts.toString());
			proUtil.setProperty("last_time", ts.toString());
		}
		stm.close();
		conorg.close();
		return zrtp;
	}
	
	public void setDesTable(List<ZRTPRecord> zrtp) throws SQLException{
		Connection condes = dbdes.getConnection();
		String sql = "insert into zrtp(file_name, create_time) values (?,?)";
		condes.setAutoCommit(false);
		PreparedStatement pst = condes.prepareStatement(sql);
		for(ZRTPRecord record:zrtp){
			pst.setString(1, record.getFile_name());
			pst.setTimestamp(2, new Timestamp(record.getCreate_time().getTime()));
			pst.addBatch();
		}
		pst.executeBatch();
		condes.commit();
		pst.close();
		condes.close();
	}
	
	public void doSyn() throws SQLException{
		System.out.println("start syn");
		List<ZRTPRecord> list = getOrgList();
		System.out.println("end of get, size is "+list.size());
		//showList(list);
		setDesTable(list);
		System.out.println("end of set");
		
	}
	
	public void showList(List<ZRTPRecord> zrtp){
		for(ZRTPRecord record:zrtp){
			System.out.println(record.getFile_name()+"	"+record.getCreate_time());
		}
	}
	
	public static void main(String [] args){
		DBSynchronizer syn = new DBSynchronizer();
		Thread synchronizer = new Thread(syn);
		synchronizer.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true){
				doSyn();
				System.out.println("sleep 3 s");
				Thread.sleep(3000);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
