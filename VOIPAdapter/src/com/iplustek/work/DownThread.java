package com.iplustek.work;
import java.util.List;

public class DownThread extends Thread {
	private String thread_name;
	private List<DownWav> downList= null;
	
	public DownThread(List<DownWav>downList, String thread_name){
		this.downList = downList;
		this.thread_name = thread_name;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(thread_name + " start downing... ");
		DownWav work = null;
		while(true){
				if(downList!=null && !downList.isEmpty()){
					synchronized (downList) {
					work = downList.remove(0);
					}
					work.download(thread_name);
				}else{
					System.out.println("All finish");
					break;
				}
			}
	}
	
	

}
