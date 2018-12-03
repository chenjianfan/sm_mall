package cn.woshicheng.core.entity;

import java.sql.Timestamp;

import cn.woshicheng.common.util.BaseEntity;

public class SyslogEntitiy extends BaseEntity {
	private int id;
	private String loginfo;
	private String name;
	private String params;
	private String methodName;
	private String ip;
	private Timestamp createdate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLoginfo() {
		return loginfo;
	}

	public void setLoginfo(String loginfo) {
		this.loginfo = loginfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Timestamp getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Timestamp createdate) {
		this.createdate = createdate;
	}

}
