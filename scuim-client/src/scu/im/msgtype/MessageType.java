package scu.im.msgtype;

import java.io.Serializable;
import java.util.Date;

public class MessageType implements Serializable {

	private static final long serialVersionUID = -2114781639474688241L;
	private String sourceId;
	private String destinationId;
	private String content;
	private Date sendTime;

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
}
