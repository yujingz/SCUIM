package scu.im.voice;

import static scu.im.utils.Print.print;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import scu.im.utils.Information;

public class PlayBackSocket extends Thread {
	private int port;

	public PlayBackSocket(int port) {
		this.port = port;
	}

	public void run() {
		try {
			Information.setVoiceRequestServerSocket(new ServerSocket(port));
			Socket cli = Information.getVoiceRequestServerSocket().accept();
			PlayBack player = new PlayBack(cli);
			player.start();
		} catch (IOException e) {
			// e.printStackTrace();
			print("打印出这一句说明语音聊天的监听socket被关闭了");
		}
	}
}
