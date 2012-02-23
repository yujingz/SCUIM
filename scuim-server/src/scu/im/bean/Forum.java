/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 论坛bean，用来存储论坛信息。
 * 
 *************************************************************************************************/

package scu.im.bean;

import java.util.Date;

public class Forum {
	private int forumid;
	private GroupInfo ginfo;
	private User authorId;
	private String tittle;
	private String content;
	private String lastReplyer;//回复人的昵称
	private int replyNumber;
	private int scanNumber;
	private Date time;

	public String getLastReplyer() {
		return lastReplyer;
	}

	public void setLastReplyer(String lastReplyer) {
		this.lastReplyer = lastReplyer;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getForumid() {
		return forumid;
	}

	public void setForumid(int forumid) {
		this.forumid = forumid;
	}

	public GroupInfo getGinfo() {
		return ginfo;
	}

	public void setGinfo(GroupInfo ginfo) {
		this.ginfo = ginfo;
	}

	public User getAuthorId() {
		return authorId;
	}

	public void setAuthorId(User authorId) {
		this.authorId = authorId;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getReplyNumber() {
		return replyNumber;
	}

	public void setReplyNumber(int replyNumber) {
		this.replyNumber = replyNumber;
	}

	public int getScanNumber() {
		return scanNumber;
	}

	public void setScanNumber(int scanNumber) {
		this.scanNumber = scanNumber;
	}

}
