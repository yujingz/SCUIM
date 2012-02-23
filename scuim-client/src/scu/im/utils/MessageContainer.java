package scu.im.utils;

import java.util.ArrayList;

import scu.im.msgtype.MessageWithAttrib;

public class MessageContainer {

	private static ArrayList<MessageWithAttrib> messageList = new ArrayList<MessageWithAttrib>();

	public static ArrayList<MessageWithAttrib> getMessageList() {
		return messageList;
	}

	public static void setMessageList(ArrayList<MessageWithAttrib> messageList) {
		MessageContainer.messageList = messageList;
	}

}
