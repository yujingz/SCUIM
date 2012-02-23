/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 群信息操作接口。
 * 
 *************************************************************************************************/

package scu.im.dao;

import java.util.List;

import scu.im.bean.GroupInfo;

public interface GroupInfoDAO {
	
	public GroupInfo findGroupInfoByGid(String gid);

	public List<GroupInfo> findGroupByCreatorUid(String creatorUid);

	public void updateUserInfo(GroupInfo groupinfo);
}
