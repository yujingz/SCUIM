/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 论坛回复操作接口。
 * 
 *************************************************************************************************/

package scu.im.dao;

import java.util.List;

import scu.im.bean.ForumReply;

public interface ForumReplyDAO {
	public List<ForumReply> findReplysByForumId(int forumId);
}
