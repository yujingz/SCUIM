/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 用户的好友DAO。
 * 
 *************************************************************************************************/

package scu.im.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import scu.im.bean.FriendArray;
import scu.im.dao.FriendDAO;
import scu.im.utils.HibernateUtils;

public class FriendDAOImpl implements FriendDAO {

	@SuppressWarnings("unchecked")
	public List<FriendArray> findFriendsByUid(String id) {
		Session session = null;
		String hql = "from FriendArray friends where friends.uid = '" + id
				+ "'";
		List<FriendArray> userList = null;
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			userList = (List<FriendArray>) query.list();
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

	@SuppressWarnings("unchecked")
	public List<FriendArray> findUsersByFid(String fid) {
		Session session = null;
		String hql = "from FriendArray friends where friends.fid = '" + fid
				+ "'";
		List<FriendArray> userList = null;
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			userList = (List<FriendArray>) query.list();
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

	public void addFriend(FriendArray friend) {
		Session session = null;
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.save(friend);
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
	}
}
