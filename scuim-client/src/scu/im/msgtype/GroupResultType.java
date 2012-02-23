package scu.im.msgtype;

import java.io.Serializable;

public class GroupResultType implements Serializable {

	private static final long serialVersionUID = 5421692513876831884L;
	private String newComerUid;
	private String gid;
	private String creatorUid;
	private String creatorIp;
	private Boolean accepted;

	public String getNewComerUid() {
		return newComerUid;
	}

	public void setNewComerUid(String newComerUid) {
		this.newComerUid = newComerUid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(String creatorUid) {
		this.creatorUid = creatorUid;
	}

	public String getCreatorIp() {
		return creatorIp;
	}

	public void setCreatorIp(String creatorIp) {
		this.creatorIp = creatorIp;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

}
