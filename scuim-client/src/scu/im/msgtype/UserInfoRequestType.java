package scu.im.msgtype;

import java.io.Serializable;

public class UserInfoRequestType implements Serializable {

	private static final long serialVersionUID = -8651430145302741718L;
	private String requestId;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}
