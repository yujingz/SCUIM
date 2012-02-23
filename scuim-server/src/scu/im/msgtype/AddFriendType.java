/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 添加好友的报文类型
 * 
 *************************************************************************************************/

package scu.im.msgtype;

import java.io.Serializable;

public class AddFriendType implements Serializable {

	private static final long serialVersionUID = -4533216215414729310L;
	private String requestUid;
	private String wantedUid;
	private String requestIp;

	public String getRequestUid() {
		return requestUid;
	}

	public void setRequestUid(String requestUid) {
		this.requestUid = requestUid;
	}

	public String getWantedUid() {
		return wantedUid;
	}

	public void setWantedUid(String wantedUid) {
		this.wantedUid = wantedUid;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

}
