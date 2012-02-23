/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 用户确认添加好友的报文类型
 * 
 *************************************************************************************************/

package scu.im.msgtype;

import java.io.Serializable;

public class ConfirmResultType implements Serializable {

	private static final long serialVersionUID = -4533216215414729310L;
	private boolean result;
	private String requestUid;
	private String wantedUid;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

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

}
