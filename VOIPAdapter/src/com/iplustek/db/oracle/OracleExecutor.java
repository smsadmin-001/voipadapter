package com.iplustek.db.oracle;

import com.iplustek.utils.PropertyLoader;
import java.sql.ResultSet;
import com.iplustek.db.DbConnectInfo;
import com.iplustek.db.DbExecutor;

public class OracleExecutor extends DbExecutor{
	static {
		try {
			PropertyLoader pl = PropertyLoader.getInstance();
			Class.forName(pl.getM_oracle_driver());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public OracleExecutor(DbConnectInfo connInfo) throws Exception {
		super(connInfo);
	}
	public static void main(String[] arhgs) throws Exception{
		DbConnectInfo connInfo = new DbConnectInfo("jdbc:oracle:thin:@//192.168.0.131:1521/orcl"//+"?autoReconnect=true"
						,"smsadmin","c437");
		OracleExecutor or = new OracleExecutor(connInfo);
		String sql = "select * from t_iv_voice_record_system where rownum < 1000";
		ResultSet rs = or.safeSelect(sql);
		while(rs.next()){
			System.out.println(rs.getString("sfilename"));
		}
	}
}