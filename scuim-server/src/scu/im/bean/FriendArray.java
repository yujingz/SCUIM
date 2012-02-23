/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 好友列表bean，用来存储好友列表信息。
 * 
 *************************************************************************************************/

package scu.im.bean;

public class FriendArray {
	private String id;
	private User uid;
	private User fid;
	private String fakename;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUid() {
		return uid;
	}

	public void setUid(User uid) {
		this.uid = uid;
	}

	public User getFid() {
		return fid;
	}

	public void setFid(User fid) {
		this.fid = fid;
	}

	public String getFakename() {
		return fakename;
	}

	public void setFakename(String fakename) {
		this.fakename = fakename;
	}

}
