package scu.im.voice;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class Capture implements Runnable {

	// TargetDataLine 接口提供从目标数据行的缓冲区读取所捕获数据的方法。
	TargetDataLine line;
	Thread thread;
	Socket s;
	BufferedOutputStream captrueOutputStream;

	public Capture(Socket s) {// 构造器 取得socket以获得网络输出流
		this.s = s;
	}

	public void start() {
		thread = new Thread(this);
		thread.setName("Capture");
		thread.start();
	}

	public void stop() {
		thread = null;
	}

	public void run() {

		try {
			captrueOutputStream = new BufferedOutputStream(s.getOutputStream());// 建立输出流
		} catch (IOException ex) {
			// return;
		}

		// 构造具有线性 PCM（脉冲编码调制） 编码和给定参数的 AudioFormat
		AudioFormat format = new AudioFormat(8000, 16, 2, true, true);
		// 根据指定信息构造数据行的信息对象，这些信息包括单个音频格式
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		try {
			// 获得与指定 Line.Info 对象中的描述匹配的行
			line = (TargetDataLine) AudioSystem.getLine(info);
			// 打开具有指定格式和请求缓冲区大小的行，这样可使行获得所有所需的系统资源并变得可操作
			line.open(format, line.getBufferSize());
		} catch (Exception ex) {
			return;
		}

		byte[] data = new byte[1024];// 此处的1024可以情况进行调整，应跟下面的1024应保持一致
		int numBytesRead = 0;
		line.start();

		while (thread != null) {
			// 从数据行的输入缓冲区读取音频数据
			numBytesRead = line.read(data, 0, 1024);// 取数据（1024）的大小，直接关系到传输的速度，
			// 一般越小越快，
			try {
				captrueOutputStream.write(data, 0, numBytesRead);// 写入网络流
			} catch (Exception ex) {
				break;
			}
		}
		line.stop();
		line.close();
		line = null;
		try {
			captrueOutputStream.flush();
			captrueOutputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}