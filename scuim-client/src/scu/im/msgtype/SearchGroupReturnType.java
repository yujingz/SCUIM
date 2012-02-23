package scu.im.msgtype;

import java.io.Serializable;

public class SearchGroupReturnType implements Serializable {

	private static final long serialVersionUID = 7254797001411582635L;
	private String gid;
	private String creatorUid;
	private String creatorIp;
	private String creatorPort;
	private String creatorState;
	private String gname;
	private String description;

	public String getCreatorIp() {
		return creatorIp;
	}

	public void setCreatorIp(String creatorIp) {
		this.creatorIp = creatorIp;
	}

	public String getCreatorPort() {
		return creatorPort;
	}

	public void setCreatorPort(String creatorPort) {
		this.creatorPort = creatorPort;
	}

	public String getCreatorState() {
		return creatorState;
	}

	public void setCreatorState(String creatorState) {
		this.creatorState = creatorState;
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

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
