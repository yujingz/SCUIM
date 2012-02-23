package scu.im.transfile;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFileChooser;

public class TCPFileReceiver {
	public static int port = 9529;
	String fileReceivePath = null;
	private JFileChooser receiveChooser;
	private static ServerSocket server = null;

	public TCPFileReceiver() {
		if (server == null) {
			try {
				server = new ServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("服务器启动...");

		try {
			Socket client = server.accept();
			System.out.println("accepted");
			if (client != null) {
				receiveChooser = new JFileChooser("D:\\111");
				receiveChooser
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retVal = receiveChooser.showOpenDialog(null);
				if (retVal != -1) {
					fileReceivePath = receiveChooser.getSelectedFile()
							.getPath();
				}
				new SocketConnection(client, fileReceivePath).start();
			}
			server.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new TCPFileReceiver();
	}

}