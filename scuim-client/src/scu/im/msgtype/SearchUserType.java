package scu.im.msgtype;

import java.io.Serializable;

public class SearchUserType implements Serializable {

	private static final long serialVersionUID = -7887657788508470082L;
	private String senderUid;
	private String searchUid;
	private int searchType;// 若searchType = 0，则搜索特定用户。 若为1，则搜索在线用户

	public String getSenderUid() {
		return senderUid;
	}

	public void setSenderUid(String senderUid) {
		this.senderUid = senderUid;
	}

	public String getSearchUid() {
		return searchUid;
	}

	public void setSearchUid(String searchUid) {
		this.searchUid = searchUid;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

}
