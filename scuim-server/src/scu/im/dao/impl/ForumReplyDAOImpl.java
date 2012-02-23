/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 论坛回复信息DAO。
 * 
 *************************************************************************************************/

package scu.im.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import scu.im.bean.ForumReply;
import scu.im.dao.ForumReplyDAO;
import scu.im.utils.HibernateUtils;

public class ForumReplyDAOImpl implements ForumReplyDAO {

	public List<ForumReply> findReplysByForumId(int forumId) {
		Session session = null;
		String hql = "from ForumReply fr where fr.forumId = '" + forumId + "'";
		List<ForumReply> replyList = null;
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			replyList = (List<ForumReply>) query.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		} finally {
			if (session != null) {
				if (session.isOpen()) {
					session.close();
				}
			}
		}
		return replyList;
	}
}
