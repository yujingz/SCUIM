package scu.im.transfile;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPFileSender {
	private Socket client;
	private boolean connected;

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public TCPFileSender(String host, int port) {
		try {
			client = new Socket(host, port);
			System.out.println("服务器连接成功！");
			this.connected = true;
		} catch (UnknownHostException e) {
			this.connected = false;
			close();
		} catch (IOException e) {
			System.out.println("服务器连接失败！");
			this.connected = false;
			close();
		}
	}

	public void sendFile(File file) {
		DataOutputStream out = null;
		DataInputStream reader = null;
		try {
			if (client == null)
				return;

			File tempFile = file;
			/*
			 * JFileChooser fileChooser = new JFileChooser("C://");
			 * fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); int
			 * retVal = fileChooser.showOpenDialog(null); if ( retVal ==
			 * JFileChooser.APPROVE_OPTION) { tempFile =
			 * fileChooser.getSelectedFile();
			 * System.out.println(tempFile.getPath()); }
			 */

			reader = new DataInputStream(new BufferedInputStream(
					new FileInputStream(tempFile)));

			out = new DataOutputStream(client.getOutputStream());

			out.writeUTF(tempFile.getName());

			int bufferSize = 2048;
			byte[] buf = new byte[bufferSize];

			int read = 0;
			while ((read = reader.read(buf)) != -1) {
				out.write(buf, 0, read);
			}

			out.flush();

		} catch (IOException ex) {
			ex.printStackTrace();
			close();
		} finally {
			try {
				reader.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * public static void main(String[] args) { TCPFileSender client = new
	 * TCPFileSender("222.210.113.203", 9527); TCPFileSender client = new
	 * TCPFileSender("127.0.0.1", 9527); if(client.isConnected()){
	 * client.sendFile(); client.close(); } }
	 */

}