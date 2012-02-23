/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 子测试调用程序
 * 
 *************************************************************************************************/

package scu.im.bean;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAll extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTestSuite(AccountTest.class);
		suite.addTestSuite(GroupTest.class);
		suite.addTestSuite(UserTest.class);
		suite.addTestSuite(FriendTest.class);
		suite.addTestSuite(UserInfoTest.class);
		suite.addTestSuite(RelationTest.class);
		suite.addTestSuite(ForumTest.class);
		suite.addTestSuite(ForumReplyTest.class);
		
		return suite;
	}
}
