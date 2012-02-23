package scu.im.msgtype;

import java.io.Serializable;

public class TCPFileResponseType implements Serializable {

	private static final long serialVersionUID = -5030800171846216538L;
	private String senderUid;
	private String senderName;
	private String senderIp;
	private boolean isAccecpted;

	public boolean isAccecpted() {
		return isAccecpted;
	}

	public void setAccecpted(boolean isAccecpted) {
		this.isAccecpted = isAccecpted;
	}

	public String getSenderIp() {
		return senderIp;
	}

	public void setSenderIp(String senderIp) {
		this.senderIp = senderIp;
	}

	public String getSenderUid() {
		return senderUid;
	}

	public void setSenderUid(String senderUid) {
		this.senderUid = senderUid;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

}
