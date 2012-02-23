package scu.im.msgtype;

import java.io.Serializable;
import java.util.Vector;

public class GroupUnitType implements Serializable {

	private static final long serialVersionUID = -2955565681377739713L;
	private String gid;
	private String gName;
	private String notice;
	private Vector<FriendUnitType> memberList;

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getGName() {
		return gName;
	}

	public void setGName(String name) {
		gName = name;
	}

	public Vector<FriendUnitType> getMemberList() {
		return memberList;
	}

	public void setMemberList(Vector<FriendUnitType> memberList) {
		this.memberList = memberList;
	}

}
