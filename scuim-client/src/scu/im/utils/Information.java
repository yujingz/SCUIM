package scu.im.utils;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

import scu.im.msgtype.FriendUnitType;
import scu.im.msgtype.GroupUnitType;
import scu.im.msgtype.UserInfoResponseType;
import scu.im.service.ListenTCPThread;
import scu.im.service.ListenUDPThread;
import scu.im.window.ChangeFace;
import scu.im.window.ChooseSkin;
import scu.im.window.FriendUnit;
import scu.im.window.Function;
import scu.im.window.GroupInformation;
import scu.im.window.MyScuimFrame;
import scu.im.window.PersonalInformation;
import scu.im.window.SearchAndFind;
import scu.im.window.StartList;
import scu.im.window.TalkToMany;
import scu.im.window.TalkToOneWithStyle;

public class Information {

	private static String ip = "";
	private static int port = 9527;
	private static int udpPort = 9528;
	private static int transFilePort = 9529;
	private static String uid = "";
	private static Hashtable<String, FriendUnitType> friendList = new Hashtable<String, FriendUnitType>();
	private static Hashtable<String, TalkToOneWithStyle> openedWindow = new Hashtable<String, TalkToOneWithStyle>();
	private static Hashtable<String, TalkToMany> openedGroupWindow = new Hashtable<String, TalkToMany>();
	private static Hashtable<String, GroupUnitType> groupList = new Hashtable<String, GroupUnitType>();
	private static Hashtable<String, PersonalInformation> personalInfoHash = new Hashtable<String, PersonalInformation>();
	private static Hashtable<String, GroupInformation> groupInfoHash = new Hashtable<String, GroupInformation>();
	private static Hashtable<String, FriendUnit> friendUnitHash = new Hashtable<String, FriendUnit>();
	private static Hashtable<String, MsgShake> shakingUnit = new Hashtable<String, MsgShake>();

	private static ListenTCPThread listener = null;
	private static ListenUDPThread udpListener = null;
	private static MyScuimFrame mainGui = null;
	private static SearchAndFind searchPanel = null;
	private static UserInfoResponseType userInfo = null;
	private static int themeNo;
	private static ChangeFace changeFace = null;
	private static Function function = null;

	private static ServerSocket voiceRequestServerSocket = null;
	private static ServerSocket voiceResponseServerSocket = null;
	private static Socket voiceRequestClientSocket = null;
	private static Socket voiceResponseClientSocket = null;

	private static String desKey = "";
	private static String accountKey = "SCUIM姚武平郑宇靖彭则荣何琦李国兴301";
	private static boolean messageUnread;

	public static String getAccountKey() {
		return accountKey;
	}

	public static void setAccountKey(String accountKey) {
		Information.accountKey = accountKey;
	}

	public static String getDesKey() {
		return desKey;
	}

	public static void setDesKey(String desKey) {
		Information.desKey = desKey;
	}

	public static Socket getVoiceRequestClientSocket() {
		return voiceRequestClientSocket;
	}

	private static StartList startlist = null;
	private static ChooseSkin chooseSkin = null;

	public static void setVoiceRequestClientSocket(
			Socket voiceRequestClientSocket) {
		Information.voiceRequestClientSocket = voiceRequestClientSocket;
	}

	public static Socket getVoiceResponseClientSocket() {
		return voiceResponseClientSocket;
	}

	public static void setVoiceResponseClientSocket(
			Socket voiceResponseClientSocket) {
		Information.voiceResponseClientSocket = voiceResponseClientSocket;
	}

	public static ServerSocket getVoiceResponseServerSocket() {
		return voiceResponseServerSocket;
	}

	public static void setVoiceResponseServerSocket(
			ServerSocket voiceResponseServerSocket) {
		Information.voiceResponseServerSocket = voiceResponseServerSocket;
	}

	public static ServerSocket getVoiceRequestServerSocket() {
		return voiceRequestServerSocket;
	}

	public static void setVoiceRequestServerSocket(
			ServerSocket voiceRequestServerSocket) {
		Information.voiceRequestServerSocket = voiceRequestServerSocket;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		Information.port = port;
	}

	public static int getUdpPort() {
		return udpPort;
	}

	public static void setUdpPort(int udpPort) {
		Information.udpPort = udpPort;
	}

	public static ListenTCPThread getListener() {
		return listener;
	}

	public static void setListener(ListenTCPThread listener) {
		Information.listener = listener;
	}

	public static Hashtable<String, FriendUnitType> getFriendList() {
		return friendList;
	}

	public static void setFriendList(
			Hashtable<String, FriendUnitType> friendList) {
		Information.friendList = friendList;
	}

	public static String getUid() {
		return uid;
	}

	public static void setUid(String uid) {
		Information.uid = uid;
	}

	public static String getIp() {
		return ip;
	}

	public static void setIp(String ip) {
		Information.ip = ip;
	}

	public static int getTransFilePort() {
		return transFilePort;
	}

	public static void setTransFilePort(int transFilePort) {
		Information.transFilePort = transFilePort;
	}

	public static Hashtable<String, TalkToOneWithStyle> getOpenedWindow() {
		return openedWindow;
	}

	public static void setOpenedWindow(
			Hashtable<String, TalkToOneWithStyle> openedWindow) {
		Information.openedWindow = openedWindow;
	}

	public static ListenUDPThread getUdpListener() {
		return udpListener;
	}

	public static void setUdpListener(ListenUDPThread udpListener) {
		Information.udpListener = udpListener;
	}

	public static Hashtable<String, GroupUnitType> getGroupList() {
		return groupList;
	}

	public static void setGroupList(Hashtable<String, GroupUnitType> groupList) {
		Information.groupList = groupList;
	}

	public static Hashtable<String, TalkToMany> getOpenedGroupWindow() {
		return openedGroupWindow;
	}

	public static void setOpenedGroupWindow(
			Hashtable<String, TalkToMany> openedGroupWindow) {
		Information.openedGroupWindow = openedGroupWindow;
	}

	public static Hashtable<String, PersonalInformation> getPersonalInfoHash() {
		return personalInfoHash;
	}

	public static void setPersonalInfoHash(
			Hashtable<String, PersonalInformation> personalInfoHash) {
		Information.personalInfoHash = personalInfoHash;
	}

	public static Hashtable<String, GroupInformation> getGroupInfoHash() {
		return groupInfoHash;
	}

	public static void setGroupInfoHash(
			Hashtable<String, GroupInformation> groupInfoHash) {
		Information.groupInfoHash = groupInfoHash;
	}

	public static MyScuimFrame getMainGui() {
		return mainGui;
	}

	public static void setMainGui(MyScuimFrame mainGui) {
		Information.mainGui = mainGui;
	}

	public static SearchAndFind getSearchPanel() {
		return searchPanel;
	}

	public static void setSearchPanel(SearchAndFind searchPanel) {
		Information.searchPanel = searchPanel;
	}

	public static UserInfoResponseType getUserInfo() {
		return userInfo;
	}

	public static void setUserInfo(UserInfoResponseType userInfo) {
		Information.userInfo = userInfo;
	}

	public static int getThemeNo() {
		return themeNo;
	}

	public static void setThemeNo(int themeNo) {
		Information.themeNo = themeNo;
	}

	public static StartList getStartListPanel() {
		return startlist;
	}

	public static void setStartListPanel(StartList startListPanel) {
		Information.startlist = startListPanel;
	}

	public static ChooseSkin getChooseSkinPanel() {
		return chooseSkin;
	}

	public static void setChooseskinPanel(ChooseSkin chooseSkinPanel) {
		Information.chooseSkin = chooseSkinPanel;
	}

	public static Hashtable<String, FriendUnit> getFriendUnitHash() {
		return friendUnitHash;
	}

	public static void setFriendUnitHash(
			Hashtable<String, FriendUnit> friendUnitHash) {
		Information.friendUnitHash = friendUnitHash;
	}

	public static Hashtable<String, MsgShake> getShakingUnit() {
		return shakingUnit;
	}

	public static void setShakingUnit(Hashtable<String, MsgShake> shakingUnit) {
		Information.shakingUnit = shakingUnit;
	}

	public static boolean isMessageUnread() {
		return messageUnread;
	}

	public static void setMessageUnread(boolean messageUnread) {
		Information.messageUnread = messageUnread;
	}
	
	public static ChangeFace getChangeFace() {
		return changeFace;
	}

	public static void setChangeFace(ChangeFace changeFacePanel) {
		Information.changeFace = changeFacePanel;
	}

	
	public static Function getFunction() {
		return function;
	}

	public static void setFunction(Function FunctionPanel) {
		Information.function = FunctionPanel;
	}
}
