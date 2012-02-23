package scu.im.group;

import javax.swing.Icon;

public class GroupListItem {
	private Icon icon;
	private String nickName;
	private String uid;
	private String ip;

	public GroupListItem(Icon icon, String nickName, String uid, String ip) {
		this.icon = icon;
		this.nickName = nickName;
		this.uid = uid;
		this.ip = ip;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}



}
