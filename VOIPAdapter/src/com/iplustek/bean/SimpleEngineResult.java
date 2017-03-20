package com.iplustek.bean;
/*
 *������������
 *SR_SIMPLE_ENGINE_RESULT
 *file_name Ψһ��ʾ��
 *speech_id bigint ������¼���е�id
 *engine_type tinyint ��������
 *						1- lid
 *						2-gid
 *						3-vad
 *result ʶ����
 *score ʶ�����Ŷ�
 *sid_type �������� 1-�������� 2-������
 *create_time ��¼����ʱ��
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
		m_result="��ʶ";
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
		 * ������������ �жϲ�ͬ�ļ����
		 * vad(4) 0���ǻ�����1������
		 * gid(2) 0���У�1��Ů
		 */
		if(engine_type==4){
			if(result==0)
				m_result="�ǻ���";
			else if(result==1)
				m_result="����";
			else
				m_result="��ʶ";
		}else if(engine_type==2){
			if(result == 0)
				m_result = "��";
			else if(result == 1)
				m_result = "Ů";
			else if(result == 2)
				m_result = "���";
			else
				m_result = "��ʶ";
		}
	}
	
	public SimpleEngineResult(String file_name, int engine_type, 
			String result, float score, int side_type, String create_time){
		/*
		 *���ô˷��� ��������Ӧ��Ϊlid(1) 
		 */
		m_file_name = file_name;
		m_engine_type = engine_type;
		m_result = result;
		m_score = score;
		m_side_type = side_type;
		m_create_time=create_time;
	}

}
