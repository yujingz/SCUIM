/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 论坛回复bean，用来存储论坛回复信息。
 * 
 *************************************************************************************************/

package scu.im.bean;

import java.util.Date;

public class ForumReply {
	private String id;
	private int forumId;
	private User replyerId;
	private Date replyTime;
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getForumId() {
		return forumId;
	}

	public void setForumId(int forumId) {
		this.forumId = forumId;
	}

	public User getReplyerId() {
		return replyerId;
	}

	public void setReplyerId(User replyerId) {
		this.replyerId = replyerId;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

}
