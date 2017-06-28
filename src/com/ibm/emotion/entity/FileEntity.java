package com.ibm.emotion.entity;

/**
 * 呼叫中心文件实体
 */
public class FileEntity {

	private int id;
	
	private String fileName;
	
	private double avgValence;
	
	private String audioTimeStr;
	
	private int audioTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public double getAvgValence() {
		return avgValence;
	}

	public void setAvgValence(double avgValence) {
		this.avgValence = avgValence;
	}

	public String getAudioTimeStr() {
		return audioTimeStr;
	}

	public void setAudioTimeStr(String audioTimeStr) {
		this.audioTimeStr = audioTimeStr;
	}

	public int getAudioTime() {
		return audioTime;
	}

	public void setAudioTime(int audioTime) {
		this.audioTime = audioTime;
	}
	
}
