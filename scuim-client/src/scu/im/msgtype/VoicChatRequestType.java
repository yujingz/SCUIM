package scu.im.msgtype;

import java.io.Serializable;

public class VoicChatRequestType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3075503027816977925L;
	private String requestUid;
	private String requestName;
	private String requestIp;
	public String getRequestUid() {
		return requestUid;
	}
	public void setRequestUid(String requestUid) {
		this.requestUid = requestUid;
	}
	public String getRequestName() {
		return requestName;
	}
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	public String getRequestIp() {
		return requestIp;
	}
	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
	
}
