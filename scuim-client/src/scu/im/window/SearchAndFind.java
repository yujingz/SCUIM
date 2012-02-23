/*************************************************************************************************
 * @author : 彭则荣
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 实现对好友的查询或群的查询及添加为好友或申请入群的功能。
 *                查找方式可供选择，有精确查找<直接输入所查找账号>和条件查找。
 *                这是看似简单却深不可测的一段代码啊.
 * 
 * 
 *************************************************************************************************/

package scu.im.window;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import scu.im.bean.SearchResultUnit;
import scu.im.msgtype.AddFriendType;
import scu.im.msgtype.FriendUnitType;
import scu.im.msgtype.GroupApplyType;
import scu.im.msgtype.GroupUnitType;
import scu.im.msgtype.Msg;
import scu.im.msgtype.Option;
import scu.im.msgtype.SearchGroupReturnType;
import scu.im.msgtype.SearchGroupType;
import scu.im.msgtype.SearchUserReturnType;
import scu.im.msgtype.SearchUserType;
import scu.im.socket.IMSocket;
import scu.im.utils.Information;

public class SearchAndFind extends JFrame {

	private static final long serialVersionUID = 1L;
	// private ImageIcon choosedImage = new ImageIcon(".\\image\\按钮\\h20.png");
	// private JLabel choosedlabel = new JLabel(choosedImage);
	private JLabel backgroundlabel;
	private JLabel topLabel = new JLabel();
	private JButton minisizeButton = new JButton();
	private JButton closeButton = new JButton();
	//
	private JPanel findoutpanel = new JPanel();

	private JButton returnbutton = new JButton();
	private JButton addFriendButton = new JButton();
	private JLabel findlabel1 = new JLabel("以下是为您找到的结果 ：");

	JPanel searchPanel = new JPanel();
	JPanel searchFriendPanel = new JPanel();
	JPanel searchGroupPanel = new JPanel();
	private JButton searchFriendButton = new JButton();
	private JButton searchGroupButton = new JButton();
	private JButton searchButton = new JButton();
	private JButton cancelButton = new JButton();
	private JLabel friendnumLabel = new JLabel("账号搜索");
	private JTextField friendnumText = new JTextField();
	private JLabel onlineLabel = new JLabel("在线搜索");
	private JLabel groupNum = new JLabel("群号 ：");
	private JTextField groupnumText = new JTextField("请输入群号码");
	private JRadioButton onlineSearchRadio = new JRadioButton();
	private JRadioButton idSearchRadio = new JRadioButton();
	private ButtonGroup radioGroup = new ButtonGroup();

	private boolean isDraging;
	private int x, y;

	int choosedNum = 1;

	private JList friendList = null;

	// 鱼的变量
	private byte[] sendBuf = null;
	private DatagramPacket sendPacket = null;
	private DatagramSocket sender = null;
	private DefaultListModel dlm;
	private SearchResultUnit searchResultUnit;
	private Hashtable<String, SearchResultUnit> userResultHash;// 搜索用户哈希表
	private SearchResultUnit tempUserUnit;
	private int searchType = 1; // 1搜索用户，2搜索群
	private String groupCreatorIp;
	private String groupCreatorUid;
	private String groupId;

	public SearchAndFind() {
		initComponents();
		this.setSize(new Dimension(399, 301));// 窗体大小
		this.setResizable(false);// 固定窗体大小
		Information.setSearchPanel(this);
	}

	private void initComponents() {
		backGround();
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// setDefaultCloseOperation(3);
		this.setUndecorated(true); // 去掉标题栏
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage(".\\image\\trayImg\\logo.png");
		this.setIconImage(image);
		this.setTitle(" 查找好友");
		setLocation(350, 230);

		Container container1 = getContentPane(); // 获取JFrame面板
		JPanel jpanel = new JPanel(); // 创建个JPanel
		jpanel.setOpaque(false);// jpanel透明
		jpanel.setLayout(null);// jpanel的布局为空
		container1.add(jpanel);

		findoutpanel.setLayout(null);
		findoutpanel.setOpaque(false);// jpanel透明

		searchPanel.setLayout(null);
		searchPanel.setOpaque(false);// jpanel透明
		searchFriendPanel.setLayout(null);
		searchFriendPanel.setOpaque(false);// jpanel透明
		searchGroupPanel.setLayout(null);
		searchGroupPanel.setOpaque(false);// jpanel透明

		jpanel.add(topLabel);
		jpanel.add(minisizeButton);
		jpanel.add(closeButton);

		dlm = new DefaultListModel();
		friendList = new JList(dlm);
		JScrollPane scropane = new JScrollPane(friendList);
		friendList
				.setBorder(BorderFactory
						.createTitledBorder("账号               昵称                      城市 "));

		jpanel.add(findoutpanel);
		findoutpanel.add(scropane);
		findoutpanel.setVisible(false);
		findoutpanel.add(findlabel1);
		findoutpanel.add(addFriendButton);
		findoutpanel.add(returnbutton);

		jpanel.add(searchPanel);

		searchPanel.setBackground(Color.white);// 背景颜色
		searchPanel.setVisible(true);
		searchPanel.add(searchFriendPanel);
		searchPanel.add(searchGroupPanel);
		searchPanel.add(searchFriendButton);
		searchPanel.add(searchGroupButton);
		searchPanel.add(searchButton);
		searchPanel.add(cancelButton);
		searchPanel.setBounds(0, 23, 400, 277);
		searchFriendButton.setBounds(5, 3, 74, 22);
		searchGroupButton.setBounds(80, 3, 74, 22);
		searchButton.setBounds(230, 250, 74, 22);
		cancelButton.setBounds(315, 250, 74, 22);

		searchFriendPanel.setVisible(true);
		friendnumText.setEditable(false);
		searchFriendPanel.add(friendnumLabel);
		searchFriendPanel.add(friendnumText);
		searchFriendPanel.add(onlineSearchRadio);
		searchFriendPanel.add(idSearchRadio);
		searchFriendPanel.add(onlineLabel);
		radioGroup.add(onlineSearchRadio);
		radioGroup.add(idSearchRadio);

		searchFriendPanel.setBounds(3, 28, 394, 215);

		friendnumLabel.setBounds(80, 90, 80, 23);
		friendnumText.setBounds(80, 125, 230, 23);
		onlineLabel.setBounds(80, 60, 80, 23);
		onlineSearchRadio.setBounds(50, 61, 23, 23);
		idSearchRadio.setBounds(50, 91, 23, 23);

		searchGroupPanel.setVisible(false);
		searchGroupPanel.add(groupNum);
		searchGroupPanel.add(groupnumText);
		searchGroupPanel.setBounds(3, 28, 394, 215);

		groupNum.setBounds(50, 70, 80, 23);
		groupnumText.setBounds(50, 100, 250, 23);

		// 鱼相关
		userResultHash = new Hashtable<String, SearchResultUnit>();

		//

		searchFriendButton.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\sf2.png"));
		searchFriendButton.setContentAreaFilled(false); // 按钮透明
		searchFriendButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				searchFriendPanel.setVisible(true);
				searchGroupPanel.setVisible(false);
				searchType = 1;
				friendList
						.setBorder(BorderFactory
								.createTitledBorder("账号               昵称                      城市 "));
				searchFriendButton.setIcon(new ImageIcon(
						".\\image\\button\\sf2.png"));

				searchGroupButton.setIcon(new ImageIcon(
						".\\image\\button\\sg1.png"));
			}

		});

		searchGroupButton.setIcon(new ImageIcon(".\\image\\button\\sg1.png"));
		searchGroupButton.setContentAreaFilled(false); // 按钮透明
		searchGroupButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				searchFriendPanel.setVisible(false);
				searchGroupPanel.setVisible(true);
				searchType = 2;
				friendList.setBorder(BorderFactory
						.createTitledBorder("群号          群称  "));

				searchFriendButton.setIcon(new ImageIcon(
						".\\image\\button\\sf1.png"));

				searchGroupButton.setIcon(new ImageIcon(
						".\\image\\button\\sg2.png"));

			}

		});

		searchButton.setIcon(new ImageIcon(".\\image\\button\\s4.png"));
		searchButton.setContentAreaFilled(false); // 按钮透明
		searchButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				searchButton.setIcon(new ImageIcon(".\\image\\button\\s5.png"));
			}

			public void mouseExited(MouseEvent evt) {
				searchButton.setIcon(new ImageIcon(".\\image\\button\\s4.png"));
			}
		});

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					ObjectOutputStream outToServer = new ObjectOutputStream(
							IMSocket.getInstance().getOutputStream());
					Msg searchMsg = new Msg();
					switch (searchType) {
					case 0:
						System.out.println("##########---------->>>:请选择一种搜索");
						break;
					case 1:
						SearchUserType sut = new SearchUserType();
						sut.setSenderUid(Information.getUid());
						searchMsg.setBody(sut);
						if (onlineSearchRadio.isSelected()) {
							searchMsg.setHead(Option.SEARCH_USER_TYPE);
							sut.setSearchType(1);// 1为搜索在线用户
							outToServer.writeObject(searchMsg);
							outToServer.flush();
							System.out.println("online");
							searchPanel.setVisible(false);
							findoutpanel.setVisible(true);
						} else if (idSearchRadio.isSelected()) {
							searchMsg.setHead(Option.SEARCH_USER_TYPE);
							sut.setSearchType(0);// 0为搜索指定用户
							sut.setSearchUid(friendnumText.getText());
							outToServer.writeObject(searchMsg);
							outToServer.flush();
							searchPanel.setVisible(false);
							findoutpanel.setVisible(true);
						}
						break;
					case 2:
						SearchGroupType sgt = new SearchGroupType();
						searchMsg.setHead(Option.SEARCH_GROUP_TYPE);
						searchMsg.setBody(sgt);
						scu.im.utils.Print.print(groupnumText.getText());
						sgt.setRequestGid(groupnumText.getText());
						sgt.setRequestUid(Information.getUid());
						sgt.setRequestIp(Information.getIp());
						scu.im.utils.Print.print("搜索gid为"
								+ groupnumText.getText() + " 的群");
						outToServer.writeObject(searchMsg);
						outToServer.flush();
						searchPanel.setVisible(false);
						findoutpanel.setVisible(true);
						break;
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		returnbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				searchPanel.setVisible(true);
				findoutpanel.setVisible(false);
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dispose();
			}
		});

		cancelButton.setIcon(new ImageIcon(".\\image\\button\\cancel1.png"));
		cancelButton.setContentAreaFilled(false); // 按钮透明
		cancelButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				cancelButton.setIcon(new ImageIcon(
						".\\image\\button\\cancel2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				cancelButton.setIcon(new ImageIcon(
						".\\image\\button\\cancel1.png"));
			}
		});

		findoutpanel.setBounds(0, 23, 400, 277);
		// findoutpanel.setBorder(javax.swing.BorderFactory
		// .createLineBorder(new java.awt.Color(0, 0, 0)));// 边框
		findlabel1.setBounds(5, 5, 200, 25);
		scropane.setBounds(4, 31, 392, 215);
		scropane.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));// 边框
		addFriendButton.setBounds(315, 250, 74, 24);
		returnbutton.setBounds(230, 250, 74, 24);

		addFriendButton.setIcon(new ImageIcon(".\\image\\button\\add1.png"));
		addFriendButton.setContentAreaFilled(false); // 按钮透明
		addFriendButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				addFriendButton.setIcon(new ImageIcon(
						".\\image\\button\\add2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				addFriendButton.setIcon(new ImageIcon(
						".\\image\\button\\add1.png"));
			}
		});

		returnbutton.setIcon(new ImageIcon(".\\image\\button\\return1.png"));
		returnbutton.setContentAreaFilled(false); // 按钮透明
		returnbutton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				returnbutton.setIcon(new ImageIcon(
						".\\image\\button\\return2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				returnbutton.setIcon(new ImageIcon(
						".\\image\\button\\return1.png"));
			}
		});

		jpanel.setBounds(0, 0, 400, 300);
		// jpanel.setBorder(javax.swing.BorderFactory
		// .createLineBorder(new java.awt.Color(0, 0, 0)));// 边框
		topLabel.setBounds(0, 0, 327, 23);
		minisizeButton.setBounds(325, 0, 40, 23);
		minisizeButton.setContentAreaFilled(false); // 按钮透明
		minisizeButton.setBorderPainted(false);
		minisizeButton.setIcon(new ImageIcon(".\\image\\button\\zxh1.png"));
		closeButton.setBounds(355, 0, 40, 23);
		closeButton.setContentAreaFilled(false); // 按钮透明
		closeButton.setBorderPainted(false);
		closeButton.setIcon(new ImageIcon(".\\image\\button\\close1.png"));

		friendnumText.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {

			}

			@Override
			public void focusGained(FocusEvent e) {
				friendnumText.setText("");

			}
		});
		onlineSearchRadio.setContentAreaFilled(false);
		onlineSearchRadio.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				friendnumText.setEditable(false);
				friendnumText.setText("");

			}
		});
		idSearchRadio.setContentAreaFilled(false);
		idSearchRadio.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				friendnumText.setEditable(true);
				friendnumText.setText("请输入对方账号");
			}
		});

		groupnumText.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				groupnumText.setText("");
			}
		});

		// 移动监听
		topLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isDraging = true;
				x = e.getX();
				y = e.getY();
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
		topLabel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}

			public void mouseExited(MouseEvent evt) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		minisizeButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				mini(evt);
			}

			public void mouseEntered(MouseEvent evt) {
				minisizeButton.setIcon(new ImageIcon(
						".\\image\\button\\zxh2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				minisizeButton.setIcon(new ImageIcon(
						".\\image\\button\\zxh1.png"));
			}
		});

		closeButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				close(evt);
			}

			public void mouseEntered(MouseEvent evt) {
				closeButton.setIcon(new ImageIcon(
						".\\image\\button\\close3.png"));
			}

			public void mouseExited(MouseEvent evt) {
				closeButton.setIcon(new ImageIcon(
						".\\image\\button\\close1.png"));
			}
		});

		friendList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				switch (searchType) {
				case 1:
					tempUserUnit = userResultHash.get(String.valueOf(friendList
							.getSelectedIndex()));
					new PersonalInformation(tempUserUnit.getUid());
					break;
				case 2:
					new GroupInformation(groupId);
					break;
				}

			}

		});

		addFriendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Msg addMsg = new Msg();
					sender = new DatagramSocket();
					switch (searchType) {
					case 1:
						tempUserUnit = userResultHash.get(String
								.valueOf(friendList.getSelectedIndex()));
						boolean flag = true;
						for (FriendUnitType fut : Information.getFriendList()
								.values()) {
							if (fut.getUid().equals(tempUserUnit.getUid())) {
								JOptionPane.showMessageDialog(null,
										"该用户已经是您的好友，无需重复添加", "Notice",
										JOptionPane.INFORMATION_MESSAGE);
								flag = false;
								break;
							}
						}
						if (flag) {
							AddFriendType aft = new AddFriendType();
							addMsg.setHead(Option.ADD_FRIEND_TYPE);
							addMsg.setBody(aft);
							aft.setRequestUid(Information.getUid());
							aft.setWantedUid(tempUserUnit.getUid());
							aft.setRequestIp(Information.getIp());
							sendPacket = toDatagram(addMsg, InetAddress
									.getByName(tempUserUnit.getIp()),
									Information.getUdpPort());
							sender.send(sendPacket);
							break;
						}
						break;
					case 2:
						scu.im.utils.Print.print("向群主发送加群请求");
						boolean groupFlag = true;
						for (GroupUnitType gut : Information.getGroupList()
								.values()) {
							if (gut.getGid().equals(groupId)) {
								JOptionPane.showMessageDialog(null,
										"您已经在此群中，无需重复添加", "Notice",
										JOptionPane.INFORMATION_MESSAGE);
								groupFlag = false;
								break;
							}
						}
						if (groupFlag) {
							GroupApplyType gat = new GroupApplyType();
							addMsg.setHead(Option.GROUP_APPLY_TYPE);
							addMsg.setBody(gat);
							gat.setGid(groupId);
							gat.setCreatorUid(groupCreatorUid);
							gat.setRequestUid(Information.getUid());
							gat.setRequestIp(Information.getIp());
							scu.im.utils.Print.print("发送添加好友信息成功");
							sendPacket = toDatagram(addMsg, InetAddress
									.getByName(groupCreatorIp), Information
									.getUdpPort());
							sender.send(sendPacket);
							break;
						}
						break;
					default:
						break;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		setVisible(true);
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
			e.printStackTrace();
		}
		return tempByte;
	}

	private void mini(MouseEvent evt) {
		this.setExtendedState(JFrame.ICONIFIED);
	}

	private void close(MouseEvent evt) {

		dispose();
		Information.setSearchPanel(null);
	}

	public void showSearchResult(List<SearchUserReturnType> al) {
		int i = 0;
		if (!dlm.isEmpty()) {
			dlm.removeAllElements();
		}
		if (!al.isEmpty()) {
			for (SearchUserReturnType sur : al) {
				dlm.addElement(sur.getUid() + "             " + sur.getNickname()
						+ "                      " + sur.getCity());
				searchResultUnit = new SearchResultUnit();
				searchResultUnit.setUid(sur.getUid());
				searchResultUnit.setIp(sur.getIp());
				userResultHash.put(String.valueOf(i), searchResultUnit);
				i++;
			}

		} else {
			System.out.println("没有找到任何用户");
		}
		findoutpanel.setVisible(false);
		findoutpanel.setVisible(true);

	}

	public void showGroupSearchResult(SearchGroupReturnType sgrt) {
		if (!dlm.isEmpty()) {
			dlm.removeAllElements();
		}
		dlm.addElement(sgrt.getGid() + "        " + sgrt.getGname());
		groupCreatorIp = sgrt.getCreatorIp();
		groupCreatorUid = sgrt.getCreatorUid();
		groupId = sgrt.getGid();

	}

	public void backGround() {
		((JPanel) this.getContentPane()).setOpaque(false);
		ImageIcon img1 = new ImageIcon(".\\image\\skin\\sf"
				+ Information.getThemeNo() + ".png");
		if (backgroundlabel != null) {
			getLayeredPane().remove(backgroundlabel);
			getLayeredPane().validate();
		}
		backgroundlabel = new JLabel(img1);
		this.getLayeredPane().add(backgroundlabel,
				new Integer(Integer.MIN_VALUE));
		backgroundlabel.setBounds(0, 0, img1.getIconWidth(), img1
				.getIconHeight());
	}
}