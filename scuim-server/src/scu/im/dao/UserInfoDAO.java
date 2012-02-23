/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 用户个人详细信息操作接口。
 * 
 *************************************************************************************************/

package scu.im.dao;

import scu.im.bean.UserInfo;

public interface UserInfoDAO {

	public void saveUserInfo(UserInfo userInfo);

	public UserInfo findUserInfoByUid(String uid);
	
	public void updateUserInfo(UserInfo userinfo);
}
