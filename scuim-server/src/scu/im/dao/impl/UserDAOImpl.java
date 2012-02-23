/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 用户身份验证信息DAO。
 * 
 *************************************************************************************************/

package scu.im.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import scu.im.bean.User;
import scu.im.dao.UserDAO;
import scu.im.utils.HibernateUtils;

public class UserDAOImpl extends HibernateDaoSupport implements UserDAO {

	// @SuppressWarnings("unchecked")
	// public List<User> findAllUsers() {
	// String hql = "from User user order by user.id desc";
	// return (List<User>) this.getHibernateTemplate().find(hql);
	// }
	//
	// public void removeUser(User user) {
	// this.getHibernateTemplate().delete(user);
	// }
	//
	public void saveUser(User user) {
		// this.getHibernateTemplate().save(user);
		new HibernateTemplate(HibernateUtils.getSessionFactory()).save(user);
	}

	//
	// public void updateUser(User user) {
	// this.getHibernateTemplate().update(user);
	// }

	@SuppressWarnings("unchecked")
	public User findUserByIdAndPwd(String id, String pwd) {
		Session session = null;
		User user = new User();
		String hql = "from User user where user.uid = '" + id
				+ "' and user.pwd = '" + pwd + "'";
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			List<User> userList = (List<User>) query.list();
			if (!userList.isEmpty()) {
				Iterator<User> it = userList.iterator();
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
}
