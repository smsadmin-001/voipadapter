package com.iplustek.utils;

import com.lily.prop.PropConf;

public class PropertyLoader {
	private static PropertyLoader instance;
	private PropConf pconf = PropConf.getInstance();
	//properties for nosql
	private String m_nosql_driver;
	private String m_nosql_url;
	private String m_nosql_username;
	private String m_nosql_password;
	private String m_nosql_table_name;
	private String m_nosql_id_name;
	private String m_nosql_process_status_name;
	
	//properties for oracle
	private String m_oracle_driver;
	private String m_oracle_url;
	private String m_oracle_username;
	private String m_oracle_password;
	private String m_oracle_table_name;
	private String m_oracle_id_name;
	private String m_oracle_process_status_name;
		
	//properties for gbase
	private String m_gbsql_driver;
	private String m_gbsql_url;
	private String m_gbsql_username;
	private String m_gbsql_password;
		
		
	//properties for common 
	private String m_undone_status;
	private String m_doing_status;
	private String m_done_succ_status;
	private String m_done_fail_status;
	private String m_down_fail_status;
	private boolean m_syn_enable;
	private boolean m_send_enable;
	private boolean m_get_enable;
	private int m_batch_way;	//0:no batch; 1:stms batch; 2:pst batch
	private boolean m_tract;
	private int m_code_rate;
	private int m_code_format;
	
	//propertise for download data
	private String m_get_ip;
	private int m_get_port;
	private String m_save_path;
	private int m_get_interval;
	private int m_get_steps;
	private int m_get_thread_num;
	
	//properties for send data
	private String m_send_ip;
	private int m_send_port;
	private int m_send_interval;
	private int m_send_steps;
	private String m_send_status_name;
	private String m_unsend_status;
	private String m_have_sent_status;

	public int getM_get_interval() {
		return m_get_interval;
	}

	public String getM_nosql_driver() {
		return m_nosql_driver;
	}

	public String getM_nosql_url() {
		return m_nosql_url;
	}

	public String getM_nosql_username() {
		return m_nosql_username;
	}

	public String getM_nosql_password() {
		return m_nosql_password;
	}

	public String getM_nosql_table_name() {
		return m_nosql_table_name;
	}

	public String getM_nosql_id_name() {
		return m_nosql_id_name;
	}

	public String getM_nosql_process_status_name() {
		return m_nosql_process_status_name;
	}

	public String getM_gbsql_driver() {
		return m_gbsql_driver;
	}

	public void setM_gbsql_driver(String m_gbsql_driver) {
		this.m_gbsql_driver = m_gbsql_driver;
	}

	public String getM_gbsql_url() {
		return m_gbsql_url;
	}

	public void setM_gbsql_url(String m_gbsql_url) {
		this.m_gbsql_url = m_gbsql_url;
	}

	public String getM_gbsql_username() {
		return m_gbsql_username;
	}

	public void setM_gbsql_username(String m_gbsql_username) {
		this.m_gbsql_username = m_gbsql_username;
	}

	public String getM_gbsql_password() {
		return m_gbsql_password;
	}

	public void setM_gbsql_password(String m_gbsql_password) {
		this.m_gbsql_password = m_gbsql_password;
	}

	public String getM_oracle_driver() {
		return m_oracle_driver;
	}

	public String getM_oracle_url() {
		return m_oracle_url;
	}

	public String getM_oracle_username() {
		return m_oracle_username;
	}

	public String getM_oracle_password() {
		return m_oracle_password;
	}

	public String getM_oracle_table_name() {
		return m_oracle_table_name;
	}

	public String getM_oracle_id_name() {
		return m_oracle_id_name;
	}

	public String getM_oracle_process_status_name() {
		return m_oracle_process_status_name;
	}

	public String getM_undone_status() {
		return m_undone_status;
	}

	public String getM_doing_status() {
		return m_doing_status;
	}

	public String getM_done_succ_status() {
		return m_done_succ_status;
	}

	public String getM_done_fail_status() {
		return m_done_fail_status;
	}

	public String getM_down_fail_status() {
		return m_down_fail_status;
	}
	
	public boolean isM_syn_enable() {
		return m_syn_enable;
	}

	public void setM_syn_enable(boolean m_syn_enable) {
		this.m_syn_enable = m_syn_enable;
	}

	public boolean isM_send_enable() {
		return m_send_enable;
	}

	public boolean isM_get_enable() {
		return m_get_enable;
	}

	public int getM_batch_way() {
		return m_batch_way;
	}

	public boolean isM_tract() {
		return m_tract;
	}

	public String getM_get_ip() {
		return m_get_ip;
	}

	public int getM_get_port() {
		return m_get_port;
	}

	public String getM_save_path() {
		return m_save_path;
	}

	public int getM_get_steps() {
		return m_get_steps;
	}
	
	public int getM__get_thread_num(){
		return m_get_thread_num;
	}

	public String getM_send_ip() {
		return m_send_ip;
	}

	public int getM_send_port() {
		return m_send_port;
	}

	public int getM_send_interval() {
		return m_send_interval;
	}

	public int getM_send_steps() {
		return m_send_steps;
	}
	
	public String getM_send_status_name() {
		return m_send_status_name;
	}

	public String getM_unsend_status() {
		return m_unsend_status;
	}

	public String getM_have_sent_status() {
		return m_have_sent_status;
	}

	public int getM_code_rate() {
		return m_code_rate;
	}

	public int getM_code_format() {
		return m_code_format;
	}
	

	public static synchronized PropertyLoader getInstance() throws Exception{
		if(instance==null){
			instance = new PropertyLoader();
		}
		return instance;
	}
	private PropertyLoader() throws Exception{
		pconf.addProps("../etc/VOIPAdapter.properties");
		
		m_nosql_driver = pconf.getStringValue("nosql_driver");
		m_nosql_url = pconf.getStringValue("nosql_url");
		m_nosql_username = pconf.getStringValue("nosql_username");
		m_nosql_password = pconf.getStringValue("nosql_password");
		m_nosql_table_name = pconf.getStringValue("nosql_table_name");
		m_nosql_id_name = pconf.getStringValue("nosql_id_name");
		m_nosql_process_status_name = pconf.getStringValue("nosql_process_status_name");
		
		m_gbsql_driver = pconf.getStringValue("gbsql_driver");
		m_gbsql_url = pconf.getStringValue("gbsql_url");
		m_gbsql_username = pconf.getStringValue("gbsql_username");
		m_gbsql_password = pconf.getStringValue("gbsql_password");
		
		
		m_oracle_driver = pconf.getStringValue("oracle_driver");
		m_oracle_url = pconf.getStringValue("oracle_url");
		m_oracle_username = pconf.getStringValue("oracle_username");
		m_oracle_password = pconf.getStringValue("oracle_password");
		m_oracle_table_name = pconf.getStringValue("oracle_table_name");
		m_oracle_id_name = pconf.getStringValue("oracle_id_name");
		m_oracle_process_status_name = pconf.getStringValue("oracle_process_status_name");
		
		m_undone_status = pconf.getStringValue("undone_status");
		m_doing_status = pconf.getStringValue("doing_status");
		m_done_succ_status = pconf.getStringValue("done_succ_status");
		m_done_fail_status = pconf.getStringValue("done_fail_status");
		m_down_fail_status = pconf.getStringValue("down_fail_status");
		m_syn_enable = pconf.getBooleanValue("syn_enable");
		m_send_enable = pconf.getBooleanValue("send_enable");
		m_get_enable = pconf.getBooleanValue("get_enable");
		m_batch_way = pconf.getIntValue("batch_way");
		m_tract = pconf.getBooleanValue("tract");
		m_code_rate = pconf.getIntValue("code_rate");
		m_code_format = pconf.getIntValue("code_format");
		
		m_get_ip = pconf.getStringValue("get_ip");
		m_get_port = pconf.getIntValue("get_port");
		m_save_path = pconf.getStringValue("save_path");
		m_get_interval = pconf.getIntValue("get_interval");
		m_get_steps = pconf.getIntValue("get_steps");
		m_get_thread_num = pconf.getIntValue("get_thread_num");
		
		m_send_ip = pconf.getStringValue("send_ip");
		m_send_port = pconf.getIntValue("send_port");
		m_send_interval = pconf.getIntValue("send_interval");
		m_send_steps = pconf.getIntValue("send_steps");
		m_send_status_name = pconf.getStringValue("send_status_name");
		m_unsend_status = pconf.getStringValue("unsend_status");
		m_have_sent_status = pconf.getStringValue("have_sent_status");
	}
	
	public static void main(String[] args) throws Exception{
		PropertyLoader pl = PropertyLoader.getInstance();
		System.out.println(pl);
		System.out.println(pl.getM_batch_way());
		System.out.println(pl.getM_doing_status());
		System.out.println(pl.getM_done_fail_status());
		System.out.println(pl.getM_done_succ_status());
		System.out.println(pl.getM_down_fail_status());
		System.out.println(pl.getM_get_interval());
		System.out.println(pl.getM_get_ip());
		System.out.println(pl.getM_get_port());
		System.out.println(pl.getM_nosql_driver());
		System.out.println(pl.getM_nosql_id_name());
		System.out.println(pl.getM_nosql_password());
		System.out.println(pl.getM_nosql_process_status_name());
		System.out.println(pl.getM_nosql_table_name());
		System.out.println(pl.getM_nosql_url());
		System.out.println(pl.getM_nosql_username());
		
		System.out.println("*"+pl.getM_gbsql_driver());
		System.out.println("*"+pl.getM_gbsql_url());
		System.out.println("*"+pl.getM_gbsql_username());
		System.out.println("*"+pl.getM_gbsql_password());
		
		System.out.println(pl.getM_oracle_driver());
		System.out.println(pl.getM_oracle_id_name());
		System.out.println(pl.getM_oracle_password());
		System.out.println(pl.getM_oracle_process_status_name());
		System.out.println(pl.getM_oracle_table_name());
		System.out.println(pl.getM_oracle_url());
		System.out.println(pl.getM_oracle_username());
		System.out.println(pl.getM_save_path());
		System.out.println(pl.getM_send_interval());
		System.out.println(pl.getM_send_ip());
		System.out.println(pl.getM_send_port());
		System.out.println(pl.getM_send_steps());
		System.out.println(pl.getM_undone_status());
		System.out.println("***"+pl.isM_syn_enable());
		System.out.println(pl.isM_get_enable());
		System.out.println(pl.isM_send_enable());
		
		System.out.println(pl.getM__get_thread_num());
	}

}
