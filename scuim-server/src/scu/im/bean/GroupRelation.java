/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 群成员bean，用来存储群成员信息。
 * 
 *************************************************************************************************/

package scu.im.bean;

public class GroupRelation {
	private String id;
	private User uid;
	private GroupInfo gid;

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

	public GroupInfo getGid() {
		return gid;
	}

	public void setGid(GroupInfo gid) {
		this.gid = gid;
	}

}
