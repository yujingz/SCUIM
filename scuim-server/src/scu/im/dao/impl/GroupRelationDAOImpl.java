/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 群成员DAO。
 * 
 *************************************************************************************************/

package scu.im.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import scu.im.bean.GroupRelation;
import scu.im.dao.GroupRelationDAO;
import scu.im.utils.HibernateUtils;

public class GroupRelationDAOImpl implements GroupRelationDAO {

	public List<GroupRelation> findGroupIdByUid(String uid) {
		Session session = null;
		String hql = "from GroupRelation gr where gr.uid = '" + uid + "'";
		List<GroupRelation> relationList = null;
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			relationList = (List<GroupRelation>) query.list();
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
		return relationList;
	}

	public List<GroupRelation> findMembersByGroupId(String gid) {
		Session session = null;
		String hql = "from GroupRelation gr where gr.gid = '" + gid + "'";
		List<GroupRelation> relationList = null;
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			relationList = (List<GroupRelation>) query.list();
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
		return relationList;
	}

	public void addMember(GroupRelation gRelation) {
		Session session = null;
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			session.save(gRelation);
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
