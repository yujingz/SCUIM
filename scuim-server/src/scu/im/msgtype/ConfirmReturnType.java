/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 对方是否同意添加好友的报文类型
 * 
 *************************************************************************************************/

package scu.im.msgtype;

import java.io.Serializable;

public class ConfirmReturnType implements Serializable {

	private static final long serialVersionUID = 2952590303330714369L;
	private String requestUid;
	private String wantedUid;
	private boolean accecped;// 为0，拒绝加好友，为1，加好友

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

	public boolean isAccecped() {
		return accecped;
	}

	public void setAccecped(boolean accecped) {
		this.accecped = accecped;
	}

}
