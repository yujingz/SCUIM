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
	//��ȡFlash��������ֵ��
	String Username = request.getParameter("useraccount");//�ʺ�
	String Password = request.getParameter("userpassword");//����

	//���ؽ����flash����ֵ����resultFlag
	//�ɹ�success
	//�û���������nameerror
	//�û��������벻ƥ��passworderror
	//ʧ��error
	String resultFlag = "";

	String creatGroupNum = "";//�û�������Ⱥ��Ŀ
	String userPic = "";//�û�ͷ��ͼƬ���ƣ�����׺��.jpg��.png��.gif��

	String groupNum = "";//�����Ⱥ��Ŀ
	//����#���������ֵ
	String groupAccount = "";//�����Ⱥ�ʺ�
	String groupName = "";//�����Ⱥ����
	String groupPic = "";//�����ȺͼƬ���ƣ�����׺��.jpg��.png��.gif��

	//��ֵ���͸���������	
	Session loginSession = null;
	Transaction transaction = null;
	UserDAO userDao = new UserDAOImpl();
	UserInfoDAO userInfoDao = new UserInfoDAOImpl();
	User user = userDao.findUserByIdAndPwd(Username, Password);// �ж��û��������Ƿ���ȷ
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
