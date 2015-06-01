package com.yeahmobi.yscheduler.storage;

import java.io.InputStream;
/**
 * 
 * @author Abel.Cui
 *
 */
public class FileEntry {
	String fileName;
	InputStream inputStream;
	
	public FileEntry(String fileName, InputStream inputStream) {
		this.fileName = fileName;
		this.inputStream = inputStream;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
}
