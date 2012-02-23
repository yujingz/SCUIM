/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 测试论坛回复类
 * 
 *************************************************************************************************/

package scu.im.bean;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;

import scu.im.dao.ForumDAO;
import scu.im.dao.impl.ForumDAOImpl;
import scu.im.utils.HibernateUtils;

public class ForumReplyTest extends TestCase {

	public void testSaveForumReply() {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtils.getSession();
			transaction = session.beginTransaction();

			ForumDAO forumDao = new ForumDAOImpl();
			List<Forum> forumList = forumDao.findForumsByGid("62301");

			User user = new User();
			user.setUid("603999");

			ForumReply forumReply = new ForumReply();
			if (!forumList.isEmpty()) {
				Iterator<Forum> it = forumList.iterator();
				forumReply.setForumId(it.next().getForumid());
			} else {
				forumReply.setForumId(1);
			}
			forumReply.setContent("我是第一个回帖的人！");
			forumReply.setReplyerId(user);
			forumReply.setReplyTime(new Date());

			session.save(forumReply);

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtils.closeSession(session);
		}
	}
}
