package scu.im.msgtype;

import java.io.Serializable;

public class SearchGroupType implements Serializable {

	private static final long serialVersionUID = -6280760071007982908L;
	private String requestUid;
	private String requestGid;
	private String requestIp;

	public String getRequestUid() {
		return requestUid;
	}

	public void setRequestUid(String requestUid) {
		this.requestUid = requestUid;
	}

	public String getRequestGid() {
		return requestGid;
	}

	public void setRequestGid(String requestGid) {
		this.requestGid = requestGid;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

}
