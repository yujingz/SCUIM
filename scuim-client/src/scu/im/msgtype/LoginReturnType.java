package scu.im.msgtype;

import java.io.Serializable;

import scu.im.bean.GroupInfo;

public class LoginReturnType implements Serializable {

	private static final long serialVersionUID = 4665456133096746022L;
	private Boolean result;
	private String logIp;
	private UserInfoResponseType userInfo;
	private GroupInfo gInfo;

	public String getLogIp() {
		return logIp;
	}

	public void setLogIp(String logIp) {
		this.logIp = logIp;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public UserInfoResponseType getUserInfo() {
		return userInfo;
	}

	public void setUserInfoResponseType(UserInfoResponseType userInfo) {
		this.userInfo = userInfo;
	}

	public GroupInfo getGInfo() {
		return gInfo;
	}

	public void setGInfo(GroupInfo info) {
		gInfo = info;
	}
}
