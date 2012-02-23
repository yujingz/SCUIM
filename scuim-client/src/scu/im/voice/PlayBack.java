package scu.im.voice;


import java.io.*;
import javax.sound.sampled.*;
import java.net.*;

public class PlayBack implements Runnable {
	final int bufSize = 16384;
	SourceDataLine line;
	Thread thread;
	Socket s;

	public PlayBack(Socket s) {// 构造器 取得socket以获得网络输入流
		this.s = s;
	}

	public void start() {
		thread = new Thread(this);
		thread.setName("Playback");
		thread.start();
	}

	public void stop() {
		thread = null;
	}

	public void run() {
		AudioFormat format = new AudioFormat(8000, 16, 2, true, true);

		BufferedInputStream playbackInputStream;
		try {
			playbackInputStream = new BufferedInputStream(new AudioInputStream(
					s.getInputStream(), format, 2147483647));// 封装成音频输出流，
		} catch (IOException ex) {
			return;
		}
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		try {
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format, bufSize);
		} catch (LineUnavailableException ex) {
//			return;
		}
		byte[] data = new byte[1024];// 此处数组的大小跟实时性关系不大，可根据情况进行调整
		int numBytesRead = 0;
		line.start();
		while (thread != null) {
			try {
				numBytesRead = playbackInputStream.read(data);
				line.write(data, 0, numBytesRead);
			} catch (IOException e) {
				break;
			}
		}
		if (thread != null) {
			line.drain();
		}
		line.stop();
		line.close();
		line = null;
	}
}
