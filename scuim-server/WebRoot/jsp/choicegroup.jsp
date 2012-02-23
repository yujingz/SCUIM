<%@ page contentType="text/html; charset=gbk" language="java"
	import="java.util.Date" import="scu.im.bean.User"
	import="java.util.List" import="scu.im.bean.Forum"
	import="scu.im.bean.ForumReply" import="scu.im.bean.GroupInfo"
	import="scu.im.dao.UserDAO" import="scu.im.dao.UserInfoDAO"
	import="scu.im.dao.GroupInfoDAO" import="scu.im.dao.ForumDAO"
	import="scu.im.dao.ForumReplyDAO" import="scu.im.dao.impl.UserDAOImpl"
	import="scu.im.dao.impl.ForumDAOImpl"
	import="scu.im.dao.impl.ForumReplyDAOImpl"
	import="scu.im.dao.impl.UserInfoDAOImpl"
	import="scu.im.dao.impl.GroupInfoDAOImpl"
	import="org.hibernate.Session" import="org.hibernate.Transaction"
	import="scu.im.utils.HibernateUtils" errorPage=""%>

<%
	request.setCharacterEncoding("GBK");
	//��ȡFlash��������ֵ��
	String Username = request.getParameter("useraccount");//�û��ʺ�
	//String Password = request.getParameter("userpassword");//�û�����
	String s_groupAccount = request.getParameter("s_groupaccount");//Ⱥ�ʺţ�

	//���ؽ����flash����ֵ����resultFlag
	//�ɹ�success��ʧ��error
	String resultFlag = "";

	String nGroupName = "";//Ⱥ���ƣ�һ����
	String nGroupPic = "";//Ⱥͷ���޺�׺��ָ��ͼƬΪ.jpg��ʽ����Ĭ��Ϊ"aaa.jpg"��(һ��)
	//String nGroupAccount="";//Ⱥ�ʺţ�һ��ֵ��

	String postsNum = "";//������Ŀ����Ϊ0
	//���¾���#���������ֵ������Ϊ����Ϊ��-��
	String postsId = "";//����id
	String postsTitle = "";//����
	String postsAuthor = "";//����
	String postsReply = "";//���ظ��ߵ��ǳ�
	String postsTime = "";//�ظ�ʱ�䣬����2009-12-01
	String postsView = "";//�ظ�/�����Ŀ������3/20

	//������Ⱥ�������ע����Ⱥ��
	String guestNum = ""; //���������û�����������Ϊ9����Ϊ0
	//���¾���#���������9����˳����ʱ�����Ϊ�ȣ����ֵ��
	String guestName = ""; //���������û��ǳ�
	String guestPic = ""; //���������û�ͷ��ͼƬ���ƣ��޺�׺��ָ��ͼƬΪ.jpg��ʽ��

	//��ֵ���͸���������	
	Session loginSession = null;
	Transaction transaction = null;
	UserDAO userDao = new UserDAOImpl();
	UserInfoDAO userInfoDao = new UserInfoDAOImpl();
	GroupInfoDAO groupInfoDao = new GroupInfoDAOImpl();
	ForumDAO forumDao = new ForumDAOImpl();
	ForumReplyDAO forumReplyDao = new ForumReplyDAOImpl();
	try {
		loginSession = HibernateUtils.getSession();
		transaction = loginSession.beginTransaction();

		GroupInfo gInfo = groupInfoDao
				.findGroupInfoByGid(s_groupAccount);
		nGroupName = gInfo.getGname();
		nGroupPic = String.valueOf(userInfoDao.findUserInfoByUid(
				gInfo.getCreatorUser().getUid()).getPicture());

		List<Forum> forumList = forumDao
				.findForumsByGid(s_groupAccount);
		if (forumList != null) {
			postsNum = String.valueOf(forumList.size());
			for (Forum forum : forumList) {
				postsId += String.valueOf(forum.getForumid()) + "#";
				postsTitle += forum.getTittle() + "#";
				postsAuthor += userInfoDao.findUserInfoByUid(
						forum.getAuthorId().getUid()).getNickname()
						+ "#";
				postsReply += forum.getLastReplyer() + "#";

				Date lastReplyTime = forum.getTime();
				postsTime += String.valueOf(lastReplyTime.getYear()+1900)
						+ "-"
						+ String.valueOf(lastReplyTime.getMonth() + 1)
						+ "-" + String.valueOf(lastReplyTime.getDate())
						+ "#";
				//List<ForumReply> replys = forumReplyDao
				//	.findReplysByForumId(forum.getForumid());

				postsView += String.valueOf(forum.getReplyNumber())
						+ "/" + String.valueOf(forum.getScanNumber())
						+ "#";
			}
		}
		guestNum = "2";
		guestName = "����#���#";
		guestPic = "7#2#";
		transaction.commit();
	} catch (Exception e) {
		transaction.rollback();
		e.printStackTrace();
	} finally {
		HibernateUtils.closeSession(loginSession);
	}

	System.out.println("befor send msg");
	System.out.println("s_groupAccount:" + s_groupAccount);

	out.println("&ngroupname=" + nGroupName + "&");
	out.println("&ngrouppic=" + nGroupPic + "&");
	//out.println("&ngroupaccount="+nGroupAccount+"&");
	System.out.println("&ngroupname=" + nGroupName + "&");
	System.out.println("&ngrouppic=" + nGroupPic + "&");

	out.println("&postsnum=" + postsNum + "&");
	out.println("&postsid=" + postsId + "&");
	out.println("&poststitle=" + postsTitle + "&");
	out.println("&postsauthor=" + postsAuthor + "&");
	out.println("&postsreply=" + postsReply + "&");
	out.println("&poststime=" + postsTime + "&");
	out.println("&postsview=" + postsView + "&");

	System.out.println("&postsnum=" + postsNum + "&");
	System.out.println("&postsid=" + postsId + "&");
	System.out.println("&poststitle=" + postsTitle + "&");
	System.out.println("&postsauthor=" + postsAuthor + "&");
	System.out.println("&postsreply=" + postsReply + "&");
	System.out.println("&poststime=" + postsTime + "&");
	System.out.println("&postsview=" + postsView + "&");

	out.println("&guestnum=" + guestNum + "&");
	out.println("&guestname=" + guestName + "&");
	out.println("&guestpic=" + guestPic + "&");

	System.out.println("&guestnum=" + guestNum + "&");
	System.out.println("&guestname=" + guestName + "&");
	System.out.println("&guestpic=" + guestPic + "&");

	System.out.println("after send msg");
	System.out.println();
%>