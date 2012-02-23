/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 在线用户操作接口。
 * 
 *************************************************************************************************/

package scu.im.dao;

import java.util.List;

import scu.im.bean.Server;

public interface ServerDAO {

	public void addMember(Server server);

	public void deleteMemberByIp(String ip);

	public Server findMemberByUid(String uid);

	public List<Server> findAllMembers();
}
