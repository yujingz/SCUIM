package scu.im.socket;

import java.net.Socket;

public class IMSocket {

	private volatile static Socket uniqueSocket;

	private IMSocket() {
	}

	public static Socket getInstance() {
		if (uniqueSocket == null) {
			synchronized (IMSocket.class) {
				if (uniqueSocket == null) {
					try {

						uniqueSocket = new Socket("127.0.0.1", 9527);

					

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return uniqueSocket;
	}
}
