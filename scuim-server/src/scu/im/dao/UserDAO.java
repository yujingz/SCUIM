/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 用户身份验证操作接口。
 * 
 *************************************************************************************************/

package scu.im.dao;

import scu.im.bean.User;

public interface UserDAO {

	public void saveUser(User user);

	public User findUserByIdAndPwd(String id, String pwd);

	// public void removeUser(User user);
	// public List<User> findAllUsers();
	// public void updateUser(User user);
}
