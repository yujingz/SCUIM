package scu.im.service;

import static scu.im.utils.Print.print;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Hashtable;

import scu.im.msgtype.AddFriendType;
import scu.im.msgtype.ConfirmReturnType;
import scu.im.msgtype.GroupApplyType;
import scu.im.msgtype.GroupResultType;
import scu.im.msgtype.MessageWithAttrib;
import scu.im.msgtype.Msg;
import scu.im.msgtype.TCPFileRequestType;
import scu.im.msgtype.TCPFileResponseType;
import scu.im.msgtype.VoicChatRequestType;
import scu.im.msgtype.VoiceChatResponseType;
import scu.im.utils.Information;
import scu.im.utils.MessageContainer;
import scu.im.utils.MessageRinger;
import scu.im.utils.MsgShake;
import scu.im.window.TalkToOneWithStyle;

public class ListenUDPThread extends Thread {

	public MessageWithAttrib receiveMessage = null;
	private DatagramSocket receiveSocket = null;
	private DatagramPacket receivePacket = null;
	private byte[] receiveBuf = new byte[1000];
	private TalkToOneWithStyle tempTalk = null;
	private String messageSender = null;
	private Hashtable<String, TalkToOneWithStyle> distributeHash;

	public ListenUDPThread() {
		doInit();
		new ReceiveUDPMessage();
		print("UDP信息监听线程初始化完毕！");

	}

	class ReceiveUDPMessage extends Thread {
		public ReceiveUDPMessage() {
			start();
		}

		public void run() {
			print("UDP信息监听线程已启动！");
			while (true) {
				try {
					receiveSocket.receive(receivePacket);
					print("收到packet");
					Msg receiveMsg = (Msg) ByteToObject(receivePacket.getData());
					print("转换为msg");
					switch (receiveMsg.getHead().ordinal()) {
					case 5:
						// 正常接收消息
						print("转换为5");
						new MessageRinger();
						receiveMessage = (MessageWithAttrib) receiveMsg
								.getBody();
						if (receiveMessage.getGid() == null) {
							// 接受正常消息
							distributeHash = Information.getOpenedWindow();
							if (distributeHash.containsKey(receiveMessage
									.getSenderUid())) {
								distributeHash.get(
										receiveMessage.getSenderUid())
										.displayReceivedMessage(receiveMessage);
							} else {
								Information.getMainGui().setMessageUnread(true);
								ArrayList<MessageWithAttrib> tempList = MessageContainer
										.getMessageList();
								tempList.add(receiveMessage);
								if (!Information.getShakingUnit().containsKey(
										receiveMessage.getSenderUid())) {
									new MsgShake(receiveMessage.getSenderUid())
											.start();
								}
								Information.getMainGui().messageBlink();// 窗口闪烁
							}
						} else {
							// 接受群消息
							if (Information.getOpenedGroupWindow().containsKey(
									receiveMessage.getGid())) {
								print("群消息打开窗口");
								Information.getOpenedGroupWindow().get(
										receiveMessage.getGid())
										.displayReceivedMessage(receiveMessage);
							} else {
								Information.getMainGui().setMessageUnread(true);
								ArrayList<MessageWithAttrib> tempList = MessageContainer
										.getMessageList();
								tempList.add(receiveMessage);
								Information.getMainGui().messageBlink();
							}
						}

						break;
					case 6:
						// 发送文件第二步，处理发送文件请求
						print("转换为6");
						new MessageRinger();
						TCPFileRequestType request = (TCPFileRequestType) receiveMsg
								.getBody();
						// 接收传来的TCPFileRequestType
						distributeHash = Information.getOpenedWindow();
						// 引用哈希表，获得当前用户打开了几个聊天窗口
						messageSender = request.getSenderUid();

						if (distributeHash.containsKey(messageSender)) {
							distributeHash.get(messageSender).isFileAccept(
									request);
							// 如果接受文件的用户和发送文件用户的聊天窗口是打开的话
						} else {
							tempTalk = new TalkToOneWithStyle(request);
							tempTalk.isFileAccept(request);
							// 如果接受文件的用户和发送文件用户的聊天窗口没有打开，就打开一个
						}

						// 调用用户聊天界面的isFileAccept函数，反馈是否接受文件
						// 下一步回到talkToOneWithStyle

						break;
					case 7:
						// 传输文件第四步，发送方分析回应
						print("转换为7");
						new MessageRinger();
						TCPFileResponseType receivedResponse = (TCPFileResponseType) receiveMsg
								.getBody();
						// 转换为TCPFileRespinseType
						distributeHash = Information.getOpenedWindow();
						// 获得哈希表
						messageSender = receivedResponse.getSenderUid();

						if (distributeHash.containsKey(messageSender)) {
							distributeHash.get(messageSender).willFileSend(
									receivedResponse);
							// 窗口打开的，直接显示图片
						} else {
							tempTalk = new TalkToOneWithStyle(receivedResponse);
							tempTalk.willFileSend(receivedResponse);
							// 窗口没打开的，创建一个并打开
						}
						// 第四步结束，下一步进入willFileSend

						break;
					/*
					 * case 8:
					 * System.out.println("##########---------->>>：转换为8");
					 * PictureType receivedPic = (PictureType) receiveMsg
					 * .getBody();
					 * 
					 * distributeHash = OpenedTalkWindow.getOpenedWindow();
					 * messageSender = receivedPic.getSenderUid();
					 * 
					 * if (distributeHash.containsKey(messageSender)) {
					 * distributeHash.get(messageSender).insertPic(
					 * receivedPic); // 窗口打开的，直接调用wiiFileSend } else { tempTalk
					 * = new talkToOneWithStyle(receivedPic .getReceiverUid(),
					 * receivedPic .getSenderIp());
					 * tempTalk.insertPic(receivedPic); // 窗口没打开的，创建一个，加入哈希表，并打开
					 * }
					 */
					case 9:
						print("收到添加好友请求");
						new MessageRinger();
						AddFriendType aft = (AddFriendType) receiveMsg
								.getBody();
						Information.getMainGui().inquireAddFriend(aft);

						break;

					case 10:
						print("收到对方是否添加好友信息");
						new MessageRinger();
						ConfirmReturnType crt = (ConfirmReturnType) receiveMsg
								.getBody();
						Information.getMainGui().showAddFriendResult(crt);
						break;
					case 16:
						print("收到申请入群信息");
						new MessageRinger();
						GroupApplyType gat = (GroupApplyType) receiveMsg
								.getBody();
						Information.getMainGui().groupApplyOperation(gat);

						break;
					case 17:
						print("收到入群反馈信息");
						new MessageRinger();
						GroupResultType grt = (GroupResultType) receiveMsg
								.getBody();
						Information.getMainGui().showGroupApplyResult(grt);
						break;
					case 25:
						print("收到语音请求信息");
						new MessageRinger();
						distributeHash = Information.getOpenedWindow();
						VoicChatRequestType vcrt = (VoicChatRequestType) receiveMsg
								.getBody();
						if (distributeHash.containsKey(vcrt.getRequestUid())) {
							distributeHash.get(vcrt.getRequestUid())
									.willStartVoice(vcrt);
							// 窗口打开的，直接显示图片
						} else {
							tempTalk = new TalkToOneWithStyle(vcrt
									.getRequestUid(), vcrt.getRequestIp(), vcrt
									.getRequestName());
							tempTalk.willStartVoice(vcrt);
							// 窗口没打开的，创建一个并打开
						}
						break;
					case 26:
						print("收到语音回报消息");
						new MessageRinger();
						distributeHash = Information.getOpenedWindow();
						VoiceChatResponseType responseVcrt = (VoiceChatResponseType) receiveMsg
								.getBody();
						if (distributeHash.containsKey(responseVcrt
								.getResponseUid())) {
							distributeHash.get(responseVcrt.getResponseUid())
									.startVoice(responseVcrt);
							// 窗口打开的，直接显示图片
						} else {
							tempTalk = new TalkToOneWithStyle(responseVcrt
									.getResponseUid(), responseVcrt
									.getResponseIp(), responseVcrt
									.getResponseName());
							tempTalk.startVoice(responseVcrt);
							// 窗口没打开的，创建一个并打开
						}
						break;
					case 28:
						String stopVoiceUid = (String) receiveMsg.getBody();
						Information.getOpenedWindow().get(stopVoiceUid).stopVoice();
						break;
					case 29:
						String stopVoiceResponse = (String) receiveMsg.getBody();
						Information.getOpenedWindow().get(stopVoiceResponse).stopVoice2();
						default:
							print("不能识别的消息");
						break;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void doInit() {
		try {
			receiveSocket = new DatagramSocket(Information.getUdpPort());
			receivePacket = new DatagramPacket(receiveBuf, receiveBuf.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Object ByteToObject(byte[] receiveBuf) {
		java.lang.Object obj = null;
		try {
			ByteArrayInputStream bi = new ByteArrayInputStream(receiveBuf);
			ObjectInputStream ois = new ObjectInputStream(bi);
			obj = ois.readObject();
			// bi.close();
			// ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static void main(String[] args) {
		new ListenUDPThread().start();
	}
}
