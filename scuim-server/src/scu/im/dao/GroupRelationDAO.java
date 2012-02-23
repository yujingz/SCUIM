/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 群成员操作接口。
 * 
 *************************************************************************************************/

package scu.im.dao;

import java.util.List;

import scu.im.bean.GroupRelation;

public interface GroupRelationDAO {
	public List<GroupRelation> findMembersByGroupId(String gid);

	public List<GroupRelation> findGroupIdByUid(String uid);
	
	public void addMember(GroupRelation gRelation);
}
