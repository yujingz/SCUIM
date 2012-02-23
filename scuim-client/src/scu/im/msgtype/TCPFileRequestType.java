package scu.im.msgtype;

import java.io.Serializable;

public class TCPFileRequestType implements Serializable {

	private static final long serialVersionUID = -5513929107716026469L;
	private String senderUid;
	private String senderName;
	private String receiverUid;
	private String filename;
	private String receiverIp;
	private String senderIp;

	public String getSenderUid() {
		return senderUid;
	}

	public void setSenderUid(String senderUid) {
		this.senderUid = senderUid;
	}

	public String getReceiverUid() {
		return receiverUid;
	}

	public void setReceiverUid(String receiverUid) {
		this.receiverUid = receiverUid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getReceiverIp() {
		return receiverIp;
	}

	public void setReceiverIp(String receiverIp) {
		this.receiverIp = receiverIp;
	}

	public String getSenderIp() {
		return senderIp;
	}

	public void setSenderIp(String senderIp) {
		this.senderIp = senderIp;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
}
