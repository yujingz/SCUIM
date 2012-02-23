/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 好友操作接口。
 * 
 *************************************************************************************************/

package scu.im.dao;

import java.util.List;

import scu.im.bean.FriendArray;

public interface FriendDAO {
	public List<FriendArray> findFriendsByUid(String id);

	public List<FriendArray> findUsersByFid(String fid);

	public void addFriend(FriendArray friend);
}
