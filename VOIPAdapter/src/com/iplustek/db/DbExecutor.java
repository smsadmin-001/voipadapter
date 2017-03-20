package com.iplustek.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.sun.rowset.CachedRowSetImpl;

public class DbExecutor {
	private DbConnectInfo connInfo;
	private Connection conn;
	private Connection batchConn;
	private Statement mStm;
	private ResultSet mRes;
	
	static final int MUST_BE_LOGGED_ON_TO_SERVER = 17430;
	static final int CLOSED_CONNECTION = 17008;
	static final int UNIQUE_KEY_DUPLICATE=1062;
	static final int INTER_TIMEOUT=0;
	static public volatile boolean exit = false;
	static private int SLEEP_MILISECOND = 10000;
	
	public Connection getBatchConnection(){
		return batchConn;
	}
	
	public Connection getNormalConnection(){
		return conn;
	}
	public DbExecutor(DbConnectInfo connInfo) throws Exception{
		this.connInfo=connInfo;
		this.init(false);
		this.init(true);
	}
	
	public void close() {
		this.tryCloseConn(this.conn);
		this.tryCloseConn(this.batchConn);
	}
	
	public void closeMStm(){
		try {
			if (mStm != null || !mStm.isClosed()) {
				mStm.close();
			}
		} catch (Exception e) { }
	}
	
	public void closeMRes(){
		try {
			if (mRes != null || !mRes.isClosed()) {
				mRes.close();
			}
		} catch (Exception e) { }
	}
	
	public ResultSet safeSelect(String sql) throws Exception {
		boolean retry=false;
		do {
			try {
				mStm=this.conn.createStatement();
				//System.out.println(mStm.toString());
				mRes = mStm.executeQuery(sql);
				//System.out.println("execute sql query "+sql);
				break;
			} catch (SQLException e){
				retry=this.dealException(e, this.conn.isClosed(),false);
			}
		} while (retry);
		return mRes;
	}
	
	/*
	public synchronized ResultSet safeSelect3(String sql) throws Exception {
		boolean retry=false;
		do {
			try {
				mStm=this.createStatement();
				mRes = mStm.executeQuery(sql);
				break;
			} catch (SQLException e){
				retry=this.dealException(e, this.conn.isClosed(),false);
			}
		} while (retry);
		return mRes;
	}
	
	public synchronized ResultSet safeSelect2(String sql) throws Exception {
		ResultSet rs = null;Statement stmt=null;boolean retry=false;
		ResultSet ret=null;
		do {
			try {
				stmt=this.createStatement();
				rs = stmt.executeQuery(sql);
				ret=this.fillRowSet(rs);
				break;
			} catch (SQLException e){
				retry=this.dealException(e, this.conn.isClosed(),false);
			} finally{
				this.closeResultSet(rs);
				this.closeStatement(stmt);
			}
		} while (retry);
		return ret;
	}
	*/
	public synchronized Long selectLong(String sql) throws Exception {
		Long ret=-1l;
		ResultSet rs = this.safeSelect(sql);
		if(rs!=null&&rs.next()){
			ret=rs.getLong(1);
		}
		return ret;
	}
	
	public synchronized String[] selectStrings(String sql,int columnNum) throws Exception {
		String[] rets=new String[columnNum];
		ResultSet rs = this.safeSelect(sql);
		if(rs!=null&&rs.next()){
			for(int i=0;i<columnNum;i++){
				rets[i]=rs.getString(i+1);	
			}
		}
		return rets;
	}
	
	

	public synchronized long safeInsertReturnID(String sql) throws Exception {
		Statement stmt=null;ResultSet rs=null;boolean retry=false;
		long generatedID=-1;
		do {
			try {
				stmt=this.createStatement();
				stmt.execute(sql);
				rs=stmt.getGeneratedKeys();					
				if(rs.next()){
					generatedID=rs.getLong(1);
				}
				break;
			} catch (SQLException e){
				retry=this.dealException(e,this.conn.isClosed(),false);
			} finally{
				this.closeResultSet(rs);
				this.closeStatement(stmt);
			}
		} while (retry);
		return generatedID;
	}

	public synchronized void safeInsert(String sql) throws Exception {
		Statement stmt=null;boolean retry=false;
		do {
			try {
				stmt=this.createStatement();
				stmt.execute(sql);
				break;
			} catch (SQLException e){
				retry=this.dealException(e,this.conn.isClosed(),false);
			} finally{
				this.closeStatement(stmt);
			}
		} while (retry);
	}

	public synchronized int safeUpdate(String sql) throws Exception {
		Statement stmt=null;boolean retry=false;int ret=-1;
		do {
			try {
				stmt=this.createStatement();
				ret=stmt.executeUpdate(sql);
				break;
			} catch (SQLException e){
				retry=this.dealException(e, this.conn.isClosed(),false);
			} finally{
				this.closeStatement(stmt);
			} 
		} while (retry);
		return ret;
	}
	
	public synchronized int[] safeInsertOrUpdate(String psSQL, List<Object[]> fieldValuesList)
			throws Exception {
		PreparedStatement pstat = null;int[] ret=null;boolean retry=false;
		//do 
		{
			try {
				pstat = this.batchConn.prepareStatement(psSQL);
				for (int index = 0; index < fieldValuesList.size(); index++) {
					Object[] fieldValues = fieldValuesList.get(index);
					int len = 0;
					len = fieldValues.length;
					for (int i = 0; i < len; i++) {
						if (fieldValues[i] == null) {
							pstat.setObject(i + 1, null);
						} else {
							pstat.setObject(i + 1, fieldValues[i]);
						}
					}
					pstat.addBatch();
				}
				System.out.println("for loop done!");
				ret= pstat.executeBatch();
				System.out.println("execute batch done!");
				this.batchConn.commit();
				System.out.println("commit done!");
				//break;
			} catch (SQLException e){
				retry=this.dealException(e, this.batchConn.isClosed(),true);
			} finally{
				this.closeStatement(pstat);
			}
		}// while (retry);
		return ret;
	}
	
	private boolean dealException(SQLException e,boolean isClosed,boolean isBatch) throws Exception{
		if(isClosed || e.getErrorCode()==INTER_TIMEOUT || 
				e.getErrorCode()==MUST_BE_LOGGED_ON_TO_SERVER ||
				e.getErrorCode()==CLOSED_CONNECTION){//be closed,need reconnect
			e.printStackTrace();
			System.out.println("ConnectionException! Reconnect after sleep "+SLEEP_MILISECOND+" ms...");
			Thread.sleep(SLEEP_MILISECOND);
			this.init(isBatch);
			return true;
		}else if(e.getErrorCode()==UNIQUE_KEY_DUPLICATE){
			e.printStackTrace();
			return false;
		}else{
			throw e;
		}
	}
	
	private void tryCloseConn(Connection con){
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (Exception e) { }
	}
	
	private void init(boolean isBatch) throws SQLException {
		if(isBatch){
			this.tryCloseConn(this.batchConn);
			this.batchConn = this.getConnection();
			this.batchConn.setAutoCommit(false);
		}else{
			this.tryCloseConn(this.conn);
			this.conn=this.getConnection();
		}
	}

	private Connection getConnection() throws SQLException{
		System.out.println("get con from "+this.connInfo.url);
		return DriverManager.getConnection(this.connInfo.url,
				this.connInfo.usr, this.connInfo.pwd);
	}
	
	private void closeResultSet(ResultSet rs){
		if(rs!=null){
			try{
				rs.close();
			}catch(Exception e){
			}
			rs=null;
		}
	}
	
	private void closeStatement(Statement st){
		if(st!=null){
			try{
				st.close();
			}catch(Exception e){
			}
			st=null;
		}
	}
	
	private Statement createStatement() throws SQLException{
		//return this.conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
		return this.conn.createStatement();
	}
	
	private CachedRowSetImpl fillRowSet(ResultSet rs) throws SQLException{
		if(rs!=null){
			CachedRowSetImpl rowset=new CachedRowSetImpl();
			rowset.populate(rs);
			return rowset;
		}else{
			return null;
		}
	}
}
