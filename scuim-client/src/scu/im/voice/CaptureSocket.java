package scu.im.voice;

import static scu.im.utils.Print.print;

import java.io.IOException;
import java.net.Socket;

import scu.im.utils.Information;

public class CaptureSocket extends Thread {
	private String ip;
	private int port;

	public CaptureSocket(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void run() {
		try {
			Information.setVoiceResponseClientSocket(new Socket(ip, port));
			Capture cap = new Capture(Information.getVoiceResponseClientSocket());
			cap.start();
		} catch (IOException e) {
			// e.printStackTrace();
			print("打印出这一句说明语音聊天发送socket被关闭了");
		}
	}
}
