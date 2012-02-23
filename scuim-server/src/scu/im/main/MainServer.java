/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 服务器端的主程序，用来开启监听端口等待用户连接，并为用户分配子线程处理用户请求。
 * 
 *************************************************************************************************/

package scu.im.main;

import static scu.im.utils.Print.print;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class MainServer extends Thread {

	private static Hashtable<String, Socket> clients = new Hashtable<String, Socket>();

	public static Hashtable<String, Socket> getClients() {
		return clients;
	}

	public static void setClients(Hashtable<String, Socket> clients) {
		MainServer.clients = clients;
	}

	public static void main(String[] args) {
		try {
			ServerSocket welcomeSocket = new ServerSocket(9527);
			print("服务器启动成功，端口号：9527");
			while (true) {
				print("等待用户连接中...");
				Socket connectionSocket = welcomeSocket.accept();
				print("接受新的连接，目标socket为：" + connectionSocket);
				SubServer sub = new SubServer(connectionSocket);
				sub.start();
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
