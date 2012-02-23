package scu.im.transfile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketConnection extends Thread {
	private Socket client;
	private String filepath;

	public SocketConnection(Socket client, String filepath) {
		this.client = client;
		this.filepath = filepath;
	}

	public void run() {
		if (client == null)
			return;

		DataInputStream in = null;
		DataOutputStream writer = null;

		try {
			// 3、访问Socket对象的getInputStream方法取得客户端发送过来的数据流
			in = new DataInputStream(new BufferedInputStream(client
					.getInputStream()));

			String fileName = in.readUTF(); // 取得附带的文件名

			if (filepath.endsWith("/") == false
					&& filepath.endsWith("\\") == false) {
				filepath += "\\";
			}
			filepath += fileName;

			// 4、将数据流写到文件中
			writer = new DataOutputStream(new BufferedOutputStream(
					new BufferedOutputStream(new FileOutputStream(new File(
							filepath)))));

			int bufferSize = 2048;
			byte[] buf = new byte[bufferSize];

			int read = 0;
			while ((read = in.read(buf)) != -1) {
				writer.write(buf, 0, read);
			}

			writer.flush();
			System.out.println("数据接收完毕");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				in.close();
				writer.close();
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}