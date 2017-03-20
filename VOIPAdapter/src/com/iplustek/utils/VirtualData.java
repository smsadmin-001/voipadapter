package com.iplustek.utils;

import java.io.IOException;
import java.util.List;

import com.iplustek.bean.*;

/*
 * ģ����ݲ����ɾ��
 */
public abstract class VirtualData {
	static int cou = 0;
	static String[] wav = new String[10];
	static{
		wav[0] = new String("/u02/WAVDATA/online_data/����_04626.wav");
		wav[1] = new String("/u02/WAVDATA/online_data/����_11935.wav");
		wav[2] = new String("/u02/WAVDATA/online_data/����_00255.wav");
		wav[3] = new String("/u02/WAVDATA/online_data/����_01101.wav");
		wav[4] = new String("/u02/WAVDATA/online_data/��ͨ��_10001.wav");
		wav[5] = new String("/u02/WAVDATA/online_data/��ͨ��_10002.wav");
		wav[6] = new String("/u02/WAVDATA/online_data/����_03244.wav");
		wav[7] = new String("/u02/WAVDATA/online_data/����_03497.wav");
		wav[8] = new String("/u02/WAVDATA/online_data/Ӣ��_00021.wav");	
		wav[9] = new String("/u02/WAVDATA/online_data/Ӣ��_00409.wav");
	}

	public static byte[] idfsReadFile2(String tablename, String key){
		byte[] arr=null;
		try {
			System.out.println("path is "+wav[cou]);
			arr = FileHelper.getContent(wav[cou]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if((cou++)==8){
			cou=0;
			return null;
		}
		return arr;
	}
	
	public static byte[] idfsReadFile(String tablename, String key){
		byte[] arr= new byte[2];
		for(int i=0;i<arr.length;i++){
			arr[i]=(byte)i;
		}
		return arr;
	}


	public static void showSendDataResult(List<SimpleEngineResult> resultsLst){

		System.out.println("Size:"+resultsLst.size());
		System.out.println("SR_SIMPLE_ENGINE_RESULT");
		
		System.out.println("no. \t file_name \t engine_type \t result \t score \t side_type \t create_time");
		
		for(int i=0;i<resultsLst.size();i++){
			System.out.print(i+"\t");
			System.out.print(resultsLst.get(i).getM_file_name()+"\t\t");
			System.out.print(resultsLst.get(i).getM_engine_type()+"\t");
			System.out.print(resultsLst.get(i).getM_result()+"\t\t");
			System.out.print(resultsLst.get(i).getM_score()+"\t\t");
			System.out.print(resultsLst.get(i).getM_side_type()+"\t");
			System.out.println(resultsLst.get(i).getM_create_time());
			System.out.println("------------------------------------------------------------------");
		}
}
	
	

}
