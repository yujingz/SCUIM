/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 测试用户好友类
 * 
 *************************************************************************************************/

package scu.im.bean;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;

import scu.im.utils.HibernateUtils;

public class UserTest extends TestCase {

	public void testSaveUser() {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtils.getSession();
			transaction = session.beginTransaction();

			User user1 = new User();
			user1.setUid("603955");
			user1.setPwd("123456");
			session.save(user1);

			User user2 = new User();
			user2.setUid("603966");
			user2.setPwd("123456");
			session.save(user2);

			User user3 = new User();
			user3.setUid("603977");
			user3.setPwd("123456");
			session.save(user3);

			User user4 = new User();
			user4.setUid("603988");
			user4.setPwd("123456");
			session.save(user4);

			User user5 = new User();
			user5.setUid("603999");
			user5.setPwd("123456");
			session.save(user5);

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtils.closeSession(session);
		}
	}
}
