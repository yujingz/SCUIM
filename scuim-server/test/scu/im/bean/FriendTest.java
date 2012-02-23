/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 测试好友信息类
 * 
 *************************************************************************************************/

package scu.im.bean;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;

import scu.im.utils.HibernateUtils;

public class FriendTest extends TestCase {

	public void testSaveFriend() {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtils.getSession();
			transaction = session.beginTransaction();

			User user1 = new User();
			user1.setUid("603955");

			User user2 = new User();
			user2.setUid("603966");

			User user3 = new User();
			user3.setUid("603977");

			User user4 = new User();
			user4.setUid("603988");

			User user5 = new User();
			user5.setUid("603999");

			// 用户1的好友
			FriendArray friend11 = new FriendArray();
			friend11.setUid(user1);
			friend11.setFid(user2);
			session.save(friend11);

			FriendArray friend12 = new FriendArray();
			friend12.setUid(user1);
			friend12.setFid(user3);
			session.save(friend12);

			FriendArray friend13 = new FriendArray();
			friend13.setUid(user1);
			friend13.setFid(user4);
			session.save(friend13);

			FriendArray friend14 = new FriendArray();
			friend14.setUid(user1);
			friend14.setFid(user5);
			session.save(friend14);

			// 用户2的好友
			FriendArray friend21 = new FriendArray();
			friend21.setUid(user2);
			friend21.setFid(user1);
			session.save(friend21);

			FriendArray friend22 = new FriendArray();
			friend22.setUid(user2);
			friend22.setFid(user3);
			session.save(friend22);

			FriendArray friend23 = new FriendArray();
			friend23.setUid(user2);
			friend23.setFid(user4);
			session.save(friend23);

			FriendArray friend24 = new FriendArray();
			friend24.setUid(user2);
			friend24.setFid(user5);
			session.save(friend24);

			// 用户3的好友
			FriendArray friend31 = new FriendArray();
			friend31.setUid(user3);
			friend31.setFid(user1);
			session.save(friend31);

			FriendArray friend32 = new FriendArray();
			friend32.setUid(user3);
			friend32.setFid(user2);
			session.save(friend32);

			FriendArray friend33 = new FriendArray();
			friend33.setUid(user3);
			friend33.setFid(user4);
			session.save(friend33);

			FriendArray friend34 = new FriendArray();
			friend34.setUid(user3);
			friend34.setFid(user5);
			session.save(friend34);

			// 用户4的好友
			FriendArray friend41 = new FriendArray();
			friend41.setUid(user4);
			friend41.setFid(user1);
			session.save(friend41);

			FriendArray friend42 = new FriendArray();
			friend42.setUid(user4);
			friend42.setFid(user2);
			session.save(friend42);

			FriendArray friend43 = new FriendArray();
			friend43.setUid(user4);
			friend43.setFid(user3);
			session.save(friend43);

			FriendArray friend44 = new FriendArray();
			friend44.setUid(user4);
			friend44.setFid(user5);
			session.save(friend44);

			// 用户5的好友
			FriendArray friend51 = new FriendArray();
			friend51.setUid(user5);
			friend51.setFid(user1);
			session.save(friend51);

			FriendArray friend52 = new FriendArray();
			friend52.setUid(user5);
			friend52.setFid(user2);
			session.save(friend52);

			FriendArray friend53 = new FriendArray();
			friend53.setUid(user5);
			friend53.setFid(user3);
			session.save(friend53);

			FriendArray friend54 = new FriendArray();
			friend54.setUid(user5);
			friend54.setFid(user4);
			session.save(friend54);

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtils.closeSession(session);
		}
	}
}
