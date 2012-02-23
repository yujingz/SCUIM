/*************************************************************************************************
 * @author : 彭则荣
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 软件主界面，功能一堆，不易书写，若需学习了解者，请从速购买本软件，联系电话15882492495
 * 
 * 
 *************************************************************************************************/
package scu.im.window;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import scu.im.msgtype.AddFriendType;
import scu.im.msgtype.ConfirmResultType;
import scu.im.msgtype.ConfirmReturnType;
import scu.im.msgtype.FriendUnitType;
import scu.im.msgtype.GroupApplyType;
import scu.im.msgtype.GroupResultType;
import scu.im.msgtype.GroupUnitType;
import scu.im.msgtype.MessageWithAttrib;
import scu.im.msgtype.Msg;
import scu.im.msgtype.Option;
import scu.im.socket.IMSocket;
import scu.im.utils.Information;
import scu.im.utils.MessageContainer;
import scu.im.utils.MsgShake;
import scu.im.utils.Observer;

import com.sun.jna.examples.WindowUtils;

public class MyScuimFrame extends JDialog implements ActionListener,
		WindowListener, Observer {

	private static final long serialVersionUID = 371953263879074940L;

	// 第一层panel‘背景’及其控件
	ImageIcon image1 = new ImageIcon(".\\image\\2.png");
	ImageIcon image2 = new ImageIcon(".\\image\\21.png");
	ImageIcon image3 = new ImageIcon(".\\image\\face\\"
			+ Information.getUserInfo().getPicture() + ".jpg");
	ImageIcon image4 = new ImageIcon(".\\image\\startlist\\1.png");
	JLabel facelabel = new JLabel(image3);// 头像label
	JLabel backgroundlabel;// 添加背景label
	JButton startbutton = new JButton();// 开始菜单按钮
	JButton searchfriendbutton = new JButton();// 收索好友按钮
	JButton changeskinbutton = new JButton();// 更换皮肤按钮
	JButton minisizebutton = new JButton();// 最小化按钮
	JButton closebutton = new JButton();// 关闭按钮
	ImageIcon roarImage = new ImageIcon(".\\image\\button\\roar.jpg");

	JLabel topLabel = new JLabel();
	JLabel roarLabel = new JLabel(roarImage);
	JLabel nickNameLabel;// name
	JLabel typelabel;// type
	JTextField signtextfield = new JTextField();// 个性签名
	JTextField roarTextfield = new JTextField();// 收索
	JButton friendlistbutton = new JButton();
	JButton grouplistbutton = new JButton();

	// 第二层panel‘好友列表’及其控件
	JPanel friendsPanel = new JPanel();

	// 第三层panel‘群列表’及其控件
	JPanel groupPanel = new JPanel();

	// 加滚动条
	JScrollPane scropane1 = new JScrollPane(friendsPanel);
	JScrollPane scropane2 = new JScrollPane(groupPanel);

	private SearchAndFind searchandfind1 = null;// 搜索好友

	// 各种变量
	private int openCloseStartList = 1;
	private int ocsearchandfind = 1;// 便于控制searchFram开关的数
	private int opencloseCS = 1;
	// 系统托盘所需变量
	private boolean isDraging;
	private int x, y;
	private TrayIcon trayIcon = null; // 托盘图标
	// 自动隐藏所需变量
	private Rectangle rect;// 指定区域
	private int left;// 窗体离屏幕左边的距离
	private int leftmax;
	private int top;// 窗体离屏幕顶部的距离
	private int width;// 窗体的宽
	private int height;// 窗体的高
	private Point point;// 鼠标在窗体的位置
	private Timer timer = new Timer(100, this);// 100毫秒后开启this中的监听事件
	private boolean messageUnread = false;
	private byte[] sendBuf = null;
	private DatagramSocket sender = null;
	private DatagramPacket sendPacket = null;
	private TalkToOneWithStyle tempTalk = null;
	private ChooseSkin chooseSkin = null;
	StartList startList = null;

	// main 方法
	public static void main(String args[]) {
		MyScuimFrame myscuimframe1 = new MyScuimFrame();
		myscuimframe1.setSize(new Dimension(240, 610));// 窗体大小
		myscuimframe1.setResizable(false);// 固定窗体大小
		// myscuimfram1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@SuppressWarnings("unchecked")
	private void initFriendlist() {
		try {
			Socket clientSocket = IMSocket.getInstance();
			ObjectOutputStream outToServer = new ObjectOutputStream(
					clientSocket.getOutputStream());
			ObjectInputStream inFromServer = new ObjectInputStream(clientSocket
					.getInputStream());
			Msg msg = new Msg();
			msg.setHead(Option.FRIENDLIST);
			msg.setBody(Information.getUid());
			outToServer.writeObject(msg);
			outToServer.flush();
			Msg returnMsg = (Msg) inFromServer.readObject();
			ArrayList<FriendUnitType> list = (ArrayList<FriendUnitType>) returnMsg
					.getBody();
			Hashtable<String, FriendUnitType> friendlist = Information
					.getFriendList();
			for (FriendUnitType f : list) {
				scu.im.utils.Print.print("收到好友" + f.getUid() + "的信息如下：");
				scu.im.utils.Print.print("好友信息" + f.getNickname());
				scu.im.utils.Print.print("好友信息" + f.getFakename());
				scu.im.utils.Print.print("好友信息" + f.getState());
				scu.im.utils.Print.print("好友信息" + f.getIp());
				scu.im.utils.Print.print("好友信息" + f.getPort());
				scu.im.utils.Print.print("好友信息" + f.getImage());
				friendlist.put(f.getUid(), f);
				FriendUnit fu1 = new FriendUnit(f);
				friendsPanel.add(fu1);
			}
			Information.setFriendList(friendlist);// 更新好友列表
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MyScuimFrame() {
		Information.setMainGui(this);
		initFriendlist();
		Information.getListener().registerObserver(this);
		Information.setThemeNo(1);
		nickNameLabel = new JLabel(Information.getUserInfo().getNickname());
		nickNameLabel.setFont(new Font(Information.getUserInfo().getNickname(), Font.BOLD, 16));
		typelabel = new JLabel("在线");
		typelabel.setFont(new Font("typeHere", Font.BOLD, 15));
		this.setUndecorated(true); // 去掉标题栏
		this.setLocation(560, 50);// 窗体位置
		miniicon();// 系统托盘方法
		changeSkin(1);// 调用 改变背景方法

		Container container1 = getContentPane();
		JPanel jpanel = new JPanel();
		jpanel.setOpaque(false);
		jpanel.setLayout(null);
		friendsPanel.setLayout(new GridLayout(6, 1, 5, 1));
		groupPanel.setLayout(new GridLayout(6, 1, 5, 1));
		container1.add(jpanel);

		// 增加控件 控件设置 事件
		jpanel.add(facelabel);
		jpanel.add(nickNameLabel);
		jpanel.add(typelabel);

		jpanel.add(startbutton);
		jpanel.add(searchfriendbutton);
		jpanel.add(changeskinbutton);
		jpanel.add(minisizebutton);
		jpanel.add(closebutton);

		jpanel.add(signtextfield);
		jpanel.add(roarTextfield);

		jpanel.add(friendlistbutton);
		jpanel.add(grouplistbutton);
		jpanel.add(topLabel);
		jpanel.add(roarLabel);

		// 基本设置
		image1.setImage(image1.getImage().getScaledInstance(50, 60, 10));// 图片按比例缩放
		image2.setImage(image2.getImage().getScaledInstance(50, 60, 10));

		topLabel.setBounds(0, 0, 170, 30);

		facelabel.setBounds(13, 40, 64, 64);// 位置
		startbutton.setBounds(10, 550, 50, 50);
		startbutton.setContentAreaFilled(false); // 按钮透明
		startbutton.setBorderPainted(false);// 去边框
		startbutton.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\l1.png"));
		searchfriendbutton.setBounds(90, 568, 65, 23);
		searchfriendbutton.setContentAreaFilled(false); // 按钮透明
		searchfriendbutton.setBorderPainted(false);
		searchfriendbutton.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\s1.png"));
		changeskinbutton.setBounds(160, 568, 65, 23);
		changeskinbutton.setContentAreaFilled(false); // 按钮透明
		changeskinbutton.setBorderPainted(false);
		changeskinbutton.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\f1.png"));
		minisizebutton.setBounds(170, 5, 35, 21);
		minisizebutton.setContentAreaFilled(false); // 按钮透明
		minisizebutton.setBorderPainted(false);
		minisizebutton.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\zxh1.png"));
		closebutton.setBounds(200, 5, 35, 21);
		closebutton.setContentAreaFilled(false); // 按钮透明
		closebutton.setBorderPainted(false);
		closebutton.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\close1.png"));

		nickNameLabel.setBounds(100, 40, 65, 20);
		typelabel.setBounds(165, 40, 65, 20);
		signtextfield.setBounds(100, 80, 130, 25);
		signtextfield.setColumns(15);// 设置txet的长度
		signtextfield.setOpaque(isMessageUnread());
		signtextfield.setText(Information.getUserInfo().getScratch());
		roarImage.setImage(roarImage.getImage().getScaledInstance(23, 23, 10));
		roarLabel.setBounds(11, 128, 24, 24);
		roarTextfield.setBounds(37, 128, 190, 24);
		roarTextfield.setColumns(100);
		roarTextfield.setText("输入吼信息");
		friendlistbutton.setBounds(10, 160, 111, 25);
		friendlistbutton.setContentAreaFilled(false); // 按钮透明
		friendlistbutton.setBorderPainted(false);// 去边框
		friendlistbutton.setIcon(new ImageIcon(".\\image\\button\\one2.png"));
		grouplistbutton.setBounds(122, 160, 111, 25);
		grouplistbutton.setContentAreaFilled(false); // 按钮透明
		grouplistbutton.setBorderPainted(false);// 去边框
		grouplistbutton.setIcon(new ImageIcon(".\\image\\button\\many1.png"));

		jpanel.add(scropane1);
		jpanel.add(scropane2);
		scropane2.setBounds(10, 185, 223, 355);
		scropane2.setVisible(false);
		scropane1.setBounds(10, 185, 223, 355);

		// 拖拽或移动鼠标到scuim界面时触发监听事件
		jpanel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent evt) {
				gragHide(evt);
				if (opencloseCS == 0) {
					chooseSkin.setVisible(false);
					chooseSkin = null;
					opencloseCS = 1;

				}

				if (openCloseStartList == 0) {
					// startListLabel.setVisible(false);
					startList.setVisible(false);
					startList = null;
					openCloseStartList = 1;

				}
			}

			public void mouseMoved(MouseEvent evt) {
				moveHide(evt);
			}
		});

		// 移动监听
		topLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isDraging = true;
				x = e.getX();
				y = e.getY();

				if (opencloseCS == 0) {
					chooseSkin.setVisible(false);
					chooseSkin = null;
					opencloseCS = 1;

				}

				if (openCloseStartList == 0) {
					// startListLabel.setVisible(false);
					startList.setVisible(false);
					startList = null;
					openCloseStartList = 1;

				}
			}

			public void mouseReleased(MouseEvent e) {
				isDraging = false;
			}
		});
		topLabel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (isDraging) {
					int left = getLocation().x;
					int top = getLocation().y;
					setLocation(left + e.getX() - x, top + e.getY() - y);
				}
			}
		});

		// 到可拖拽区域 鼠标指针变化
		topLabel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}

			public void mouseExited(MouseEvent evt) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		signtextfield.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				signtextfield.setText(Information.getUserInfo().getScratch());

				signtextfield.setOpaque(false);
			}

			@Override
			public void focusGained(FocusEvent e) {
				signtextfield.setText("");

				signtextfield.setOpaque(true);

				if (opencloseCS == 0) {
					chooseSkin.setVisible(false);
					chooseSkin = null;
					opencloseCS = 1;

				}

				if (openCloseStartList == 0) {

					startList.setVisible(false);
					startList = null;
					openCloseStartList = 1;

				}
			}
		});

		roarTextfield.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				roarTextfield.setText("输入吼信息");

			}

			@Override
			public void focusGained(FocusEvent e) {
				roarTextfield.setText("");

				if (opencloseCS == 0) {
					chooseSkin.setVisible(false);
					chooseSkin = null;
					opencloseCS = 1;

				}

				if (openCloseStartList == 0) {
					startList.setVisible(false);
					startList = null;
					openCloseStartList = 1;

				}

			}
		});

		roarTextfield.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					Msg roarMsg = new Msg();
					MessageWithAttrib mwa = getMessageWithAttrib();
					roarMsg.setHead(Option.MESSAGE_WITH_ATTRIB);
					roarMsg.setBody(mwa);
					mwa.setGid(null);
					mwa.setReceiverUid(null);
					mwa.setText(roarTextfield.getText());

					List<String> toRoarList = new ArrayList<String>();
					for (FriendUnit fu : Information.getFriendUnitHash()
							.values()) {
						if (fu.roarCheckbox.isSelected()) {
							if(Information.getFriendList().get(fu.friendinfo.getUid()).getState().equals("1")){
								toRoarList.add(fu.friendinfo.getIp());
							} else {
								System.out.println("这B不在线，你选了也没有用");
							}						
						}
					}

					if (!toRoarList.isEmpty()) {
						for (String str : toRoarList) {
							try {
								mwa.setSendTime(new Date());
								mwa.setReceiverIp(str);
								sendBuf = null;
								sender = new DatagramSocket();
								System.out.println("当前用户ip为" + str);
								System.out.println("发送内容为" + mwa.getText());
								sendPacket = toDatagram(roarMsg, InetAddress
										.getByName(str), Information
										.getUdpPort());
								sender.send(sendPacket);
							} catch (UnknownHostException e1) {
								e1.printStackTrace();
							} catch (IOException e2) {
								e2.printStackTrace();
							}
						}
					}
					roarTextfield.setText(null);
				}
				

			}
		});

		facelabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				information(evt);
				if (opencloseCS == 0) {
					chooseSkin.setVisible(false);
					chooseSkin = null;
					opencloseCS = 1;

				}

				if (openCloseStartList == 0) {
					startList.setVisible(false);
					startList = null;
					openCloseStartList = 1;

				}
			}
		});
		facelabel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent evt) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		startbutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				startlist(evt);
				if (opencloseCS == 0) {
					chooseSkin.setVisible(false);
					chooseSkin = null;
					opencloseCS = 1;

				}

			}

			public void mouseEntered(MouseEvent evt) {
				startbutton.setIcon(new ImageIcon(".\\image\\button\\l2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				startbutton.setIcon(new ImageIcon(".\\image\\button\\l1.png"));
			}
		});

		searchfriendbutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {

				searchFriends(evt);
				if (opencloseCS == 0) {
					chooseSkin.setVisible(false);
					chooseSkin = null;
					opencloseCS = 1;

				}
				if (openCloseStartList == 0) {
					// startListLabel.setVisible(false);
					startList.setVisible(false);
					startList = null;
					openCloseStartList = 1;

				}

			}

			public void mouseEntered(MouseEvent evt) {
				searchfriendbutton.setIcon(new ImageIcon(
						".\\image\\button\\s2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				searchfriendbutton.setIcon(new ImageIcon(
						".\\image\\button\\s1.png"));
			}
		});

		changeskinbutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {

				chooseSkin(evt);

				if (openCloseStartList == 0) {

					startList.setVisible(false);
					startList = null;
					openCloseStartList = 1;

				}

			}

			public void mouseEntered(MouseEvent evt) {
				changeskinbutton.setIcon(new ImageIcon(
						".\\image\\button\\f2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				changeskinbutton.setIcon(new ImageIcon(
						".\\image\\button\\f1.png"));
			}
		});

		minisizebutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				mini(evt);
				if (opencloseCS == 0) {
					chooseSkin.setVisible(false);
					chooseSkin = null;
					opencloseCS = 1;

				}

				if (openCloseStartList == 0) {
					startList.setVisible(false);
					startList = null;
					openCloseStartList = 1;

				}
			}

			public void mouseEntered(MouseEvent evt) {
				minisizebutton.setIcon(new ImageIcon(
						".\\image\\button\\zxh2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				minisizebutton.setIcon(new ImageIcon(
						".\\image\\button\\zxh1.png"));
			}
		});

		closebutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				close(evt);
			}

			public void mouseEntered(MouseEvent evt) {
				closebutton.setIcon(new ImageIcon(
						".\\image\\button\\close3.png"));
			}

			public void mouseExited(MouseEvent evt) {
				closebutton.setIcon(new ImageIcon(
						".\\image\\button\\close1.png"));
			}
		});

		friendlistbutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				friendsPanel.setVisible(true);
				scropane1.setVisible(true);
				scropane2.setVisible(false);
				groupPanel.setVisible(false);
				if (opencloseCS == 0) {
					chooseSkin.setVisible(false);
					chooseSkin = null;
					opencloseCS = 1;

				}

				if (openCloseStartList == 0) {
					startList.setVisible(false);
					startList = null;
					openCloseStartList = 1;

				}

				friendlistbutton.setIcon(new ImageIcon(
						".\\image\\button\\one2.png"));

				grouplistbutton.setIcon(new ImageIcon(
						".\\image\\button\\many1.png"));
			}

		});
		grouplistbutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				friendsPanel.setVisible(false);
				scropane1.setVisible(false);
				scropane2.setVisible(true);
				groupPanel.setVisible(true);
				if (opencloseCS == 0) {
					chooseSkin.setVisible(false);
					chooseSkin = null;
					opencloseCS = 1;

				}

				if (openCloseStartList == 0) {
					startList.setVisible(false);
					startList = null;
					openCloseStartList = 1;

				}

				friendlistbutton.setIcon(new ImageIcon(
						".\\image\\button\\one1.png"));

				grouplistbutton.setIcon(new ImageIcon(
						".\\image\\button\\many2.png"));
			}
		});

		setVisible(true);// 框架构造完毕
	}

	private void chooseSkin(MouseEvent evt) {
		int x = this.getX() + 240;
		int y = this.getY() + 435;
		if (opencloseCS == 1) {
			chooseSkin = new ChooseSkin(x, y, this);
			RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0, 0,
					chooseSkin.getWidth(), chooseSkin.getHeight(), 20, 20);
			WindowUtils.setWindowMask(chooseSkin, mask);
			chooseSkin.setVisible(true);
			opencloseCS = 0;

		} else if (opencloseCS == 0) {
			chooseSkin.setVisible(false);
			chooseSkin = null;
			opencloseCS = 1;
		}
	}

	// 信息窗口相关方法
	private void information(MouseEvent evt) {
		if (Information.getPersonalInfoHash().containsKey(Information.getUid())) {
			Information.getPersonalInfoHash().get(Information.getUid())
					.setVisible(true);
		} else {
			new PersonalInformation(
					Information.getUid());

		}

	}

	// 收索好友
	private void searchFriends(MouseEvent evt) {
		if (ocsearchandfind == 1) {
			searchandfind1 = new SearchAndFind();
			RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0, 0,
					searchandfind1.getWidth(), searchandfind1.getHeight(), 45,
					45);
			WindowUtils.setWindowMask(searchandfind1, mask);
			searchandfind1.setVisible(true);
			ocsearchandfind = 0;
		} else if (ocsearchandfind == 0) {
			searchandfind1.setVisible(false);
			searchandfind1 = null;
			ocsearchandfind = 1;
		}// 按钮事件

	}

	private void mini(MouseEvent evt) {
		// this.setExtendedState(JFrame.ICONIFIED);
		timer.stop();
		this.setVisible(false);// 按钮事件
	}

	private void close(MouseEvent evt) {
		scu.im.utils.Print.print("用户点击了关闭窗口");
		switch (JOptionPane.showConfirmDialog(null, null,
				"Do you really want to close the frame?",
				JOptionPane.YES_NO_OPTION)) {
		case JOptionPane.YES_OPTION:
			try {
				Socket clientSocket = IMSocket.getInstance();
				ObjectOutputStream outToServer = new ObjectOutputStream(
						clientSocket.getOutputStream());
				scu.im.utils.Print.print("关闭窗口输出流获取成功");
				// ObjectInputStream inFromServer = new ObjectInputStream(
				// clientSocket.getInputStream());
				Msg msg = new Msg();
				msg.setHead(Option.EXIT);
				msg.setBody(Information.getUid());
				scu.im.utils.Print.print("准备将窗口关闭事件发送给服务器！");

				outToServer.writeObject(msg);
				outToServer.flush();
				scu.im.utils.Print.print("窗口关闭事件发送完毕！");
				outToServer.close();
				// inFromServer.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					Information.getListener().stop();
					IMSocket.getInstance().close();
				} catch (IOException e) {
					e.printStackTrace();
				}// 关闭TCPsocket监听线程
				System.exit(0);
			}
			dispose();
			break;
		case JOptionPane.NO_OPTION:
			break;
		default:
			break;
		}
	}

	// 隐藏窗口方法
	private void moveHide(MouseEvent evt) {
		timer.start();
		// System.out.println("hide2");
	}

	private void gragHide(MouseEvent evt) {
		timer.start();
		// System.out.println("hide1");
	}

	// timer监听事件
	public void actionPerformed(ActionEvent e) {
		left = getLocationOnScreen().x;
		top = getLocationOnScreen().y;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		leftmax = screenSize.width;
		width = getWidth();
		height = getHeight();
		// 获取窗体的轮廓
		rect = new Rectangle(0, 0, width, height);
		// 获取鼠标在窗体的位置
		point = getMousePosition();
		if ((top < 0) && isPtInRect(rect, point)) {
			// 当鼠标在当前窗体内，并且窗体的Top属性小于0
			// 设置窗体的Top属性为0,就是将窗口上边沿紧靠顶部
			setLocation(left, 0);
		} else if (top > -5 && top < 5 && !(isPtInRect(rect, point))) {
			// 当窗体的上边框与屏幕的顶端的距离小于5时 ，
			// 并且鼠标不再窗体上 将QQ窗体隐藏到屏幕的顶端
			setLocation(left, 5 - height);
			timer.stop();
			// startListLabel.setVisible(false);
			openCloseStartList = 1;
		}
		if ((left > (leftmax - width)) && isPtInRect(rect, point)) {
			setLocation(leftmax - width, top);
		} else if ((left + width) > (leftmax - 5)
				&& (left + width) < (leftmax + 5) && !(isPtInRect(rect, point))) {
			setLocation(leftmax - 5, top);
			timer.stop();
		}
		if ((left < 0) && isPtInRect(rect, point)) {

			setLocation(0, top);
		} else if (left > -5 && left < 5 && !(isPtInRect(rect, point))) {
			setLocation(5 - width, top);
			timer.stop();
		}
	}

	public boolean isPtInRect(Rectangle rect, Point point) {
		if (rect != null && point != null) {
			int x0 = rect.x;
			int y0 = rect.y;
			int x1 = rect.width;
			int y1 = rect.height;
			int x = point.x;
			int y = point.y;

			return x >= x0 && x < x1 && y >= y0 && y < y1;
		}
		return false;
	}

	// 开始菜单按钮
	private void startlist(MouseEvent evt) {
		int x = this.getX() + 10;
		int y = this.getY() + 411;

		if (openCloseStartList == 1) {
			// startListLabel.setVisible(true);
			startList = new StartList(x, y);
			startList.setVisible(true);
			openCloseStartList = 0;
		} else if (openCloseStartList == 0) {
			// startListLabel.setVisible(false);
			startList.setVisible(false);
			startList = null;
			openCloseStartList = 1;

		}

	}

	// 系统托盘方法
	public void miniicon()

	{
		if (SystemTray.isSupported()) // 判断系统是否支持系统托盘
		{
			SystemTray tray = SystemTray.getSystemTray(); // 创建系统托盘
			Image image = Toolkit.getDefaultToolkit().getImage(
					".\\image\\trayImg\\logo.png");// 载入图片

			ActionListener listener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (messageUnread) {

						ArrayList<MessageWithAttrib> handleAl = MessageContainer
								.getMessageList();// 获得消息列表
						Hashtable<String, TalkToOneWithStyle> tempWindow = Information
								.getOpenedWindow();// 获得打开的聊天窗口的哈希表
						Hashtable<String, TalkToMany> tempGroupWindow = Information
								.getOpenedGroupWindow();

						for (MessageWithAttrib unitMessage : handleAl) {
							if (unitMessage.getGid() == null) {
								if (tempWindow.containsKey(unitMessage
										.getSenderUid())) {
									tempWindow
											.get(unitMessage.getSenderUid())
											.displayReceivedMessage(unitMessage);
								} else {
									tempTalk = new TalkToOneWithStyle(
											unitMessage.getSenderUid(),
											unitMessage.getSenderIp(),
											unitMessage.getSenderName());
									tempTalk
											.displayReceivedMessage(unitMessage);
								}
							} else {
								if (tempGroupWindow.containsKey(unitMessage
										.getGid())) {
									tempGroupWindow
											.get(unitMessage.getGid())
											.displayReceivedMessage(unitMessage);
								} else {
									TalkToMany tempGroupTalk = new TalkToMany(
											Information.getGroupList().get(
													unitMessage.getGid()));
									tempGroupTalk
											.displayReceivedMessage(unitMessage);
								}
							}
						}// 遍历

						handleAl.clear();// 清空list
						for (MsgShake ms : Information.getShakingUnit()
								.values()) {
							ms.stopShake();
						}
						MessageContainer.setMessageList(handleAl);
						messageUnread = false;
						stopBlink();
					} else {
						// open();
						// System.out.print("sdf");
						// tray.remove(trayIcon); // 从系统的托盘实例中移除托盘图标
						// setExtendedState(JFrame.NORMAL);
						windowActivated(null);
						setVisible(true); // 显示窗口
						timer.start();
						toFront();
					}
				}
			};

			// 创建托盘弹出菜单
			PopupMenu popup = new PopupMenu();
			MenuItem defaultItem = new MenuItem("open");
			defaultItem.addActionListener(listener);
			MenuItem exitItem = new MenuItem("exit");
			exitItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});

			popup.add(defaultItem);
			popup.add(exitItem);
			trayIcon = new TrayIcon(image, "My System Tray ", popup);// 创建trayIcon
			trayIcon.addActionListener(listener);
			try {
				tray.add(trayIcon);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void messageBlink() {
		if (messageUnread) {
			trayIcon.setImage(Toolkit.getDefaultToolkit().getImage(
					".\\image\\trayImg\\shake.gif"));
		}
	}

	public void stopBlink() {
		trayIcon.setImage(Toolkit.getDefaultToolkit().getImage(
				".\\image\\trayImg\\logo.png"));
	}

	public boolean isMessageUnread() {
		return messageUnread;
	}

	public void setMessageUnread(boolean messageUnread) {
		this.messageUnread = messageUnread;
	}

	// 改头像方法
	public void setImage(int num) {
		facelabel.setIcon(new ImageIcon(".\\image\\face\\" + num + ".jpg"));
	}

	public void open() {
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}

	public void inquireAddFriend(AddFriendType aft) {
		try {
			Msg confirmReturnMsg = new Msg();
			Msg confirmResultMsg = new Msg();
			ConfirmResultType toServerCrt = new ConfirmResultType();
			ConfirmReturnType crt = new ConfirmReturnType();
			confirmResultMsg.setHead(Option.CONFIRM_RESULT_TYPE);
			confirmResultMsg.setBody(toServerCrt);
			confirmReturnMsg.setHead(Option.CONFIRM_RETURN_TYPE);
			confirmReturnMsg.setBody(crt);

			crt.setRequestUid(aft.getRequestUid());
			crt.setWantedUid(Information.getUid());

			int n = JOptionPane.showConfirmDialog(null, "收到来自"
					+ aft.getRequestUid() + " 的添加好友请求，是否同意", "收到好友请求",
					JOptionPane.YES_NO_OPTION);

			if (n == 0) {
				crt.setAccecped(true);
				toServerCrt.setRequestUid(aft.getRequestUid());
				toServerCrt.setWantedUid(aft.getWantedUid());
				ObjectOutputStream outToServer = new ObjectOutputStream(
						IMSocket.getInstance().getOutputStream());
				outToServer.writeObject(confirmResultMsg);
				outToServer.flush();

			} else if (n == 1) {
				crt.setAccecped(false);
			}
			sendBuf = null;
			sender = new DatagramSocket();
			sendPacket = toDatagram(confirmReturnMsg, InetAddress.getByName(aft
					.getRequestIp()), Information.getUdpPort());
			sender.send(sendPacket);
			scu.im.utils.Print.print("UDP发送加好友回报成功");

			scu.im.utils.Print.print("ConfirmReturn发送完毕");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private DatagramPacket toDatagram(Serializable obj, InetAddress destIA,
			int destPort) {
		sendBuf = objectToByte(obj);
		return new DatagramPacket(sendBuf, sendBuf.length, destIA, destPort);
	}// 转数据包，将要发送的信息包装，返回一个数据包类型

	private byte[] objectToByte(Serializable obj) {
		byte[] tempByte = null;
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream(2048);
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			tempByte = bo.toByteArray();
			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return tempByte;
	}

	public void showAddFriendResult(ConfirmReturnType crt) {
		if (crt.isAccecped()) {
			JOptionPane.showMessageDialog(null, crt.getWantedUid()
					+ " 接受了你的添加好友请求", "Happy", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, crt.getRequestUid()
					+ " 拒绝了你的添加好友请求", "Sorry", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void updateGroupList() {
		Hashtable<String, GroupUnitType> groupList = Information.getGroupList();
		groupPanel.removeAll();
		for (GroupUnitType groupUnit : groupList.values()) {
			groupPanel.add(new GroupUnit(groupUnit));
		}

		groupPanel.validate();
	}

	public void groupApplyOperation(GroupApplyType gat) {
		int n = JOptionPane.showConfirmDialog(null, "收到来自"
				+ gat.getRequestUid() + "的申请加入群" + gat.getGid() + " 的请求，是否同意",
				"用户申请加入您创建的群", JOptionPane.YES_NO_CANCEL_OPTION);
		Msg groupResultMsg = new Msg();
		GroupResultType grt = new GroupResultType();
		groupResultMsg.setHead(Option.GROUP_RESULT_TYPE);
		groupResultMsg.setBody(grt);

		switch (n) {
		case 0:
			grt.setAccepted(true);
			try {
				ObjectOutputStream outToServer = new ObjectOutputStream(
						IMSocket.getInstance().getOutputStream());
				grt.setCreatorUid(Information.getUid());
				grt.setCreatorIp(Information.getIp());
				grt.setNewComerUid(gat.getRequestUid());
				grt.setGid(gat.getGid());
				outToServer.writeObject(groupResultMsg);
				outToServer.flush();
				scu.im.utils.Print.print("向服务器发送确认新人入群信息成功");
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		case 1:
			grt.setGid(gat.getGid());
			grt.setAccepted(false);
			break;
		}
		try {
			sender = new DatagramSocket();
			sendPacket = toDatagram(groupResultMsg, InetAddress.getByName(gat
					.getRequestIp()), Information.getUdpPort());
			sender.send(sendPacket);
			scu.im.utils.Print.print("向申请者回报信息成功");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void chooseSkinClose(int x) {
		opencloseCS = x;
	}

	public void showGroupApplyResult(GroupResultType grt) {
		if (grt.getAccepted()) {
			JOptionPane.showMessageDialog(null, "群主" + grt.getCreatorUid()
					+ " 同意了你加入群" + grt.getGid() + " 的请求", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (!grt.getAccepted()) {
			JOptionPane.showMessageDialog(null, "群主" + grt.getCreatorUid()
					+ " 拒绝了你加入群" + grt.getGid() + " 的请求", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void changeSkin(int x) {
		((JPanel) this.getContentPane()).setOpaque(false);
		ImageIcon img1 = new ImageIcon(".\\image\\skin\\" + x + ".png");
		if (backgroundlabel != null) {
			getLayeredPane().remove(backgroundlabel);
			getLayeredPane().validate();
		}
		backgroundlabel = new JLabel(img1);
		this.getLayeredPane().add(backgroundlabel,
				new Integer(Integer.MIN_VALUE));
		backgroundlabel.setBounds(0, 0, img1.getIconWidth(), img1
				.getIconHeight());

		// startListLabel.setIcon(new javax.swing.ImageIcon(
		// ".\\image\\startlist\\" + x + ".png"));
	}

	private MessageWithAttrib getMessageWithAttrib() {

		MessageWithAttrib tempAtt = new MessageWithAttrib();
		tempAtt.setGid(null);
		tempAtt.setSenderUid(Information.getUid());
		tempAtt.setSenderName(Information.getUserInfo().getNickname());
		tempAtt.setFontName("微软雅黑");
		tempAtt.setFontSize(20);
		tempAtt.setFontStyle(MessageWithAttrib.GENERAL);
		tempAtt.setFontColor(Color.BLACK);
		tempAtt.setSenderIp(Information.getIp());
		tempAtt.setSendTime(new Date());
		return tempAtt;

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void update() {
		scu.im.utils.Print.print("好友上线,更新好友信息！");
		Hashtable<String, FriendUnitType> friends = Information.getFriendList();

		friendsPanel.removeAll();
		for (FriendUnitType f : friends.values()) {
			scu.im.utils.Print.print("update()中" + f.getUid() + "的好友信息：");
			scu.im.utils.Print.print("update()中的好友信息：" + f.getNickname());
			scu.im.utils.Print.print("update()中的好友信息：" + f.getFakename());
			scu.im.utils.Print.print("update()中的好友信息：" + f.getState());
			scu.im.utils.Print.print("update()中的好友信息：" + f.getIp());
			scu.im.utils.Print.print("update()中的好友信息：" + f.getPort());
			scu.im.utils.Print.print("pdate()中的好友信息：" + f.getImage());
			friendsPanel.add(new FriendUnit(f));

			scu.im.utils.Print.print("更新群中friendunit");
			// GroupUnitType tempGut;
			// for (GroupUnitType gut : Information.getGroupList().values()) {
			// String gid = gut.getGid();
			// FriendUnitType tempFut;
			// for (FriendUnitType fut : gut.getMemberList()) {
			// if (fut.getUid().equals(f.getUid())) {
			// System.out.println("##########---------->>>："
			// + f.getIp());
			// System.out.println("##########---------->>>：更新前"
			// + fut.getUid() + " 的ip为" + fut.getIp());
			//						
			// System.out.println("##########---------->>>：更新后"
			// + fut.getUid() + " 的ip为" + fut.getIp());
			// fut = f;
			// }
			// }
			// System.out.println(Information.getGroupList().get("62301").
			// getMemberList().get(0).getIp());
			// System.out.println(Information.getGroupList().get("62301").
			// getMemberList().get(1).getIp());
			// System.out.println(Information.getGroupList().get("62301").
			// getMemberList().get(2).getIp());
			// }

			boolean bigFlag = false;
			GroupUnitType bigGut = null;

			for (Iterator<GroupUnitType> gutIt = Information.getGroupList()
					.values().iterator(); gutIt.hasNext();) {
				boolean flag = false;
				FriendUnitType fut = null;
				GroupUnitType gut = gutIt.next();
				for (Iterator<FriendUnitType> futIt = gut.getMemberList()
						.iterator(); futIt.hasNext();) {
					if (futIt.next().getUid().equals(f.getUid())) {
						futIt.remove();
						flag = true;
						fut = f;
					}
				}
				if (flag) {
					gut.getMemberList().add(fut);
					bigFlag = true;
					gutIt.remove();
					bigGut = gut;
				}
			}

			if (bigFlag) {
				Information.getGroupList().put(bigGut.getGid(), bigGut);
			}
		}
		friendsPanel.validate();
	}

}
