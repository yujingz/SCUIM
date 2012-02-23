/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 群信息DAO。
 * 
 *************************************************************************************************/

package scu.im.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import scu.im.bean.GroupInfo;
import scu.im.dao.GroupInfoDAO;
import scu.im.utils.HibernateUtils;

public class GroupInfoDAOImpl implements GroupInfoDAO {

	public GroupInfo findGroupInfoByGid(String gid) {
		Session session = null;
		GroupInfo group = new GroupInfo();
		String hql = "from GroupInfo info where info.gid = '" + gid + "'";
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			List<GroupInfo> userList = (List<GroupInfo>) query.list();
			if (!userList.isEmpty()) {
				Iterator<GroupInfo> it = userList.iterator();
				group = it.next();
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
		return group;
	}

	public List<GroupInfo> findGroupByCreatorUid(String creatorUid) {
		Session session = null;
		GroupInfo group = new GroupInfo();
		List<GroupInfo> userList = null;
		String hql = "from GroupInfo info where info.creatorUser = '"
				+ creatorUid + "'";
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			userList = (List<GroupInfo>) query.list();
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
		return userList;
	}

	public void updateUserInfo(GroupInfo groupinfo) {
		new HibernateTemplate(HibernateUtils.getSessionFactory())
				.update(groupinfo);
	}
}
