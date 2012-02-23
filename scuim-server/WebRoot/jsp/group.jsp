<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.util.Date" import="scu.im.bean.User"
	import="scu.im.bean.UserInfo" import="scu.im.bean.GroupRelation"
	import="scu.im.dao.GroupNumberDAO" import="org.hibernate.Session"
	import="org.hibernate.Transaction" import="scu.im.utils.HibernateUtils"
	import="scu.im.bean.GroupInfo"
	import="scu.im.dao.impl.GroupNumberDAOImpl" errorPage=""%>

<%
	request.setCharacterEncoding("GBK");
	//获取Flash传过来的值；
	//用户信息
	String Username = request.getParameter("useraccount");//帐号
	String Password = request.getParameter("userpassword");//密码

	//群信息
	String Groupname = request.getParameter("usergroupname");//群名称
	//群类别
	String FirsSort = request.getParameter("userfirstsort");//一级群类别
	String SecondSort = request.getParameter("usersecondsort");//二级群类别

	String Tags = request.getParameter("usertags");//群标签
	String Introduction = request.getParameter("userintroduction");//群简介

	//身份验证，取值为1，2，3
	//1：允许任何人加入该群
	//2：需要身份验证才能加入该群
	//3：不允许任何人加入该群 
	String Authentication = request.getParameter("userauthentication");

	//访问权限，取值为1，2
	//1：允许任何人访问群空间
	//2：只有群成员能访问群空间
	String Permissions = request.getParameter("userpermissions");

	System.out.println("Username: " + Username);
	System.out.println("Groupname: " + Groupname);
	System.out.println("Tags: " + Tags);
	System.out.println("Introduction: " + Introduction);

	//将值传送给服务器；
	//返回群ID给flash，将值付给newNumber
	String newNumber = "";
	Session groupSession = null;
	Transaction transaction = null;
	try {
		groupSession = HibernateUtils.getSession();
		transaction = groupSession.beginTransaction();

		GroupNumberDAO account = new GroupNumberDAOImpl();
		newNumber = account.generateGroupNumber();

		User creator = new User();
		creator.setUid(Username);

		GroupInfo gInfo = new GroupInfo();
		gInfo.setCreatorUser(creator);
		gInfo.setDescription(Introduction);
		gInfo.setGid(newNumber);
		gInfo.setGname(Groupname);
		gInfo.setSize(100);
		groupSession.save(gInfo);
		transaction.commit();

		transaction = groupSession.beginTransaction();
		GroupInfo gi = new GroupInfo();
		gi.setGid(newNumber);
		User u = new User();
		u.setUid(Username);
		GroupRelation gr = new GroupRelation();
		gr.setGid(gi);
		gr.setUid(u);
		groupSession.save(gr);

		transaction.commit();
	} catch (Exception e) {
		transaction.rollback();
		e.printStackTrace();
	} finally {
		HibernateUtils.closeSession(groupSession);
	}

	//返回结果值给flash，将值付给resultFlag
	//成功success
	//失败false
	String resultFlag = "success";

	System.out.println("befor send success");
	out.println("&userback=" + resultFlag + "&");
	out.println("&groupid=" + newNumber + "&");

	System.out.println("after send number: " + newNumber);
%>