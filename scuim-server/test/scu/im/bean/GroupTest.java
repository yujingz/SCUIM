/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 测试群信息类
 * 
 *************************************************************************************************/

package scu.im.bean;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;

import scu.im.utils.HibernateUtils;

public class GroupTest extends TestCase {

	public void testCreateGroup() {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtils.getSession();
			transaction = session.beginTransaction();

			GroupNumber number1 = new GroupNumber();
			number1.setGroupNumber("301301");
			session.save(number1);

			GroupNumber number2 = new GroupNumber();
			number2.setGroupNumber("201201");
			session.save(number2);

			GroupNumber number3 = new GroupNumber();
			number3.setGroupNumber("101101");
			session.save(number3);

			GroupNumber[] numbers = new GroupNumber[10];

			for (int i = 0; i < 10; i++) {
				numbers[i] = new GroupNumber();
				numbers[i].setGroupNumber("17951" + i);
				session.save(numbers[i]);
			}
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtils.closeSession(session);
		}
	}
}
