package scu.im.msgtype;

import java.io.Serializable;

public class ConfirmReturnType implements Serializable {

	private static final long serialVersionUID = 2952590303330714369L;

	private String requestUid;
	private String wantedUid;
	private boolean accepted;// 为0，拒绝加好友，为1，加好友

	public String getRequestUid() {
		return requestUid;
	}

	public void setRequestUid(String requestUid) {
		this.requestUid = requestUid;
	}

	public String getWantedUid() {
		return wantedUid;
	}

	public void setWantedUid(String wantedUid) {
		this.wantedUid = wantedUid;
	}

	public boolean isAccecped() {
		return accepted;
	}

	public void setAccecped(boolean accecped) {
		this.accepted = accecped;
	}

}
