/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 测试用户信息类
 * 
 *************************************************************************************************/

package scu.im.bean;

import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;

import scu.im.utils.HibernateUtils;

public class UserInfoTest extends TestCase {

	public void testSaveUserInfo() {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtils.getSession();
			transaction = session.beginTransaction();

			User u1 = new User();
			u1.setUid("603955");
			User u2 = new User();
			u2.setUid("603966");
			User u3 = new User();
			u3.setUid("603977");
			User u4 = new User();
			u4.setUid("603988");
			User u5 = new User();
			u5.setUid("603999");
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

			UserInfo user1 = new UserInfo();
			user1.setUid(u1);
			user1.setCountry("中国");
			user1.setProvince("浙江");
			user1.setCity("杭州");
			user1.setBirthday(dateFormat.parse("7-7-1989"));
			user1.setBlood(0);
			user1.setScratch("Final Fantasy");
			user1.setDescription("Yes, I do\nYes, \nI do my princess. Forever... \n死生契阔,与子成说:执子之手,与子偕老");
			user1.setEmail("603977@qq.com");
			user1.setGender(1);
			user1.setNickname("小郑");
			user1.setOccupation("学生");
			user1.setPhone("88592607");
			user1.setPicture(14);
			user1.setPwdQuestion("Can you?");
			user1.setPwdAnswer("Yse");

			UserInfo user2 = new UserInfo();
			user2.setUid(u2);
			user2.setCountry("中国");
			user2.setProvince("云南");
			user2.setCity("昆明");
			user2.setBirthday(dateFormat.parse("6-10-1989"));
			user2.setBlood(0);
			user2.setScratch("雅蠛蝶在天上飞诶~~~草泥马再地上追诶~~~");
			user2.setDescription("这人不错~~恩 不错");
			user2.setEmail("708941437@qq.com");
			user2.setGender(0);
			user2.setNickname("小彭");
			user2.setOccupation("学生");
			user2.setPhone("85992607");
			user2.setPicture(21);
			user2.setPwdQuestion("Are you?");
			user2.setPwdAnswer("No");

			UserInfo user3 = new UserInfo();
			user3.setUid(u3);
			user3.setCountry("中国");
			user3.setProvince("新疆");
			user3.setCity("乌鲁木齐");
			user3.setBirthday(dateFormat.parse("6-26-1988"));
			user3.setBlood(1);
			user3.setScratch("作为程序员，在20-30岁这段时间“勤奋”这两个字要背得起！");
			user3.setDescription("森林中有一个分岔口，我愿选择脚印少的那一条路，这样我的一生会截然不同......\n我要走我自己的路");
			user3.setEmail("yaowuping@qq.com");
			user3.setGender(0);
			user3.setNickname("小姚");
			user3.setOccupation("学生");
			user3.setPhone("85992607");
			user3.setPicture(16);
			user3.setPwdQuestion("Will you?");
			user3.setPwdAnswer("No");
			
			UserInfo user4 = new UserInfo();
			user4.setUid(u4);
			user4.setCountry("中国");
			user4.setProvince("四川");
			user4.setCity("成都");
			user4.setBirthday(dateFormat.parse("7-20-1988"));
			user4.setBlood(0);
			user4.setScratch("哈球哈球~~");
			user4.setDescription("这个人很懒，什么都没有留下...");
			user4.setEmail("heqi37@gmail.com");
			user4.setGender(1);
			user4.setNickname("小琦");
			user4.setOccupation("学生");
			user4.setPhone("85992607");
			user4.setPicture(13);
			user4.setPwdQuestion("Shall we?");
			user4.setPwdAnswer("No");
			
			UserInfo user5 = new UserInfo();
			user5.setUid(u5);
			user5.setCountry("中国");
			user5.setProvince("云南");
			user5.setCity("昆明");
			user5.setBirthday(dateFormat.parse("6-1-1988"));
			user5.setBlood(0);
			user5.setScratch("亲爱的朋友们，我电话有点问题格机了，通讯录也不见了，麻烦大家把号码发我一下吧~~");
			user5.setDescription("http://ks.cn.yahoo.com/question/1407112203698_19.htm\nhttp://www.xuexi58.com/untitled/yyxx.html\nhttp://www.pconline.com.cn \nskyfuqi@gmail\nliguoxing8868@163.com \nhttp://www2.scut.edu.cn/scuttefl/cet4/\n");
			user5.setEmail("skyfuqi@gmail");
			user5.setGender(1);
			user5.setNickname("小李");
			user5.setOccupation("学生");
			user5.setPhone("85992607");
			user5.setPicture(7);
			user5.setPwdQuestion("How are you?");
			user5.setPwdAnswer("No");

			session.save(user1);
			session.save(user2);
			session.save(user3);
			session.save(user4);
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
