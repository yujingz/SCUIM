package scu.im.msgtype;

import java.io.Serializable;

public class SearchUserReturnType implements Serializable {

	private static final long serialVersionUID = -1180597043590007912L;

	private String uid;
	private String nickname;
	private String city;
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
