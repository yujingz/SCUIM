/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 测试账号生成类
 * 
 *************************************************************************************************/

package scu.im.bean;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;

import scu.im.utils.HibernateUtils;

public class AccountTest extends TestCase {

	public void testSetAccount() {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtils.getSession();
			transaction = session.beginTransaction();

			AccountNumber number1 = new AccountNumber();
			number1.setAccountNumber("408096242");
			session.save(number1);

			AccountNumber number2 = new AccountNumber();
			number2.setAccountNumber("708941437");
			session.save(number2);

			AccountNumber number3 = new AccountNumber();
			number3.setAccountNumber("827506818");
			session.save(number3);

			AccountNumber number4 = new AccountNumber();
			number4.setAccountNumber("287283895");
			session.save(number4);

			AccountNumber[] numbers = new AccountNumber[10];

			for (int i = 0; i < 10; i++) {
				numbers[i] = new AccountNumber();
				numbers[i].setAccountNumber("95279527" + i);
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
