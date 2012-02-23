/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 论坛信息DAO。
 * 
 *************************************************************************************************/

package scu.im.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import scu.im.bean.Forum;
import scu.im.dao.ForumDAO;
import scu.im.utils.HibernateUtils;

public class ForumDAOImpl implements ForumDAO {

	public List<Forum> findForumsByGid(String gid) {
		Session session = null;
		List<Forum> forumList = null;
		String hql = "from Forum f where f.ginfo = '" + gid + "'";
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			forumList = (List<Forum>) query.list();
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
		return forumList;
	}

	public Forum findForumByForumId(String forumId) {
		Session session = null;
		Forum forum = null;
		String hql = "from Forum f where f.forumid = '" + forumId + "'";
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			List<Forum> forumList = (List<Forum>) query.list();
			if (forumList != null) {
				Iterator<Forum> it = forumList.iterator();
				forum = it.next();
			}
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
		return forum;
	}
}
