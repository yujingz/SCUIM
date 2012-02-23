package scu.im.msgtype;

import java.io.Serializable;

public class GroupApplyType implements Serializable {

	private static final long serialVersionUID = 7924905644881838296L;
	private String requestUid;
	private String creatorUid;
	private String gid;
	private String requestIp;

	public String getRequestUid() {
		return requestUid;
	}

	public void setRequestUid(String requestUid) {
		this.requestUid = requestUid;
	}

	public String getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(String creatorUid) {
		this.creatorUid = creatorUid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

}
