/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 测试群成员类
 * 
 *************************************************************************************************/

package scu.im.bean;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;

import scu.im.utils.HibernateUtils;

public class RelationTest extends TestCase {

	public void testGroupInfo() {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtils.getSession();
			transaction = session.beginTransaction();

			User user = new User();
			user.setUid("603977");

			GroupInfo groupInfo = new GroupInfo();
			groupInfo.setCreatorUser(user);
			groupInfo.setDescription("为301的战士们提供的VIP群！");
			groupInfo.setGid("62301");
			groupInfo.setGname("301寝室群");
			groupInfo.setNotice("暂无公告");
			groupInfo.setSize(100);
			session.save(groupInfo);

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtils.closeSession(session);
		}

	}

	public void testGroupRelation() {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtils.getSession();
			transaction = session.beginTransaction();

			User user1 = new User();
			user1.setUid("603977");

			User user2 = new User();
			user2.setUid("603988");

			User user3 = new User();
			user3.setUid("603999");

			GroupInfo groupInfo = new GroupInfo();
			groupInfo.setGid("62301");

			GroupRelation groupRelation1 = new GroupRelation();
			groupRelation1.setGid(groupInfo);
			groupRelation1.setUid(user1);
			session.save(groupRelation1);

			GroupRelation groupRelation2 = new GroupRelation();
			groupRelation2.setGid(groupInfo);
			groupRelation2.setUid(user2);
			session.save(groupRelation2);

			GroupRelation groupRelation3 = new GroupRelation();
			groupRelation3.setGid(groupInfo);
			groupRelation3.setUid(user3);
			session.save(groupRelation3);

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtils.closeSession(session);
		}
	}
}
