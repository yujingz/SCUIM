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
