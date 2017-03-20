package com.iplustek.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/*
 * 用于文件的读写操作
 */
public abstract class FileHelper {
	/*
	 * 从传入的路径读取文件，读入到byte数组中，返回该数组
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
		
		//确保读取结束
		if(offset!=buffer.length){
			fis.close();
			throw new IOException("Could not completely read file:"+file.getName());
		}
		fis.close();
		return buffer;
	}
	
	
	
	/*
	 * 将byte数组写入到特定路径下的文件中
	 */
	public static void createFile(String path, byte[] content) throws Exception{
		
		File file = checkExist(path);
		FileOutputStream fos = new FileOutputStream(file.getPath());
		fos.write(content);
		fos.close();
	}
	
	/*
	 * 辅助方法，判断特定路径下的文件是否存在
	 * 若文件不存在，则判断其父文件夹是否存在，不存在则创建新的文件夹
	 * 创建新的文件，并将该文件句柄返回
	 */
	private static File checkExist(String path) throws Exception{
		File file = new File(path);
		if(file.exists()){
			System.out.println("文件存在");
		}else{
			File file2 = new File(file.getParent());
			if(!file2.exists()){
				file2.mkdir();
				System.out.println("文件夹不存在，已创建");
			}
			file.createNewFile();
			System.out.println("文件不存在，已创建");
		}
		return file;
	}
}
