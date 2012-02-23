package scu.im.service;

import static scu.im.utils.Print.print;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import scu.im.msgtype.FriendUnitType;
import scu.im.msgtype.GroupInfoType;
import scu.im.msgtype.GroupUnitType;
import scu.im.msgtype.Msg;
import scu.im.msgtype.SearchGroupReturnType;
import scu.im.msgtype.SearchUserReturnType;
import scu.im.msgtype.UserInfoResponseType;
import scu.im.msgtype.UserLeaveType;
import scu.im.socket.IMSocket;
import scu.im.utils.Information;
import scu.im.utils.Observer;
import scu.im.utils.Subject;

public class ListenTCPThread extends Thread implements Subject {

	private Socket connectionSocket = null;
	private Boolean stopThread = false;
	private Observer observer = null;

	public ListenTCPThread() {
		this.connectionSocket = IMSocket.getInstance();
		print("服务器端socket为：" + connectionSocket);
	}

	@SuppressWarnings("unchecked")
	public void run() {
		print("客户端" + Information.getUid() + "监听线程已启动!");
		try {
			while (!stopThread) {
				ObjectInputStream inFromClient = new ObjectInputStream(
						connectionSocket.getInputStream());
				// ObjectOutputStream outToClient = new ObjectOutputStream(
				// connectionSocket.getOutputStream());
				Msg msg = (Msg) inFromClient.readObject();
				switch (msg.getHead().ordinal()) {
				case 0:
					break;
				case 4:
					print("收到服务器发来的好友上线消息!");
					FriendUnitType friend = (FriendUnitType) msg.getBody();

					print("接收info:" + friend.getUid());
					print("接收info:" + friend.getIp());
					print("接收info:" + friend.getPort());
					print("接收info:" + friend.getState());

					Hashtable<String, FriendUnitType> friendlist = Information
							.getFriendList();
					if (friendlist.containsKey(friend.getUid())) {
						friendlist.remove(friend.getUid());
					}
					friendlist.put(friend.getUid(), friend);
					Information.setFriendList(friendlist);
					print("调用notifyObservers()方法更新好友列表!");
					notifyObservers();
					break;
				case 8:
					print("收到搜索在线用户反馈信息!");
					List<SearchUserReturnType> onlineUserList = (ArrayList<SearchUserReturnType>) msg
							.getBody();
					Information.getSearchPanel().showSearchResult(
							onlineUserList);
					break;
				case 12:
					print("收到用户群信息!");
					List<GroupUnitType> groupUnitList = (List<GroupUnitType>) msg
							.getBody();
					for (GroupUnitType gut : groupUnitList) {
						Hashtable<String, GroupUnitType> groupList = Information
								.getGroupList();
						if (groupList.containsKey(gut.getGid())) {
							groupList.remove(gut.getGid());
						}
						groupList.put(gut.getGid(), gut);

					}
					Information.getMainGui().updateGroupList();
					break;
				case 15:
					print("收到搜索群回报消息！");
					SearchGroupReturnType sgrt = (SearchGroupReturnType) msg
							.getBody();
					Information.getSearchPanel().showGroupSearchResult(sgrt);
					break;
				case 19:
					print("收到群详细信息");
					GroupInfoType git = (GroupInfoType) msg.getBody();
					Information.getGroupInfoHash().get(git.getGid())
							.showGroupInfo(git);
					break;
				case 21:
					print("收到好友详细信息");
					UserInfoResponseType uirt = (UserInfoResponseType) msg
							.getBody();
					Information.getPersonalInfoHash().get(uirt.getUid())
							.showUserInfo(uirt);
					print(uirt.getBirthday());
					break;
				case 23:
					print("收到好友下线消息");
					UserLeaveType ult = (UserLeaveType) msg.getBody();
					String leaveUid = ult.getLeaveUserId();
					print(leaveUid + "下线了");
					Information.getFriendUnitHash().get(leaveUid).offLine();
					Information.getFriendList().get(leaveUid).setIp(null);					
					break;
				case 24:
					print("TCP AC 回报消息");
					String acStr = (String) msg.getBody();
					print(acStr);
					break;
				
				default:
					print(msg.getBody());
					print("监听到非法信息!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notifyObservers() {
		observer.update();
	}

	@Override
	public void registerObserver(Observer o) {
		observer = o;
	}
}
