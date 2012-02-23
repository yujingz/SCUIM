package scu.im.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class MessageRinger {

	public MessageRinger() {
		FileInputStream fileau;
		try {
			fileau = new FileInputStream(".\\Misc\\msg.wav");
			AudioStream as = new AudioStream(fileau);
			AudioPlayer.player.start(as);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
