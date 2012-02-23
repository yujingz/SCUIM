/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 群账号DAO。
 * 
 *************************************************************************************************/

package scu.im.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import scu.im.bean.GroupNumber;
import scu.im.dao.GroupNumberDAO;
import scu.im.utils.HibernateUtils;

public class GroupNumberDAOImpl extends HibernateDaoSupport implements
		GroupNumberDAO {

	public String generateGroupNumber() {
		Session session = null;
		List<GroupNumber> accounts = null;
		String number = "";
		String hql = "from GroupNumber";
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			accounts = (List<GroupNumber>) query.list();
			if (!accounts.isEmpty()) {
				Iterator<GroupNumber> it = accounts.iterator();
				GroupNumber account = it.next();
				number = account.getGroupNumber();
				hql = "delete from GroupNumber gn where gn.groupNumber = '"
						+ number + "'";
				int result = session.createQuery(hql).executeUpdate();
				if (result > 0) {
					System.out
							.println("##########---------->>>：A new GroupNumber has been used.");
				}
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
		return number;
	}
}
