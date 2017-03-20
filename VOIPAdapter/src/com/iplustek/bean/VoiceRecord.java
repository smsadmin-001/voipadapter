package com.iplustek.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 声音记录表
 * T_IV_VOICE_RECORD_SYSTEM
 */
public class VoiceRecord {

	private static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//自增 主键
	private long m_NID;
	
	//文件名 不带路径 同时也是NoSQL表的主键
	private String m_SFILENAME;
	
	//创建时间
	private String m_STARTTIME;
	
	//话音识别结果 0：非话音 1：话音
	private int m_NVADRESULT;
	
	//话音识别结果置信度
	private float m_NVADSCORE;
	
	//性别识别结果  0：男 1：女 2：混合
	private int m_NGIDRESULT;
	
	//性别结果置信度
	private float m_NGIDSCORE;
	
	//语种识别结果 格式是LANGNAME_SCORE，例如”英语_95”
	private String m_SLIDRESULT;

	/*语音信息处理状态
	 * INIT(0), QUEUEING(1), SUCC(2), FAIL(3), PART_REUSLT(4);
	 * succ和fail是全部完成的状态 
	 */
	private int m_NSPEECH_PROCESS_STATUS;

	public int getNSPEECH_PROCESS_STATUS() {
		return m_NSPEECH_PROCESS_STATUS;
	}

	public void setNSPEECH_PROCESS_STATUS(int nSPEECH_PROCESS_STATUS) {
		m_NSPEECH_PROCESS_STATUS = nSPEECH_PROCESS_STATUS;
	}

	public String getSFILENAME() {
		return m_SFILENAME;
	}

	public void setSFILENAME(String sFILENAME) {
		m_SFILENAME = sFILENAME;
	}

	public String getSTARTTIME() {
		return m_STARTTIME;
	}

	public void setSTARTTIME(String sTARTTIME) {
		m_STARTTIME = sTARTTIME;
	}

	public int getNVADRESULT() {
		return m_NVADRESULT;
	}

	public void setNVADRESULT(int nVADRESULT) {
		m_NVADRESULT = nVADRESULT;
	}

	public float getNVADSCORE() {
		return m_NVADSCORE;
	}

	public void setNVADSCORE(float nVADSCORE) {
		m_NVADSCORE = nVADSCORE;
	}

	public int getNGIDRESULT() {
		return m_NGIDRESULT;
	}

	public void setNGIDRESULT(int nGIDRESULT) {
		m_NGIDRESULT = nGIDRESULT;
	}

	public float getNGIDSCORE() {
		return m_NGIDSCORE;
	}

	public void setNGIDSCORE(float nGIDSCORE) {
		m_NGIDSCORE = nGIDSCORE;
	}

	public String getSLIDRESULT() {
		return m_SLIDRESULT;
	}

	public void setSLIDRESULT(String sLIDRESULT) {
		m_SLIDRESULT = sLIDRESULT;
	}
	
	public long getNID() {
		return m_NID;
	}
	
	public VoiceRecord(){
		m_SFILENAME=null;
		m_STARTTIME= date_format.format(new Date());
		m_NVADRESULT=-1;
		m_NVADSCORE=0;
		m_NGIDRESULT=-1;
		m_NGIDSCORE=0;
		m_SLIDRESULT="";
		m_NSPEECH_PROCESS_STATUS=0;
	}
	
	public VoiceRecord(String SFILENAME, String STARTTIME){
		m_SFILENAME=SFILENAME;
		m_STARTTIME= STARTTIME;
		m_NVADRESULT=-1;
		m_NVADSCORE=0;
		m_NGIDRESULT=-1;
		m_NGIDSCORE=0;
		m_SLIDRESULT="";
		m_NSPEECH_PROCESS_STATUS=0;
	}
	
	public VoiceRecord(long NID, String SFILENAME, String STARTTIME, int NVADRESULT, float NVADSCORE, int NGIDRESULT, float NGIDSCORE, String SLIDRESULT){
		m_NID=NID;
		m_SFILENAME=SFILENAME;
		m_STARTTIME=STARTTIME;
		m_NVADRESULT=NVADRESULT;
		m_NVADSCORE=NVADSCORE;
		m_NGIDRESULT=NGIDRESULT;
		m_NGIDSCORE=NGIDSCORE;
		m_SLIDRESULT=SLIDRESULT;
	}
}
