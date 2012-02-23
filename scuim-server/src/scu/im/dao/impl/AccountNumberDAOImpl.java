/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 账号生成DAO。
 * 
 *************************************************************************************************/

package scu.im.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import scu.im.bean.AccountNumber;
import scu.im.dao.AccountNumberDAO;
import scu.im.utils.HibernateUtils;

public class AccountNumberDAOImpl extends HibernateDaoSupport implements
		AccountNumberDAO {

	public String generateAccountNumber() {
		Session session = null;
		List<AccountNumber> accounts = null;
		String number = "";
		String hql = "from AccountNumber";
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			accounts = (List<AccountNumber>) query.list();
			if (!accounts.isEmpty()) {
				Iterator<AccountNumber> it = accounts.iterator();
				AccountNumber account = it.next();
				number = account.getAccountNumber();
				hql = "delete from AccountNumber an where an.accountNumber = '"
						+ number + "'";
				int result = session.createQuery(hql).executeUpdate();
				if (result > 0) {
					System.out
							.println("##########---------->>>：A new AccountNumber has been used.");
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
