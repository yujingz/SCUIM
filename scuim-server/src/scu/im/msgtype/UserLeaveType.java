/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 用户离开请求的报文类型
 * 
 *************************************************************************************************/

package scu.im.msgtype;

import java.io.Serializable;

public class UserLeaveType implements Serializable {

	private static final long serialVersionUID = 54068067469580461L;
	String leaveUserId;

	public String getLeaveUserId() {
		return leaveUserId;
	}

	public void setLeaveUserId(String leaveUserId) {
		this.leaveUserId = leaveUserId;
	}

}
