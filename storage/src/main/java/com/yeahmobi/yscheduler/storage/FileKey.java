package com.yeahmobi.yscheduler.storage;
/**
 * 
 * @author Abel.Cui
 *
 */
public class FileKey {

	String nameSpace;
	String key;
	public FileKey(String nameSpace, String key) {
		this.nameSpace = nameSpace;
		this.key = key;
	}
	public String getNameSpace() {
		return nameSpace;
	}
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

}
