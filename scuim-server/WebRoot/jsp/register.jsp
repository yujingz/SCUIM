<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.util.Date" import="scu.im.bean.User"
	import="scu.im.bean.UserInfo" 
	import="scu.im.dao.AccountNumberDAO"
	import="org.hibernate.Session"
	import="org.hibernate.Transaction"
	import="scu.im.utils.HibernateUtils"
	import="scu.im.dao.impl.AccountNumberDAOImpl" errorPage=""%>

<%
	request.setCharacterEncoding("GBK");
	//获取Flash传过来的值；
	String Username = request.getParameter("username");//昵称
	String Password = request.getParameter("userpassword");//密码
	String Gender = request.getParameter("usergender");//性别
	//生日
	String Year = request.getParameter("useryear");//年
	String Month = request.getParameter("usermonth");//月
	String Day = request.getParameter("userday");//日
	//所在地
	String Country = request.getParameter("usercontry");//国家
	String Province = request.getParameter("userprovince");//省
	String City = request.getParameter("usercity");//城市

	//将值传送给服务器；
	AccountNumberDAO account = new AccountNumberDAOImpl();
	String newNumber = account.generateAccountNumber();
	
	Session registerSession = null;
		Transaction transaction = null;
		try {
			User newUser = new User();
			newUser.setUid(newNumber);
			newUser.setPwd(Password);
			registerSession = HibernateUtils.getSession();
			transaction = registerSession.beginTransaction();
			
			registerSession.save(newUser);
			
			UserInfo newInfo = new UserInfo();
			newInfo.setUid(newUser);
			newInfo.setGender(Integer.parseInt(Gender));
			newInfo.setBirthday(new Date(Integer.parseInt(Year)-1900, Integer.parseInt(Month), Integer.parseInt(Day)));
			newInfo.setCountry(Country);
			newInfo.setProvince(Province);
			newInfo.setCity(City);
			
			registerSession.save(newInfo);

			transaction.commit();
			} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtils.closeSession(registerSession);
		}

	String resultFlag = "success";
	System.out.println("befor send success");
	out.println("&userback="+resultFlag+"&");
	out.println("&userid=" + newNumber + "&");
	System.out.println("after send number: "+newNumber);
%>