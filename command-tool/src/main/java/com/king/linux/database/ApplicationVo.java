package com.king.linux.database;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class ApplicationVo  implements Serializable,Comparable<ApplicationVo> {
	/**
	 * @Fields serialVersionUID TODO <span>用一句话描述这个变量表示什么</span>
	 */
	private static final long serialVersionUID = 1L;

	public static ApplicationVo valueOf(String key, Map<String, Object> value) {
		ApplicationVo applicationVo = new ApplicationVo();
		applicationVo.key = key;
		applicationVo.value = value;
		applicationVo.time = new Date().getTime();
		return applicationVo;
	}

	private String key;

	private Map<String, Object> value;

	private Long time;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, Object> getValue() {
		return value;
	}

	public void setValue(Map<String, Object> value) {
		this.value = value;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	@Override
	public int compareTo(ApplicationVo arg0) {
		return arg0.getTime() > this.time ? 0 : -1;
	}
}
