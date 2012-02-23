/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 用户个人详细信息DAO。
 * 
 *************************************************************************************************/

package scu.im.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import scu.im.bean.UserInfo;
import scu.im.dao.UserInfoDAO;
import scu.im.utils.HibernateUtils;

public class UserInfoDAOImpl extends HibernateDaoSupport implements UserInfoDAO {

	public UserInfo findUserInfoByUid(String uid) {
		Session session = null;
		UserInfo user = new UserInfo();
		String hql = "from UserInfo info where info.uid = '" + uid + "'";
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			List<UserInfo> userList = (List<UserInfo>) query.list();
			if (!userList.isEmpty()) {
				Iterator<UserInfo> it = userList.iterator();
				user = it.next();
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
		return user;
	}

	public void saveUserInfo(UserInfo userInfo) {
		new HibernateTemplate(HibernateUtils.getSessionFactory())
				.save(userInfo);
	}

	public void updateUserInfo(UserInfo userinfo) {
//		Session session = null;
//		try {
//			session = HibernateUtils.getSession();
//			session.beginTransaction();
			new HibernateTemplate(HibernateUtils.getSessionFactory())
					.update(userinfo);
//			session.getTransaction().commit();
//		} catch (Exception ex) {
//			session.getTransaction().rollback();
//			ex.printStackTrace();
//		} finally {
//			if (session != null) {
//				if (session.isOpen()) {
//					session.close();
//				}
//			}
//		}
	}
}
