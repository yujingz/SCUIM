/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 对象、关系映射的工具类，用来实例化配置表，然后生成session工厂，从而封装session池中的session。
 * 
 *************************************************************************************************/

package scu.im.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

	private static SessionFactory factory;

	static {
		try {
			Configuration cfg = new Configuration().configure();
			factory = cfg.buildSessionFactory();

		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

	public static SessionFactory getSessionFactory() {
		return factory;
	}

	public static Session getSession() {
		return factory.openSession();
	}

	public static void closeSession(Session session) {
		if (session != null) {
			if (session.isOpen()) {
				session.close();
			}
		}
	}
}
