package com.iplustek.db.nosql;

import java.sql.ResultSet;

import com.iplustek.db.DbConnectInfo;
import com.iplustek.db.DbExecutor;
import com.iplustek.db.oracle.OracleExecutor;
import com.iplustek.utils.PropertyLoader;

public class NosqlExecutor extends DbExecutor{
	static {
		try {
			PropertyLoader pl = PropertyLoader.getInstance();
			Class.forName(pl.getM_nosql_driver());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public NosqlExecutor(DbConnectInfo connInfo) throws Exception {
		super(connInfo);
	}
	
	public static void main(String[] arhgs) throws Exception{
		DbConnectInfo connInfo = new DbConnectInfo("jdbc:mysql://192.168.0.124:3306/voip"//+"?autoReconnect=true"
						,"gbase","gbase20110531");
		OracleExecutor or = new OracleExecutor(connInfo);
		String sql = "select * from zrtp limit 1000";
		ResultSet rs = or.safeSelect(sql);
		while(rs.next()){
			System.out.println(rs.getString("file_name"));
		}
	}
}
