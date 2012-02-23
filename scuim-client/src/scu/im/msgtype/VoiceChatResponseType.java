package scu.im.msgtype;

import java.io.Serializable;

public class VoiceChatResponseType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2346789142473603499L;
	private String responseUid;
	private String responseName;
	private String responseIp;
	private boolean isAccepted;
	public String getResponseUid() {
		return responseUid;
	}
	public void setResponseUid(String responseUid) {
		this.responseUid = responseUid;
	}
	public String getResponseName() {
		return responseName;
	}
	public void setResponseName(String responseName) {
		this.responseName = responseName;
	}
	public boolean isAccepted() {
		return isAccepted;
	}
	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}
	public String getResponseIp() {
		return responseIp;
	}
	public void setResponseIp(String responseIp) {
		this.responseIp = responseIp;
	}
	
}
