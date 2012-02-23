<%@ page contentType="text/html; charset=gbk" language="java"
	import="java.util.List" import="java.util.Date"
	import="scu.im.bean.User" import="scu.im.dao.UserInfoDAO"
	import="scu.im.dao.GroupInfoDAO"
	import="scu.im.dao.impl.UserInfoDAOImpl"
	import="scu.im.dao.impl.GroupInfoDAOImpl" import="scu.im.bean.Forum"
	import="scu.im.bean.ForumReply" import="scu.im.dao.ForumDAO"
	import="scu.im.dao.ForumReplyDAO" import="scu.im.dao.impl.ForumDAOImpl"
	import="scu.im.dao.impl.ForumReplyDAOImpl" import="scu.im.dao.UserDAO"
	import="scu.im.dao.impl.UserDAOImpl" import="org.hibernate.Session"
	import="org.hibernate.Transaction" import="scu.im.utils.HibernateUtils"
	import="scu.im.dao.impl.AccountNumberDAOImpl" errorPage=""%>
<%
	request.setCharacterEncoding("GBK");
	//获取Flash传过来的值；把回复的内容写入数据库
	String Username = request.getParameter("useraccount");//帐号
	//String Password = request.getParameter("userpassword");//密码
	String s_postsId = request.getParameter("s_postsid");//帖子id；
	String nReplyConst = request.getParameter("nreplyconst");//回复内容

	System.out.println("befor send msg");
	System.out.println("init Username:" + Username);
	System.out.println("init s_postsId:" + s_postsId);
	System.out.println("init nReplyConst:" + nReplyConst);

	//返回结果给flash，将值付给resultFlag
	//成功success，失败error
	//String resultFlag="";

	//String nGroupName="";//群名称（一个）
	//String nGroupPic="";//群头像（无后缀，指定图片为.jpg格式）（默认为"aaa.jpg"）(一个)
	//String nGroupAccount="";//群帐号（一个值）

	//帖子内容，若为空则为“-”(单一值，只有选中的帖子)
	String nPostsId = "";//帖子id
	String nPostsTitle = "";//标题
	String nPostsAuthor = "";//作者
	String nPostsReply = "";//最后回复者的昵称
	String nPostsTime = "";//回复时间，样例17:35 2009/12/01
	String nPostsView = "";//回复/浏览数目，样例3/20
	String nPostsConst = "";//帖子内容

	//回复的内容
	String replyNum = "";//回复的数目，可为0
	//以下均以#隔开（多个值）
	String replyName = "";//回复者的昵称
	String replyPic = "";//回复者的头像图片名称（无后缀，指定图片为.jpg格式）
	String replyTime = "";//回复的时间，样例17:35 2009/12/01
	String replyConst = "";//回复的内容

	//最近浏览帖子的情况（注意是帖子）
	String guestNum = ""; //最近浏览的用户数量，上限为9
	//以下均以#隔开，最多9个，顺序以时间较新为先
	String guestName = ""; //最近浏览的用户昵称
	String guestPic = ""; //最近浏览的用户头像图片名称（无后缀，指定图片为.jpg格式）

	//将值传送给服务器；	
	Session replySession = null;
	Transaction transaction = null;
	UserDAO userDao = new UserDAOImpl();
	UserInfoDAO userInfoDao = new UserInfoDAOImpl();
	GroupInfoDAO groupInfoDao = new GroupInfoDAOImpl();
	ForumDAO forumDao = new ForumDAOImpl();
	ForumReplyDAO forumReplyDao = new ForumReplyDAOImpl();
	try {
		replySession = HibernateUtils.getSession();
		transaction = replySession.beginTransaction();

		User u = new User();
		u.setUid(Username);
		ForumReply fr = new ForumReply();
		fr.setContent(nReplyConst);
		fr.setForumId(Integer.parseInt(s_postsId));
		fr.setReplyerId(u);
		fr.setReplyTime(new Date());
		replySession.save(fr);
		transaction.commit();

		transaction = replySession.beginTransaction();
		Forum forum = forumDao.findForumByForumId(s_postsId);
		if (forum != null) {
			nPostsId = s_postsId;

			nPostsTitle = forum.getTittle();
			nPostsAuthor = userInfoDao.findUserInfoByUid(
					forum.getAuthorId().getUid()).getNickname();
			nPostsReply = forum.getLastReplyer();

			Date lastReplyTime = forum.getTime();
			nPostsTime = String.valueOf(lastReplyTime.getHours()) + ":"
					+ String.valueOf(lastReplyTime.getMinutes()) + ":"
					+ String.valueOf(lastReplyTime.getSeconds()) + " "
					+ String.valueOf(lastReplyTime.getYear() + 1900)
					+ "-"
					+ String.valueOf(lastReplyTime.getMonth() + 1)
					+ "-" + String.valueOf(lastReplyTime.getDate());
			nPostsView = String.valueOf(forum.getReplyNumber()) + "/"
					+ String.valueOf(forum.getScanNumber());
			nPostsConst = String.valueOf(forum.getContent());
		}

		List<ForumReply> replys = forumReplyDao
				.findReplysByForumId(Integer.parseInt(s_postsId));
		if (null != replys) {
			replyNum = String.valueOf(replys.size());
			for (ForumReply reply : replys) {
				replyName += userInfoDao.findUserInfoByUid(
						reply.getReplyerId().getUid()).getNickname()
						+ "#";
				replyPic += userInfoDao.findUserInfoByUid(
						reply.getReplyerId().getUid()).getPicture()
						+ "#";
				Date rTime = reply.getReplyTime();
				replyTime += String.valueOf(rTime.getHours()) + ":"
						+ String.valueOf(rTime.getMinutes()) + ":"
						+ String.valueOf(rTime.getSeconds()) + " "
						+ String.valueOf(rTime.getYear() + 1900) + "-"
						+ String.valueOf(rTime.getMonth() + 1) + "-"
						+ String.valueOf(rTime.getDate()) + "#";
				replyConst += reply.getContent() + "#";
			}
		} else {
			replyNum = "0";
		}
		guestNum = "3";
		guestName = "何琦#小郑#则二#";
		guestPic = "2#3#12#";
		transaction.commit();
	} catch (Exception e) {
		transaction.rollback();
		e.printStackTrace();
	} finally {
		HibernateUtils.closeSession(replySession);
	}

	System.out.println("Username:" + Username);
	System.out.println("s_postsId:" + s_postsId);
	System.out.println("nReplyConst:" + nReplyConst);

	//out.println("&userback="+resultFlag+"&");
	//out.println("&ngroupname="+nGroupName+"&");
	//out.println("&ngrouppic="+nGroupPic+"&");
	//out.println("&ngroupaccount="+nGroupAccount+"&");

	out.println("&npostsid=" + nPostsId + "&");
	out.println("&npoststitle=" + nPostsTitle + "&");
	out.println("&npostsauthor=" + nPostsAuthor + "&");
	out.println("&npostsreply=" + nPostsReply + "&");
	out.println("&npoststime=" + nPostsTime + "&");
	out.println("&npostsview=" + nPostsView + "&");
	out.println("&npostsconst=" + nPostsConst + "&");

	System.out.println("&npostsid=" + nPostsId + "&");
	System.out.println("&npoststitle=" + nPostsTitle + "&");
	System.out.println("&npostsauthor=" + nPostsAuthor + "&");
	System.out.println("&npostsreply=" + nPostsReply + "&");
	System.out.println("&npoststime=" + nPostsTime + "&");
	System.out.println("&npostsview=" + nPostsView + "&");
	System.out.println("&npostsconst=" + nPostsConst + "&");

	out.println("&replynum=" + replyNum + "&");
	out.println("&replyname=" + replyName + "&");
	out.println("&replypic=" + replyPic + "&");
	out.println("&replytime=" + replyTime + "&");
	out.println("&replyconst=" + replyConst + "&");

	System.out.println("&replynum=" + replyNum + "&");
	System.out.println("&replyname=" + replyName + "&");
	System.out.println("&replypic=" + replyPic + "&");
	System.out.println("&replytime=" + replyTime + "&");
	System.out.println("&replyconst=" + replyConst + "&");

	out.println("&guestnum=" + guestNum + "&");
	out.println("&guestname=" + guestName + "&");
	out.println("&guestpic=" + guestPic + "&");

	System.out.println("&guestnum=" + guestNum + "&");
	System.out.println("&guestname=" + guestName + "&");
	System.out.println("&guestpic=" + guestPic + "&");

	System.out.println("after send msg");
	System.out.println();
%>