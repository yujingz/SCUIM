<%@ page contentType="text/html; charset=gbk" language="java"
	import="java.util.List" import="java.util.Date"
	import="scu.im.bean.User" import="scu.im.bean.GroupInfo"
	import="scu.im.dao.UserInfoDAO" import="scu.im.dao.GroupInfoDAO"
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
	//获取Flash传过来的值；将新帖子写入数据库
	String Username = request.getParameter("useraccount");//用户帐号
	//String Password = request.getParameter("userpassword");//用户密码
	String s_groupAccount = request.getParameter("s_groupaccount");//群帐号；
	String newPostsTitle = request.getParameter("newpoststitle");//新帖子标题
	String newPostsConts = request.getParameter("newpostsconts");//新帖子内容

	System.out.println("befor send msg");
	System.out.println("init Username:" + Username);
	System.out.println("init s_groupAccount:" + s_groupAccount);
	System.out.println("init newPostsTitle:" + newPostsTitle);
	System.out.println("init newPostsConts:" + newPostsConts);

	//返回结果给flash，将值付给resultFlag
	//成功success，失败error
	//String resultFlag="";

	//String nGroupName="";//群名称（一个）
	//String nGroupPic="";//群头像（无后缀，指定图片为.jpg格式）（默认为"aaa.jpg"）(一个)
	//String nGroupAccount="";//群帐号（一个值）

	String postsNum = "";//帖子数目，可为0
	//以下均以#隔开（多个值），若为空则为“-”
	String postsId = "";//帖子id
	String postsTitle = "";//标题
	String postsAuthor = "";//作者
	String postsReply = "";//最后回复者的昵称
	String postsTime = "";//回复时间，样例2009-12-01
	String postsView = "";//回复/浏览数目，样例3/20

	//最近浏览群的情况（注意是群）
	String guestNum = ""; //最近浏览的用户数量，上限为9，可为0
	//以下均以#隔开，最多9个，顺序以时间较新为先（多个值）
	String guestName = ""; //最近浏览的用户昵称
	String guestPic = ""; //最近浏览的用户头像图片名称（无后缀，指定图片为.jpg格式）

	//将值传送给服务器；	
	Session newPostSession = null;
	Transaction transaction = null;
	UserDAO userDao = new UserDAOImpl();
	UserInfoDAO userInfoDao = new UserInfoDAOImpl();
	GroupInfoDAO groupInfoDao = new GroupInfoDAOImpl();
	ForumDAO forumDao = new ForumDAOImpl();
	ForumReplyDAO forumReplyDao = new ForumReplyDAOImpl();
	try {
		newPostSession = HibernateUtils.getSession();
		transaction = newPostSession.beginTransaction();

		User u = new User();
		u.setUid(Username);
		GroupInfo gi = new GroupInfo();
		gi.setGid(s_groupAccount);

		Forum f = new Forum();
		f.setAuthorId(u);
		f.setLastReplyer("-");
		f.setContent(newPostsConts);
		f.setGinfo(gi);
		f.setReplyNumber(0);
		f.setScanNumber(0);
		f.setTime(new Date());
		f.setTittle(newPostsTitle);
		newPostSession.save(f);
		transaction.commit();
		
		transaction = newPostSession.beginTransaction();
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
				postsTime += String
						.valueOf(lastReplyTime.getYear() + 1900)
						+ "-"
						+ String.valueOf(lastReplyTime.getMonth() + 1)
						+ "-"
						+ String.valueOf(lastReplyTime.getDate())
						+ "#";
				postsView += String.valueOf(forum.getReplyNumber())
						+ "/" + String.valueOf(forum.getScanNumber())
						+ "#";
			}
		}
		guestNum = "2";
		guestName = "国兴#则二#";
		guestPic = "7#2#";
		transaction.commit();
	} catch (Exception e) {
		transaction.rollback();
		e.printStackTrace();
	} finally {
		HibernateUtils.closeSession(newPostSession);
	}

	//out.println("&userback="+resultFlag+"&");
	//out.println("&ngroupname="+nGroupName+"&");
	//out.println("&ngrouppic="+nGroupPic+"&");
	//out.println("&ngroupaccount="+nGroupAccount+"&");

	System.out.println("Username:" + Username);
	System.out.println("s_groupAccount:" + s_groupAccount);
	System.out.println("newPostsTitle:" + newPostsTitle);
	System.out.println("newPostsConts:" + newPostsConts);

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