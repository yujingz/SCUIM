<%@ page contentType="text/html; charset=gbk" language="java"
	import="java.util.Date" import="scu.im.bean.User"
	import="scu.im.dao.UserDAO" import="scu.im.dao.GroupRelationDAO"
	import="scu.im.dao.GroupInfoDAO" import="scu.im.dao.UserInfoDAO"
	import="scu.im.dao.impl.UserDAOImpl"
	import="scu.im.dao.impl.UserInfoDAOImpl"
	import="scu.im.dao.impl.GroupRelationDAOImpl"
	import="scu.im.dao.impl.GroupInfoDAOImpl"
	import="org.hibernate.Session" import="org.hibernate.Transaction"
	import="scu.im.utils.HibernateUtils" import="java.util.List"
	import="scu.im.bean.GroupRelation" import="scu.im.bean.GroupInfo"
	import="scu.im.dao.impl.AccountNumberDAOImpl" errorPage=""%>

<%
	request.setCharacterEncoding("GBK");
	//获取Flash传过来的值；
	String Username = request.getParameter("useraccount");//帐号
	String Password = request.getParameter("userpassword");//密码

	//返回结果给flash，将值赋给resultFlag
	//成功success
	//用户名不存在nameerror
	//用户名与密码不匹配passworderror
	//失败error
	String resultFlag = "";

	String creatGroupNum = "";//用户创建的群数目
	String userPic = "";//用户头像图片名称（含后缀如.jpg或.png或.gif）

	String groupNum = "";//加入的群数目
	//以下#隔开，多个值
	String groupAccount = "";//加入的群帐号
	String groupName = "";//加入的群名称
	String groupPic = "";//加入的群图片名称（含后缀如.jpg或.png或.gif）

	//将值传送给服务器；	
	Session loginSession = null;
	Transaction transaction = null;
	UserDAO userDao = new UserDAOImpl();
	UserInfoDAO userInfoDao = new UserInfoDAOImpl();
	User user = userDao.findUserByIdAndPwd(Username, Password);// 判断用户名密码是否正确
	if (null != user.getId()) {
		resultFlag = "success";
		GroupRelationDAO groupRelationDao = new GroupRelationDAOImpl();
		GroupInfoDAO groupInfoDao = new GroupInfoDAOImpl();
		try {
			loginSession = HibernateUtils.getSession();
			transaction = loginSession.beginTransaction();

			userPic = String.valueOf(userInfoDao.findUserInfoByUid(
					Username).getPicture());

			List<GroupRelation> groups = groupRelationDao
					.findGroupIdByUid(Username);
			List<GroupInfo> gInfos = groupInfoDao
					.findGroupByCreatorUid(Username);

			if (groups != null) {
				groupNum = String.valueOf(groups.size());
				for (GroupRelation relation : groups) {
					GroupInfo info = groupInfoDao
							.findGroupInfoByGid(relation.getGid()
									.getGid());
					groupAccount += info.getGid() + "#";
					groupName += info.getGname() + "#";
					groupPic += String.valueOf(userInfoDao
							.findUserInfoByUid(
									info.getCreatorUser().getUid())
							.getPicture())
							+ "#";
				}
			} else {
				groupNum = "0";
			}

			if (gInfos != null) {
				creatGroupNum = String.valueOf(gInfos.size());
			} else {
				creatGroupNum = "0";
			}
			Username=userInfoDao.findUserInfoByUid(Username).getNickname();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtils.closeSession(loginSession);
		}
	} else {
		resultFlag = "passworderror";
	}

	System.out.println("before send msg");

	out.println("&userback=" + resultFlag + "&");
	out.println("&username=" + Username + "&");

	System.out.println("&userback=" + resultFlag + "&");
	System.out.println("&username=" + Username + "&");

	out.println("&creatgroupnum=" + creatGroupNum + "&");
	out.println("&userpic=" + userPic + "&");

	System.out.println("&creatgroupnum=" + creatGroupNum + "&");
	System.out.println("&userpic=" + userPic + "&");

	out.println("&groupnum=" + groupNum + "&");
	out.println("&groupaccount=" + groupAccount + "&");
	out.println("&groupname=" + groupName + "&");
	out.println("&grouppic=" + groupPic + "&");

	System.out.println("&groupnum=" + groupNum + "&");
	System.out.println("&groupaccount=" + groupAccount + "&");
	System.out.println("&groupname=" + groupName + "&");
	System.out.println("&grouppic=" + groupPic + "&");

	System.out.println("after send msg");
	System.out.println();
%>
