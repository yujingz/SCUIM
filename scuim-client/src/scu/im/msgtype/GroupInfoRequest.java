package scu.im.msgtype;

import java.io.Serializable;

public class GroupInfoRequest implements Serializable {

	private static final long serialVersionUID = -1137911979498565914L;
	private String uid;
	private String gid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

}
