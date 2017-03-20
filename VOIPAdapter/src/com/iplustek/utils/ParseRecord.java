package com.iplustek.utils;

import java.util.List;

import com.iplustek.bean.*;
import java.util.ArrayList;
public abstract class ParseRecord {
	/*
	 * 将一条声音 记录解析成三条简单查询结果
	 */
	private static List<SimpleEngineResult> ParseSingleResult(VoiceRecord voiceRecord){
		List<SimpleEngineResult> resultLst = new ArrayList<SimpleEngineResult>();
		String file_name = voiceRecord.getSFILENAME();
		int vad_result = voiceRecord.getNVADRESULT();
		float vad_score = voiceRecord.getNVADSCORE();
		int gid_result = voiceRecord.getNGIDRESULT();
		float gid_score = voiceRecord.getNGIDSCORE();
		String lid_out = voiceRecord.getSLIDRESULT();
		String lid_result = getLidResult(lid_out);
		float lid_score = getLidScore(lid_out);
		String create_time = voiceRecord.getSTARTTIME();
		/*
		 * vad 4
		 * gid 2
		 * lid 1
		 */
		SimpleEngineResult vadResult = new SimpleEngineResult(file_name,4,vad_result,vad_score,1,create_time);
		SimpleEngineResult gidResult = new SimpleEngineResult(file_name,2,gid_result,gid_score,1,create_time);
		SimpleEngineResult lidResult = new SimpleEngineResult(file_name,1,lid_result,lid_score,1,create_time);
		resultLst.add(vadResult);
		resultLst.add(gidResult);
		resultLst.add(lidResult);
		return resultLst;
	}
	
	/*
	 * 将一组声音记录解析成一组简单查询结果
	 */
	public static List<SimpleEngineResult> ParseMutilResults(List<VoiceRecord> recordLst){
		List<SimpleEngineResult> resultsLst = new ArrayList<SimpleEngineResult>();
		for(int i=0;i<recordLst.size();i++){
			List<SimpleEngineResult> singleLst = ParseSingleResult(recordLst.get(i));
			resultsLst.addAll(singleLst);
		}
		return resultsLst;
	}

	/*
	 * lid_out 语种识别结果 格式是LANGNAME_SCORE，例如”英语_95”
	 * getLidScore 用于将SCORE提取出来
	 * getLidResult 用于将LANGNAME提取出来
	 */
	private static float getLidScore(String lid_out) {
		if(lid_out.split("_").length>1){
			return Integer.parseInt(lid_out.split("_")[1].trim());
		}
		return 0;
	}

	private static String getLidResult(String lid_out) {
		String lidres = lid_out.split("_")[0];
		if(lidres == null || lidres.trim().equals(""))
			return "拒识";
		return lid_out.split("_")[0];
	}
	/*
	public static void main(String[] args){
		String s11=new String("aaa");
		String s12="aaa";
		System.out.print(s11.equals("aaa"));
		System.out.print(s11=="aaa");
		System.out.print(s11.equals(s12));
		System.out.println(s11==s12);
		//return;
		
		String s = "拒识";
		String s1 = getLidResult(s);
		float s2 = getLidScore(s);
		
		//System.out.println(s1+"&&&"+s2);
		
		VoiceRecord vr = new VoiceRecord("aa", dateFormat.format(new Date()));
		vr.setNGIDRESULT(2);
		vr.setNGIDSCORE((float) 88.8);
		vr.setNVADRESULT(1);
		vr.setNVADSCORE((float) 77.7);
		vr.setSLIDRESULT(" 		 ");
		List<VoiceRecord> recordLst = new  ArrayList<VoiceRecord>();
		recordLst.add(vr);
		List<SimpleEngineResult> resultLst = ParseMutilResults(recordLst);
		for(int i =0;i<resultLst.size();i++){
			SimpleEngineResult sr = resultLst.get(i);
			System.out.println(sr.getM_engine_type()+"***"+sr.getM_result()+":"+sr.getM_score());
		}
		
	}
	*/
}
