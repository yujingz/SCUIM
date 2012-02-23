<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.util.Date" import="scu.im.bean.User"
	import="scu.im.dao.UserDAO" 
	import="scu.im.dao.UserInfoDAO" 
	import="scu.im.dao.impl.UserDAOImpl" 
	import="scu.im.dao.impl.UserInfoDAOImpl"
	import="org.hibernate.Session"
	import="org.hibernate.Transaction"
	import="scu.im.utils.HibernateUtils"
	import="scu.im.dao.impl.AccountNumberDAOImpl" errorPage=""%>
<%
	request.setCharacterEncoding("GBK");
	//获取Flash传过来的值；
	String Username = request.getParameter("useraccount");//帐号
	String Password = request.getParameter("userpassword");//密码
	
	//返回结果给flash，将值付给resultFlag
	//成功success
	//用户名不存在nameerror
	//用户名与密码不匹配passworderror
	String resultFlag="";

	//返回用户名给flash，将值付给username
	String username="";

	//将值传送给服务器；	
	Session loginSession = null;
	Transaction transaction = null;
	UserDAO userDao = new UserDAOImpl();
	UserInfoDAO userInfoDao = new UserInfoDAOImpl();
	try {
		loginSession = HibernateUtils.getSession();
		transaction = loginSession.beginTransaction();
		User user = userDao.findUserByIdAndPwd(Username,Password);// 判断用户名密码是否正确

		if (null != user.getId()) {
			resultFlag="success";
			username = userInfoDao.findUserInfoByUid(user.getUid()).getNickname();
		}else{
			resultFlag="passworderror";
		}

		transaction.commit();
	} catch (Exception e) {
		transaction.rollback();
		e.printStackTrace();
	} finally {
		HibernateUtils.closeSession(loginSession);
	}

	System.out.println("befor send success");
	out.println("&userback="+resultFlag+"&");
	out.println("&username="+username+"&");
	System.out.println("after send number: "+Username);
%>