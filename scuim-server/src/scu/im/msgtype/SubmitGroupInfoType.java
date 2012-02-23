/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 提交修改群信息的报文类型
 * 
 *************************************************************************************************/

package scu.im.msgtype;

import java.io.Serializable;

public class SubmitGroupInfoType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7583945564913573714L;
	private String gid;
	private String gName;
	private String description;
	private String notice;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

}