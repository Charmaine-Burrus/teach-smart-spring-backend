package com.claim.model;

public class TSClass {
	
	private long classId;
	private String className;
	private String accessToken;
	
	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String toString() {
		return "Class [classId=" + classId + ", className=" + className + "]";
	}

}
