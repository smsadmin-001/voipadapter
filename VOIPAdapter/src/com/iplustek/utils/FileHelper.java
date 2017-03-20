package com.iplustek.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/*
 * �����ļ��Ķ�д����
 */
public abstract class FileHelper {
	/*
	 * �Ӵ����·����ȡ�ļ������뵽byte�����У����ظ�����
	 */
	public static byte[] getContent(String filePath) throws IOException{
		File file = new File(filePath);
		long fileSize = file.length();
		if(fileSize>Integer.MAX_VALUE){
			System.out.println("file too big");
			return null;
		}
		
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[(int)fileSize];
		int offset = 0;
		int numRead = 0;
		while(offset<buffer.length &&(numRead = fis.read(buffer,offset,buffer.length-offset))>=0){
			offset+=numRead;
		}
		
		//ȷ����ȡ����
		if(offset!=buffer.length){
			fis.close();
			throw new IOException("Could not completely read file:"+file.getName());
		}
		fis.close();
		return buffer;
	}
	
	
	
	/*
	 * ��byte����д�뵽�ض�·���µ��ļ���
	 */
	public static void createFile(String path, byte[] content) throws Exception{
		
		File file = checkExist(path);
		FileOutputStream fos = new FileOutputStream(file.getPath());
		fos.write(content);
		fos.close();
	}
	
	/*
	 * �����������ж��ض�·���µ��ļ��Ƿ����
	 * ���ļ������ڣ����ж��丸�ļ����Ƿ���ڣ��������򴴽��µ��ļ���
	 * �����µ��ļ����������ļ��������
	 */
	private static File checkExist(String path) throws Exception{
		File file = new File(path);
		if(file.exists()){
			System.out.println("�ļ�����");
		}else{
			File file2 = new File(file.getParent());
			if(!file2.exists()){
				file2.mkdir();
				System.out.println("�ļ��в����ڣ��Ѵ���");
			}
			file.createNewFile();
			System.out.println("�ļ������ڣ��Ѵ���");
		}
		return file;
	}
}
