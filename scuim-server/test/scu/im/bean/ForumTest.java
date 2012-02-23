/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 测试论坛信息类
 * 
 *************************************************************************************************/

package scu.im.bean;

import java.util.Date;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;

import scu.im.utils.HibernateUtils;

public class ForumTest extends TestCase {

	public void testSaveForum() {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtils.getSession();
			transaction = session.beginTransaction();

			User user = new User();
			user.setUid("603955");

			GroupInfo gInfo = new GroupInfo();
			gInfo.setGid("62301");

			Forum forum = new Forum();
			
			forum.setAuthorId(user);
			forum.setContent("抢占论坛沙发~~");
			forum.setGinfo(gInfo);
			forum.setReplyNumber(2);
			forum.setScanNumber(4);
			forum.setTittle("这是论坛的第一个测试！");
			forum.setTime(new Date());
			forum.setLastReplyer("小琦");
			session.save(forum);

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtils.closeSession(session);
		}
	}
}
