package com.iplustek.bean;
/*
 *基础引擎结果表
 *SR_SIMPLE_ENGINE_RESULT
 *file_name 唯一标示符
 *speech_id bigint 语音记录表中的id
 *engine_type tinyint 引擎类型
 *						1- lid
 *						2-gid
 *						3-vad
 *result 识别结果
 *score 识别置信度
 *sid_type 声道类型 1-左声道； 2-右声道
 *create_time 记录插入时间
 */
public class SimpleEngineResult {
	private String m_file_name;
	private int m_engine_type;
	private String m_result;
	private float m_score;
	private int m_side_type;
	private String m_create_time;
	
	public String getM_file_name() {
		return m_file_name;
	}

	public void setM_file_name(String file_name) {
		m_file_name = file_name;
	}

	public int getM_engine_type() {
		return m_engine_type;
	}

	public void setM_engine_type(int engine_type) {
		m_engine_type = engine_type;
	}

	public String getM_result() {
		return m_result;
	}

	public void setM_result(String result) {
		m_result = result;
	}

	public float getM_score() {
		return m_score;
	}

	public void setM_score(float score) {
		m_score = score;
	}

	public int getM_side_type() {
		return m_side_type;
	}

	public void setM_side_type(int side_type) {
		m_side_type = side_type;
	}

	public String getM_create_time() {
		return m_create_time;
	}

	public void setM_create_time(String create_time) {
		m_create_time = create_time;
	}

	public SimpleEngineResult(){
		m_result="拒识";
		m_score=0;
		m_side_type=1;
	}
	
	public SimpleEngineResult(String file_name, int engine_type, 
			int result, float score, int side_type, String create_time){
		m_file_name = file_name;
		m_engine_type = engine_type;
		m_score = score;
		m_side_type = side_type;
		m_create_time=create_time;
		/*
		 * 根据引擎类型 判断不同的检测结果
		 * vad(4) 0：非话音；1：话音
		 * gid(2) 0：男；1：女
		 */
		if(engine_type==4){
			if(result==0)
				m_result="非话音";
			else if(result==1)
				m_result="话音";
			else
				m_result="拒识";
		}else if(engine_type==2){
			if(result == 0)
				m_result = "男";
			else if(result == 1)
				m_result = "女";
			else if(result == 2)
				m_result = "混合";
			else
				m_result = "拒识";
		}
	}
	
	public SimpleEngineResult(String file_name, int engine_type, 
			String result, float score, int side_type, String create_time){
		/*
		 *调用此方法 引擎类型应该为lid(1) 
		 */
		m_file_name = file_name;
		m_engine_type = engine_type;
		m_result = result;
		m_score = score;
		m_side_type = side_type;
		m_create_time=create_time;
	}

}
