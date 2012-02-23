package scu.im.msgtype;

import java.io.Serializable;

public class FriendUnitType implements Serializable {

	private static final long serialVersionUID = -8117440595538880453L;
	private String uid;
	private String ip;
	private String port;
	private String nickname;
	private String fakename;
	private String state;
	private int image;
	private String signature;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFakename() {
		return fakename;
	}

	public void setFakename(String fakename) {
		this.fakename = fakename;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
