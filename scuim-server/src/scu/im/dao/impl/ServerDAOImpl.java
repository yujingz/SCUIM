/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 在线用户信息DAO。
 * 
 *************************************************************************************************/

package scu.im.dao.impl;

import static scu.im.utils.Print.print;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import scu.im.bean.Server;
import scu.im.dao.ServerDAO;
import scu.im.utils.HibernateUtils;

public class ServerDAOImpl extends HibernateDaoSupport implements ServerDAO {

	public void addMember(Server server) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtils.getSession();
			transaction = session.beginTransaction();

			session.save(server);

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtils.closeSession(session);
		}
	}

	public void deleteMemberByIp(String ip) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtils.getSession();
			transaction = session.beginTransaction();

			String hql = "delete from Server server where server.ip = '" + ip
					+ "'";
			int result = session.createQuery(hql).executeUpdate();
			if (result > 0) {
				print("Ip为[" + ip + "]的用户断开了连接.");
				print();
			}
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtils.closeSession(session);
		}
	}

	public Server findMemberByUid(String uid) {
		Session session = null;
		Server server = new Server();
		String hql = "from Server server where server.uid = '" + uid + "'";
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			List<Server> userList = (List<Server>) query.list();
			if (!userList.isEmpty()) {
				Iterator<Server> it = userList.iterator();
				server = it.next();
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
		return server;
	}

	public List<Server> findAllMembers() {
		Session session = null;
		List<Server> onlineUsers = null;
		String hql = "from Server ";
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			onlineUsers = (List<Server>) query.list();
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
		return onlineUsers;
	}
}
