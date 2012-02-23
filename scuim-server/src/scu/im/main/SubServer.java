/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 服务器端的子程序，用来处理用户的所有请求。这些请求包括：
 * 				  1、用户身份验证
 * 				  2、更新好友信息
 * 				  3、添加好友请求
 *                4、接受/拒绝添加好友请求
 *                5、查询好友请求
 *                6、发送聊天信息请求
 *                7、发送文件请求
 *                8、接受/拒绝接收文件请求
 *                9、获取用户详细信息请求
 *                10、修改用户个人信息请求
 *                11、查找群信息请求
 *                12、查看群资料请求
 *                13、加入群请求
 *                14、修改群资料请求
 *                15、群信息发送请求
 *                16、语音聊天请求
 *                17、接受/拒绝语音聊天请求
 * 
 *************************************************************************************************/

package scu.im.main;

import static scu.im.utils.Print.print;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import scu.im.bean.FriendArray;
import scu.im.bean.GroupInfo;
import scu.im.bean.GroupRelation;
import scu.im.bean.Server;
import scu.im.bean.User;
import scu.im.bean.UserInfo;
import scu.im.dao.FriendDAO;
import scu.im.dao.GroupInfoDAO;
import scu.im.dao.GroupRelationDAO;
import scu.im.dao.ServerDAO;
import scu.im.dao.UserDAO;
import scu.im.dao.UserInfoDAO;
import scu.im.dao.impl.FriendDAOImpl;
import scu.im.dao.impl.GroupInfoDAOImpl;
import scu.im.dao.impl.GroupRelationDAOImpl;
import scu.im.dao.impl.ServerDAOImpl;
import scu.im.dao.impl.UserDAOImpl;
import scu.im.dao.impl.UserInfoDAOImpl;
import scu.im.msgtype.ConfirmResultType;
import scu.im.msgtype.FriendUnitType;
import scu.im.msgtype.GroupInfoRequest;
import scu.im.msgtype.GroupInfoType;
import scu.im.msgtype.GroupResultType;
import scu.im.msgtype.GroupUnitType;
import scu.im.msgtype.LoginReturnType;
import scu.im.msgtype.LoginType;
import scu.im.msgtype.Msg;
import scu.im.msgtype.Option;
import scu.im.msgtype.SearchGroupReturnType;
import scu.im.msgtype.SearchGroupType;
import scu.im.msgtype.SearchUserReturnType;
import scu.im.msgtype.SearchUserType;
import scu.im.msgtype.SubmitGroupInfoType;
import scu.im.msgtype.SubmitUserInfoType;
import scu.im.msgtype.UserInfoRequestType;
import scu.im.msgtype.UserInfoResponseType;
import scu.im.msgtype.UserLeaveType;

/*********************************************************************/
/********************** 与客户端建立连接的子线程 **********************/
/*********************************************************************/

public class SubServer extends Thread {

	private Socket connectionSocket = null;
	private Boolean stopThread = false;
	private String g_userId = "";

	/*********************************************************************/
	/************************* 子线程的构造函数 ***************************/
	/*********************************************************************/
	public SubServer(Socket socket) {
		this.connectionSocket = socket;
		print("SubServer中新用户的socket为：", socket);
		MainServer.getClients().put(getLoggerIp(), socket);
		print("SubServer中新用户的IP地址为：" + getLoggerIp());// 打印新用户的IP
	}

	/*********************************************************************/
	/**************** 从客户端socket中提取客户端IP地址 ********************/
	/*********************************************************************/
	private String getLoggerIp() {
		int beginIndex = connectionSocket.toString().indexOf("/") + 1;
		int endIndex = connectionSocket.toString().indexOf(",");
		String userIp = connectionSocket.toString().substring(beginIndex,
				endIndex);
		return userIp;
	}

	/*********************************************************************/
	/*************************** 线程运行主函数 ***************************/
	/*********************************************************************/
	public void run() {
		UserDAO userDao = new UserDAOImpl();
		ServerDAO serverDao = new ServerDAOImpl();
		FriendDAO friendDao = new FriendDAOImpl();
		UserInfoDAO userInfoDao = new UserInfoDAOImpl();
		GroupInfoDAO groupInfoDao = new GroupInfoDAOImpl();
		GroupRelationDAO groupRelationDao = new GroupRelationDAOImpl();
		try {
			while (!stopThread) {
				ObjectInputStream inFromClient = new ObjectInputStream(
						connectionSocket.getInputStream());
				ObjectOutputStream outToClient = new ObjectOutputStream(
						connectionSocket.getOutputStream());
				Msg msgReturn = new Msg();
				Msg msg = (Msg) inFromClient.readObject();
				switch (msg.getHead().ordinal()) {

				/*********************************************************************/
				/************************* 接收到用户退出消息 *************************/
				/*********************************************************************/
				case 0:
					print("收到用户发送的关闭程序信息");
					String userId = (String) msg.getBody();

					// /** 启动一个线程通知用户有好友下线 **/
					// new InformLeave(friendDao.findUsersByFid(userId),
					// getLoggerIp()).start();

					List<FriendArray> userList = friendDao
							.findUsersByFid(userId);
					try {
						for (FriendArray f : userList) {
							Server myFriend = serverDao.findMemberByUid(f
									.getUid().getUid());
							if (null != myFriend.getIp()) {
								print("将要通知本人下线消息的好友为：" + myFriend.getUid());
								Socket friendSocket = MainServer.getClients()
										.get(myFriend.getIp());
								print("InformLeave线程准备将消息发给：", friendSocket);
								ObjectOutputStream toFriend = new ObjectOutputStream(
										friendSocket.getOutputStream());
								print("InformLeave线程获取输入输出流成功！");

								Msg message = new Msg();
								message.setHead(Option.USER_LEAVE);

								UserLeaveType ult = new UserLeaveType();
								ult.setLeaveUserId(g_userId);
								message.setBody(ult);

								print("准备向用户发送好友下线信息");
								toFriend.writeObject(message);
								toFriend.flush();
								print("好友下线信息发送完毕");
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
					}
					serverDao.deleteMemberByIp(getLoggerIp());
					stopThread = true;

					print("通知用户有好友下线的线程启动完毕！");
					outToClient.close();
					inFromClient.close();
					break;

				/*********************************************************************/
				/************************* 接收到用户登录消息 *************************/
				/*********************************************************************/
				case 1:// 用户登录
					LoginType loginType = (LoginType) msg.getBody();// 获取登陆用户信息报文
					LoginReturnType returnType = new LoginReturnType();// 定义回复报文

					User user = userDao.findUserByIdAndPwd(loginType.getUid(),
							loginType.getPwd());// 判断用户名密码是否正确

					if (null != user.getId()) {// 用户输入正确
						returnType.setResult(true);
						returnType.setLogIp(getLoggerIp());// 将用户的公网IP回复给用户
						UserInfo uInfo = userInfoDao.findUserInfoByUid(user
								.getUid());
						UserInfoResponseType responseInfo = new UserInfoResponseType();
						g_userId = uInfo.getUid().getUid();

						responseInfo.setUid(uInfo.getUid().getUid());
						responseInfo.setNickname(uInfo.getNickname());
						responseInfo.setScratch(uInfo.getScratch());
						responseInfo.setBirthday(uInfo.getBirthday());
						responseInfo.setGender(uInfo.getGender());
						responseInfo.setPicture(uInfo.getPicture());
						responseInfo.setCountry(uInfo.getCountry());
						responseInfo.setProvince(uInfo.getProvince());
						responseInfo.setCity(uInfo.getCity());
						responseInfo.setBlood(uInfo.getBlood());
						responseInfo.setEmail(uInfo.getEmail());
						responseInfo.setDescription(uInfo.getDescription());

						returnType.setUserInfoResponseType(responseInfo);

						print(loginType.getUid() + "正在登录!");

						Server server = new Server();
						server.setUid(loginType.getUid());
						server.setIp(getLoggerIp());
						server.setPort(loginType.getPort());
						server.setState(loginType.getState());
						serverDao.addMember(server);// 在数据库中添加记

						msgReturn.setHead(Option.LOGIN_RETURN);
						msgReturn.setBody(returnType);
						outToClient.writeObject(msgReturn);
						outToClient.flush();

						/** 启动一个线程发送用户所在的群的信息 **/
						new SendGroupInfo(loginType.getUid(), getLoggerIp())
								.start();

						/** 启动一个线程通知用户有好友上线 **/
						new InformFriend(friendDao.findUsersByFid(loginType
								.getUid()), server).start();

					} else {// 用户输入错误
						returnType.setResult(false);
						msgReturn.setHead(Option.LOGIN_RETURN);
						msgReturn.setBody(returnType);
						outToClient.writeObject(msgReturn);
						outToClient.flush();
					}
					break;

				/*********************************************************************/
				/******************* 接收到用户请求获取好友信息消息 ********************/
				/*********************************************************************/
				case 3:
					String uid = (String) msg.getBody();
					List<FriendUnitType> list = new ArrayList<FriendUnitType>();
					List<FriendArray> friends = friendDao.findFriendsByUid(uid);
					if (null != friends) {
						Iterator<FriendArray> it = friends.iterator();
						while (it.hasNext()) {
							FriendArray friend = it.next();
							FriendUnitType fut = new FriendUnitType();
							fut.setUid(friend.getFid().getUid());
							fut.setFakename(friend.getFakename());
							fut.setSignature(userInfoDao.findUserInfoByUid(
									friend.getFid().getUid()).getScratch());
							fut.setNickname(userInfoDao.findUserInfoByUid(
									friend.getFid().getUid()).getNickname());
							fut.setImage(userInfoDao.findUserInfoByUid(
									friend.getFid().getUid()).getPicture());
							Server server = serverDao.findMemberByUid(friend
									.getFid().getUid());
							print("好友目前的在线状态是！！：" + server.getState());
							if (null != server.getId()) {
								fut.setState(server.getState());
								fut.setIp(server.getIp());
								fut.setPort(server.getPort());
							} else {
								fut.setState("0");
							}
							list.add(fut);
						}
					}
					msgReturn.setHead(Option.FRIENDLIST);
					msgReturn.setBody(list);
					outToClient.writeObject(msgReturn);
					outToClient.flush();
					break;

				/*********************************************************************/
				/************************* 接收到查找用户消息 *************************/
				/*********************************************************************/
				case 8:
					SearchUserType searchType = (SearchUserType) msg.getBody();
					List<SearchUserReturnType> onlineMembers = new ArrayList<SearchUserReturnType>();
					if (1 == searchType.getSearchType()) {// 查找在线用户

						List<Server> onlineUsers = serverDao.findAllMembers();// 在server表中查找所有在线用户
						for (Server ou : onlineUsers) {
							if (!ou.getUid().equals(searchType.getSenderUid())) {
								SearchUserReturnType returnUserInfo = new SearchUserReturnType();
								UserInfo ouInfo = userInfoDao
										.findUserInfoByUid(ou.getUid());
								returnUserInfo.setUid(ou.getUid());// 返回信息之UID
								returnUserInfo
										.setNickname(ouInfo.getNickname());// 返回信息之昵称
								returnUserInfo.setCity(ouInfo.getCity());// 返回信息之城市
								returnUserInfo.setIp(ou.getIp());// 返回信息之IP
								onlineMembers.add(returnUserInfo);
							}
						}
					} else if (0 == searchType.getSearchType()) {// 查找特定用户
						Server destinationUser = serverDao
								.findMemberByUid(searchType.getSearchUid());
						if (destinationUser.getUid() != null) {// 用户在线
							SearchUserReturnType returnUserInfo = new SearchUserReturnType();
							UserInfo info = userInfoDao
									.findUserInfoByUid(searchType
											.getSearchUid());
							returnUserInfo.setUid(info.getUid().getUid());
							returnUserInfo.setNickname(info.getNickname());
							returnUserInfo.setCity(info.getCity());
							returnUserInfo.setIp(destinationUser.getIp());// 返回信息之IP
							onlineMembers.add(returnUserInfo);
						}
					} else {
						print("查找用户出错！");
					}
					msgReturn.setBody(onlineMembers);
					msgReturn.setHead(Option.SEARCH_USER_TYPE);
					outToClient.writeObject(msgReturn);
					outToClient.flush();
					break;

				/*********************************************************************/
				/********************* 接收到用户确认添加好友消息 **********************/
				/*********************************************************************/
				case 11:
					ConfirmResultType cResult = (ConfirmResultType) msg
							.getBody();
					/********* 在数据库中添加好友关系 *********/
					User userR = new User();
					userR.setUid(cResult.getRequestUid());
					User userW = new User();
					userW.setUid(cResult.getWantedUid());

					FriendArray friendR = new FriendArray();
					friendR.setUid(userR);
					friendR.setFid(userW);

					FriendArray friendW = new FriendArray();
					friendW.setUid(userW);
					friendW.setFid(userR);

					friendDao.addFriend(friendR);
					friendDao.addFriend(friendW);

					/** ========更新新的好友消息======= **/
					Server serverR = serverDao.findMemberByUid(cResult
							.getRequestUid());
					Server serverW = serverDao.findMemberByUid(cResult
							.getWantedUid());

					// new AddFriend(friendW, serverR).start();
					new AddFriend(friendR, serverW).start();

					msg.setHead(Option.FRIENDLOGIN);
					FriendUnitType fut = new FriendUnitType();

					fut.setUid(serverR.getUid());
					fut.setIp(serverR.getIp());
					fut.setPort(serverR.getPort());
					fut.setState(serverR.getState());
					fut.setImage(userInfoDao
							.findUserInfoByUid(serverR.getUid()).getPicture());
					fut.setNickname(userInfoDao.findUserInfoByUid(
							serverR.getUid()).getNickname());
					msg.setBody(fut);
					outToClient.writeObject(msg);
					outToClient.flush();
					break;

				/**********************************************************************/
				/*********************** 发送查询群时的简略信息 ************************/
				/**********************************************************************/
				case 14:
					SearchGroupType searchGType = (SearchGroupType) msg
							.getBody();
					SearchGroupReturnType searchReturnInfo = new SearchGroupReturnType();
					GroupInfo groupInfo = groupInfoDao
							.findGroupInfoByGid(searchGType.getRequestGid());
					print("所要查找的群的gid：" + searchGType.getRequestGid());
					if (groupInfo.getId() != null) {
						searchReturnInfo.setCreatorUid(groupInfo
								.getCreatorUser().getUid());
						searchReturnInfo.setGid(groupInfo.getGid());
						searchReturnInfo.setDescription(groupInfo
								.getDescription());
						searchReturnInfo.setGname(groupInfo.getGname());

						Server server = serverDao.findMemberByUid(groupInfo
								.getCreatorUser().getUid());
						if (null != server.getId()) {
							searchReturnInfo.setCreatorState(server.getState());
							searchReturnInfo.setCreatorIp(server.getIp());
							searchReturnInfo.setCreatorPort(server.getPort());
						} else {
							searchReturnInfo.setCreatorState("0");
						}
						print("群的creatorIp:" + searchReturnInfo.getCreatorIp());
						print("群的creatorUid:"
								+ groupInfo.getCreatorUser().getUid());
						print("群的gid:" + groupInfo.getGid());
						print("群的description" + groupInfo.getDescription());
						print("群的gname" + groupInfo.getGname());
					}
					msg.setBody(searchReturnInfo);
					msg.setHead(Option.SEARCH_GROUP_RETURN_TYPE);
					outToClient.writeObject(msg);
					outToClient.flush();
					print("所要查询的群的信息发送完毕");
					break;

				/**********************************************************************/
				/************************** 在群中添加新成员 ***************************/
				/**********************************************************************/
				case 17:
					GroupResultType grt = (GroupResultType) msg.getBody();

					/** 在数据库的GroupRelation表中添加新成员 **/
					GroupRelation newRecord = new GroupRelation();

					GroupInfo g = new GroupInfo();
					g.setGid(grt.getGid());
					newRecord.setGid(g);

					User u = new User();
					u.setUid(grt.getNewComerUid());
					newRecord.setUid(u);

					groupRelationDao.addMember(newRecord);
					print("在群" + grt.getGid() + "中添加新成员" + grt.getNewComerUid()
							+ "成功!");

					/** 通知群中的成员有新成员加入 **/
					List<GroupRelation> members = groupRelationDao
							.findMembersByGroupId(grt.getGid());
					for (GroupRelation gr : members) {
						Server onlineOrNot = serverDao.findMemberByUid(gr
								.getUid().getUid());
						if (onlineOrNot.getIp() == null) {
							continue;
						} else {
							/** 启动一个线程发送用户所在的群的信息 **/
							new SendGroupInfo(gr.getUid().getUid(), onlineOrNot
									.getIp()).start();
						}
					}
					msgReturn.setHead(Option.MSG_REPLY);
					outToClient.writeObject(msgReturn);
					outToClient.flush();
					print("群信息发送完毕");
					break;

				/**********************************************************************/
				/************************** 发送群的詳細信息 ***************************/
				/**********************************************************************/
				case 18:
					GroupInfoRequest groupInfoRequest = (GroupInfoRequest) msg
							.getBody();
					GroupInfo gInfo = groupInfoDao
							.findGroupInfoByGid(groupInfoRequest.getGid());
					GroupInfoType gInfoType = new GroupInfoType();
					gInfoType.setCreatorUid(gInfo.getCreatorUser().getUid());
					gInfoType.setGid(gInfo.getGid());
					gInfoType.setGname(gInfo.getGname());
					gInfoType.setNotice(gInfo.getNotice());
					gInfoType.setDescription(gInfo.getDescription());
					gInfoType.setSize(gInfo.getSize());
					msgReturn.setHead(Option.GROUP_INFO_RESPONSE);
					msgReturn.setBody(gInfoType);
					outToClient.writeObject(msgReturn);
					outToClient.flush();
					print("群信息发送完毕");
					break;

				/**********************************************************************/
				/************************** 发送用户的詳細信息 *************************/
				/**********************************************************************/
				case 20:
					UserInfoRequestType uirt = (UserInfoRequestType) msg
							.getBody();
					UserInfo uInfo = userInfoDao.findUserInfoByUid(uirt
							.getRequestId());
					UserInfoResponseType responseInfo = new UserInfoResponseType();
					responseInfo.setUid(uInfo.getUid().getUid());
					responseInfo.setNickname(uInfo.getNickname());
					responseInfo.setScratch(uInfo.getScratch());
					responseInfo.setBirthday(uInfo.getBirthday());
					responseInfo.setGender(uInfo.getGender());
					responseInfo.setPicture(uInfo.getPicture());
					responseInfo.setCountry(uInfo.getCountry());
					responseInfo.setProvince(uInfo.getProvince());
					responseInfo.setCity(uInfo.getCity());
					responseInfo.setBlood(uInfo.getBlood());
					responseInfo.setEmail(uInfo.getEmail());
					responseInfo.setDescription(uInfo.getDescription());

					msgReturn.setHead(Option.USER_INFO_RESPONSE);
					msgReturn.setBody(responseInfo);
					outToClient.writeObject(msgReturn);
					outToClient.flush();
					print("所请求的" + uirt.getRequestId() + "的用户信息发送完毕");
					break;

				/*********************************************************************/
				/********************* 接收到用户修改个人信息消息 **********************/
				/*********************************************************************/
				case 22:
					SubmitUserInfoType suit = (SubmitUserInfoType) msg
							.getBody();

					User newUser = new User();
					newUser.setUid(suit.getUid());

					UserInfo newInfo = new UserInfo();
					newInfo.setId(userInfoDao.findUserInfoByUid(suit.getUid())
							.getId());
					newInfo.setUid(newUser);
					newInfo.setCountry(suit.getCountry());
					newInfo.setProvince(suit.getProvince());
					newInfo.setCity(suit.getCity());
					newInfo.setBirthday(new Date(
							suit.getBirthday().getYear() - 1900, suit
									.getBirthday().getMonth(), suit
									.getBirthday().getDate()));
					newInfo.setBlood(suit.getBlood());
					newInfo.setScratch(suit.getScratch());
					newInfo.setDescription(suit.getDescription());
					newInfo.setEmail(suit.getEmail());
					newInfo.setGender(suit.getGender());
					newInfo.setNickname(suit.getNickname());
					newInfo.setPhone(suit.getPhone());
					newInfo.setPicture(suit.getPicture());

					newInfo.setPwdQuestion(userInfoDao.findUserInfoByUid(
							suit.getUid()).getPwdQuestion());
					newInfo.setPwdAnswer(userInfoDao.findUserInfoByUid(
							suit.getUid()).getPwdAnswer());

					userInfoDao.updateUserInfo(newInfo);

					msgReturn.setHead(Option.MSG_REPLY);
					msgReturn.setBody("OK");
					outToClient.writeObject(msgReturn);
					outToClient.flush();
					print("修改信息确认发送完毕");
					break;
				/*********************************************************************/
				/******************** 接收到群创建者修改群信息消息 *********************/
				/*********************************************************************/
				case 27:
					SubmitGroupInfoType sgit = (SubmitGroupInfoType) msg
							.getBody();

					GroupInfo oldgInfo = groupInfoDao.findGroupInfoByGid(sgit
							.getGid());
					GroupInfo gInfomation = new GroupInfo();

					gInfomation.setCreatorUser(oldgInfo.getCreatorUser());
					gInfomation.setDescription(sgit.getDescription());
					gInfomation.setGid(sgit.getGid());
					gInfomation.setGname(sgit.getGName());
					gInfomation.setId(oldgInfo.getId());
					gInfomation.setNotice(sgit.getNotice());

					groupInfoDao.updateUserInfo(gInfomation);

					msgReturn.setHead(Option.MSG_REPLY);
					msgReturn.setBody("OK");
					outToClient.writeObject(msgReturn);
					outToClient.flush();
					print("修改群信息确认发送完毕");
					break;
				default:
					print("收到非法信息");
					break;
				}
			}
		} catch (Exception e) {
			stopThread = false;
			e.printStackTrace();
		} finally {
			try {
				// /** 启动一个线程通知用户有好友下线 **/
				// new InformLeave(friendDao.findUsersByFid(userId),
				// getLoggerIp())
				// .start();

				List<FriendArray> userList = friendDao.findUsersByFid(g_userId);
				try {
					for (FriendArray f : userList) {
						Server myFriend = serverDao.findMemberByUid(f.getUid()
								.getUid());
						if (null != myFriend.getIp()) {
							print("将要通知本人下线消息的好友为：" + myFriend.getUid());
							Socket friendSocket = MainServer.getClients().get(
									myFriend.getIp());
							print("InformLeave线程准备将消息发给：", friendSocket);
							ObjectOutputStream toFriend = new ObjectOutputStream(
									friendSocket.getOutputStream());
							print("InformLeave线程获取输入输出流成功！");

							Msg message = new Msg();
							message.setHead(Option.USER_LEAVE);

							UserLeaveType ult = new UserLeaveType();
							ult.setLeaveUserId(g_userId);
							message.setBody(ult);

							print("准备向用户发送好友下线信息");
							toFriend.writeObject(message);
							toFriend.flush();
							print("好友下线信息发送完毕");
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
				}

				print("通知用户有好友下线的线程启动完毕！");
				serverDao.deleteMemberByIp(getLoggerIp());// 从在线用户表中删除该用户
				MainServer.getClients().remove(connectionSocket);// 删除用户socket记录
				connectionSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*********************************************************************/
	/*********** 内部线程类：通知用户的所有在线好友该用户上线了 *************/
	/*********************************************************************/
	class InformFriend extends Thread {
		private List<FriendArray> userList;
		private Server loggerServer;

		public InformFriend(List<FriendArray> list, Server loggerServer) {
			this.userList = list;
			this.loggerServer = loggerServer;
		}

		public void run() {
			print("InformFriend线程启动！");
			try {
				Thread.sleep(1000);
				ServerDAO serverDao = new ServerDAOImpl();
				UserInfoDAO userInfoDao = new UserInfoDAOImpl();
				for (FriendArray f : userList) {
					Server myFriend = serverDao.findMemberByUid(f.getUid()
							.getUid());
					if (null != myFriend.getIp()) {
						print("将要通知的好友的号码为：" + myFriend.getUid());
						Socket friendSocket = MainServer.getClients().get(
								myFriend.getIp());
						print("InformFriend线程准备将消息发给：", friendSocket);
						ObjectOutputStream toFriend = new ObjectOutputStream(
								friendSocket.getOutputStream());

						print("InformFriend线程获取输入输出流成功！");
						Msg message = new Msg();
						message.setHead(Option.FRIENDLOGIN);
						FriendUnitType fut = new FriendUnitType();
						print("传递info:" + loggerServer.getUid());
						print("传递info:" + loggerServer.getIp());
						print("传递info:" + loggerServer.getPort());
						print("传递info:" + loggerServer.getState());
						print("传递info:"
								+ userInfoDao.findUserInfoByUid(
										loggerServer.getUid()).getPicture());
						print("传递info:"
								+ userInfoDao.findUserInfoByUid(
										loggerServer.getUid()).getNickname());

						fut.setUid(loggerServer.getUid());
						fut.setIp(loggerServer.getIp());
						fut.setPort(loggerServer.getPort());
						fut.setState(loggerServer.getState());
						fut.setImage(userInfoDao.findUserInfoByUid(
								loggerServer.getUid()).getPicture());
						fut.setNickname(userInfoDao.findUserInfoByUid(
								loggerServer.getUid()).getNickname());
						// fut.setFakename();这个暂未实现
						message.setBody(fut);
						print("准备向用户发送好友上线信息.");
						toFriend.writeObject(message);
						toFriend.flush();
						print("好友上线信息发送完毕.");
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {

			}
		}
	}

	/*********************************************************************/
	/*********** 内部线程类：通知用户的所有在线好友该用户下线了 *************/
	/*********************************************************************/
	class InformLeave extends Thread {
		private List<FriendArray> userList;
		private String leaverIP;

		public InformLeave(List<FriendArray> list, String leaverIP) {
			this.userList = list;
			this.leaverIP = leaverIP;
		}

		public void run() {
			print("InformLeave线程启动！");
			try {
				// Thread.sleep(1000);
				ServerDAO serverDao = new ServerDAOImpl();
				for (FriendArray f : userList) {
					Server myFriend = serverDao.findMemberByUid(f.getUid()
							.getUid());
					if (null != myFriend.getIp()) {
						print("将要通知本人下线消息的好友为：" + myFriend.getUid());
						Socket friendSocket = MainServer.getClients().get(
								myFriend.getIp());
						print("InformLeave线程准备将消息发给：", friendSocket);
						ObjectOutputStream toFriend = new ObjectOutputStream(
								friendSocket.getOutputStream());
						print("InformLeave线程获取输入输出流成功！");

						Msg message = new Msg();
						message.setHead(Option.USER_LEAVE);

						UserLeaveType ult = new UserLeaveType();
						ult.setLeaveUserId(g_userId);
						message.setBody(ult);

						print("准备向用户发送好友下线信息");
						toFriend.writeObject(message);
						toFriend.flush();
						print("好友下线信息发送完毕");
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
			}
		}
	}

	/*********************************************************************/
	/*************** 内部线程类：发送上线好友所在的群信息 ******************/
	/*********************************************************************/
	class SendGroupInfo extends Thread {
		private String userId;
		private String userIp;

		public SendGroupInfo(String userId, String userIp) {
			this.userId = userId;
			this.userIp = userIp;
		}

		public void run() {
			print("SendGroupInfo线程启动！");
			ServerDAO serverDao = new ServerDAOImpl();
			UserInfoDAO userInfoDao = new UserInfoDAOImpl();
			GroupInfoDAO groupInfoDao = new GroupInfoDAOImpl();
			GroupRelationDAO groupRelationDao = new GroupRelationDAOImpl();
			try {
				Thread.sleep(1500);
				print("准备发送用户所在的群的信息");
				List<GroupRelation> groups = groupRelationDao
						.findGroupIdByUid(userId);// 获取用户所在的所有群
				if (groups != null) {
					print("用户至少有一个群");
					List<GroupUnitType> groupInformations = new ArrayList<GroupUnitType>();// 所要返回的消息体
					for (GroupRelation group : groups) {
						GroupUnitType gut = new GroupUnitType();

						GroupInfo gInfo = groupInfoDao.findGroupInfoByGid(group
								.getGid().getGid());
						List<GroupRelation> members = groupRelationDao
								.findMembersByGroupId(group.getGid().getGid());
						Vector<FriendUnitType> memberInfos = new Vector<FriendUnitType>();
						for (GroupRelation gr : members) {
							FriendUnitType fut = new FriendUnitType();
							fut.setUid(gr.getUid().getUid());

							fut.setNickname(userInfoDao.findUserInfoByUid(
									gr.getUid().getUid()).getNickname());
							fut.setImage(userInfoDao.findUserInfoByUid(
									gr.getUid().getUid()).getPicture());
							Server onlineInfo = serverDao.findMemberByUid(gr
									.getUid().getUid());
							if (null != onlineInfo.getId()) {
								fut.setState(onlineInfo.getState());
								fut.setIp(onlineInfo.getIp());
								fut.setPort(onlineInfo.getPort());
							} else {
								fut.setState("0");
							}
							memberInfos.add(fut);
						}
						gut.setMemberList(memberInfos);// 设置返回信息中的群成员
						gut.setGName(gInfo.getGname());// 设置返回信息中的群名称
						gut.setGid(group.getGid().getGid());// 设置返回信息中的群账号
						gut.setNotice(gInfo.getNotice());// 设置返回信息中的群公告
						groupInformations.add(gut);
						print("一个群的信息获取完毕，该群的群账号为" + group.getGid().getGid());
					}
					Msg msgReturn = new Msg();
					msgReturn.setHead(Option.GROUP_UNIT_TYPE);
					msgReturn.setBody(groupInformations);

					Socket groupInfoSocket = MainServer.getClients()
							.get(userIp);
					print("SendGroupInfo线程准备将消息发给：" + groupInfoSocket);
					ObjectOutputStream outToClient = new ObjectOutputStream(
							groupInfoSocket.getOutputStream());

					print("SendGroupInfo线程获取输入输出流成功！");
					outToClient.writeObject(msgReturn);
					outToClient.flush();
					print("用户所在群的所有信息发送完毕");

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/*********************************************************************/
	/******************* 内部线程类：用来添加新的好友关系 ******************/
	/*********************************************************************/
	class AddFriend extends Thread {
		private FriendArray friend;
		private Server loggerServer;

		public AddFriend(FriendArray friend, Server loggerServer) {
			this.friend = friend;
			this.loggerServer = loggerServer;
		}

		public void run() {
			try {
				Thread.sleep(1000);
				ServerDAO serverDao = new ServerDAOImpl();
				UserInfoDAO userInfoDao = new UserInfoDAOImpl();
				Server myFriend = serverDao.findMemberByUid(friend.getUid()
						.getUid());
				print("将要通知的好友的号码为：" + myFriend.getUid());
				Socket friendSocket = MainServer.getClients().get(
						myFriend.getIp());
				print("InformFriend线程准备将消息发给：" + friendSocket);
				ObjectOutputStream toFriend = new ObjectOutputStream(
						friendSocket.getOutputStream());

				print("InformFriend线程获取输入输出流成功！");
				Msg message = new Msg();
				message.setHead(Option.FRIENDLOGIN);
				FriendUnitType fut = new FriendUnitType();

				print("传递info:" + loggerServer.getUid());
				print("传递info:" + loggerServer.getIp());
				print("传递info:" + loggerServer.getPort());
				print("传递info:" + loggerServer.getState());
				print("传递info:"
						+ userInfoDao.findUserInfoByUid(loggerServer.getUid())
								.getPicture());
				print("传递info:"
						+ userInfoDao.findUserInfoByUid(loggerServer.getUid())
								.getNickname());

				fut.setUid(loggerServer.getUid());
				fut.setIp(loggerServer.getIp());
				fut.setPort(loggerServer.getPort());
				fut.setState(loggerServer.getState());
				fut.setImage(userInfoDao.findUserInfoByUid(
						loggerServer.getUid()).getPicture());
				fut.setNickname(userInfoDao.findUserInfoByUid(
						loggerServer.getUid()).getNickname());
				fut.setSignature(userInfoDao.findUserInfoByUid(
						loggerServer.getUid()).getScratch());
				message.setBody(fut);
				print("准备向用户发送好友上线信息.");
				toFriend.writeObject(message);
				toFriend.flush();
				print("好友上线信息发送完毕.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
