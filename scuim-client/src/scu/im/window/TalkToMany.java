/*************************************************************************************************
 * @author : 彭则荣
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 单对单聊天窗口，有快捷键，可选字体大小颜色，有消息记录功能。
 *                挺帅气一个窗口，点消息记录时可变形。
 * 
 * 
 *************************************************************************************************/

package scu.im.window;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import scu.im.group.GroupCell;
import scu.im.group.GroupListItem;
import scu.im.msgtype.FriendUnitType;
import scu.im.msgtype.GroupUnitType;
import scu.im.msgtype.MessageWithAttrib;
import scu.im.msgtype.Msg;
import scu.im.msgtype.Option;
import scu.im.security.DESCoder;
import scu.im.utils.Information;

import com.sun.jna.examples.WindowUtils;

public class TalkToMany extends javax.swing.JFrame {

	private static final long serialVersionUID = 4480458084080349494L;

	public TalkToMany(GroupUnitType gut) {

		this.gut = gut;
		initMisc();
		initCompoments();
		initSocket();
		inputPane.requestFocusInWindow();
	}

	private void initMisc() {

		fontNameLibrary = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();

		fontSizeLibrary = new String[66];
		for (int i = 0; i < 66; i++) {
			fontSizeLibrary[i] = String.valueOf(i + 15);
		}

		fontStyleLibrary = new String[] { "常规", "斜体", "粗体", "粗斜体" };

		try {
			talkLogFw = new FileWriter(".\\ChatRecord\\GroupRecord\\"
					+ gut.getGid() + ".txt", true);
			talkLogBw = new BufferedWriter(talkLogFw);
		} catch (IOException e) {

			e.printStackTrace();
		}

		g_sdf = new SimpleDateFormat("HH:mm:ss");

		Hashtable<String, TalkToMany> openedGroupWindow = Information
				.getOpenedGroupWindow();
		openedGroupWindow.put(gut.getGid(), this);
		Information.setOpenedGroupWindow(openedGroupWindow);
	}

	private void initCompoments() {
		backGround();
	
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage(".\\image\\trayImg\\logo.png");
		this.setIconImage(image);
	
		thisFrame = this;

		RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0,
				0, 580, 480, 45, 45);
		WindowUtils.setWindowMask(thisFrame, mask);

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		dlm = new DefaultListModel();
		friendList = new JList(dlm);
		friendList.setCellRenderer(new GroupCell());
		JScrollPane scropane = new JScrollPane(friendList);
		for (FriendUnitType fut : gut.getMemberList()) {

			ImageIcon unitImage = new ImageIcon(".\\image\\face\\"
					+ fut.getImage() + ".jpg");
			unitImage.setImage(unitImage.getImage().getScaledInstance(20, 20,
					Image.SCALE_DEFAULT));
			GroupListItem unitItem = new GroupListItem(unitImage, fut
					.getNickname(), fut.getUid(), fut.getIp());
			dlm.addElement(unitItem);
		}

		final JPopupMenu pm = new JPopupMenu();
		JLabel popupLabel = new JLabel("查看该用户资料");
		pm.add(popupLabel);

		popupLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				GroupListItem tempItem = (GroupListItem) friendList
						.getSelectedValue();
				if (!tempItem.getUid().equals(Information.getUid())) {
					new PersonalInformation(tempItem.getUid());
				}
				pm.setVisible(false);
			}
		});

		friendList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if (e.getButton() == 3) {
					int index = friendList.locationToIndex(e.getPoint());
					friendList.setSelectedIndex(index);
					pm.show(friendList, e.getX(), e.getY());
				} else {
					if (!friendList.isSelectionEmpty()) {
						GroupListItem tempItem = (GroupListItem) friendList
								.getSelectedValue();
						if (!tempItem.getUid().equals(Information.getUid())) {
							new TalkToOneWithStyle(tempItem.getUid(), tempItem
									.getIp(), tempItem.getNickName());
						}
						pm.setVisible(false);
					}
				}
			}
		});

		scropane.setBounds(410, 180, 165, 290);
		scropane.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 111, 0)));

		minisizeButton = new JButton();
		closeButton = new JButton();
		topLabel = new JLabel();
		displayPane = new JTextPane();
		styledDoc = displayPane.getStyledDocument();
		inputPane = new JTextPane();
		displayPane.setEditable(false);
		displayScroll = new JScrollPane(displayPane);

		inputScroll = new JScrollPane(inputPane);
		sendButton = new JButton();
		closeButton1 = new JButton();
		fontNameBox = new JComboBox(fontNameLibrary);
		fontSizeBox = new JComboBox(fontSizeLibrary);
		fontStyleBox = new JComboBox(fontStyleLibrary);

		colorButton = new JButton();

		showFontPanelButton = new JButton();

		talklogButton = new JButton();
		talklogTextPane = new JTextPane();
		// Start
		noticeTextPane = new JTextPane();

		// groupFaceLabel = new JLabel();
		// groupName = new JLabel("无聊无耻无理取闹");
		// groupSign = new JLabel("飞机大炮抹脖上吊");
		groupAdLabel = new JLabel("|群公告|");
		setNoticeString(gut.getNotice());
		noticeTextPane.setText(noticeString);
		noticeTextPane.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 111, 0)));
		// groupFaceLabel.setBorder(javax.swing.BorderFactory
		// .createLineBorder(new java.awt.Color(0, 111, 0)));

		noticeTextPane.setBounds(410, 53, 165, 122);
		// groupFaceLabel.setBounds(5, 28, 55, 55);
		// groupName.setBounds(70, 28, 150, 23);
		// groupSign.setBounds(70, 60, 150, 23);
		groupAdLabel.setBounds(410, 28, 100, 25);
		talklogTextPane.setBounds(410, 53, 380, 418);
		talklogTextPane.setVisible(false);
		// Stop
		topLabel = new JLabel();
		fontPanel = new JPanel();
		fontPanel.setLayout(new FlowLayout());
		fontPanel.add(fontNameBox);
		fontPanel.add(fontStyleBox);
		fontPanel.add(fontSizeBox);
		fontPanel.add(colorButton);
		colorButton.setContentAreaFilled(false);
		fontPanel.setOpaque(true);

		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setOpaque(false);// jpanel透明
		getContentPane().add(mainPanel);
		mainPanel.add(fontPanel);
		mainPanel.add(sendButton);
		sendButton.setContentAreaFilled(false);
		mainPanel.add(closeButton1);
		closeButton1.setContentAreaFilled(false);
		mainPanel.add(displayScroll);
		mainPanel.add(inputScroll);
		mainPanel.add(showFontPanelButton);

		mainPanel.add(talklogButton);
		mainPanel.add(talklogTextPane);

		showFontPanelButton.setContentAreaFilled(false);

		// Start
		mainPanel.add(topLabel);
		mainPanel.add(minisizeButton);
		mainPanel.add(closeButton);
		topLabel.setBounds(0, 0, 515, 30);
		minisizeButton.setBounds(510, 4, 30, 16);
		minisizeButton.setContentAreaFilled(false); // 按钮透明
		minisizeButton.setBorderPainted(false);
		minisizeButton.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\zxh1.png"));
		closeButton.setBounds(540, 4, 30, 16);
		closeButton.setContentAreaFilled(false); // 按钮透明
		closeButton.setBorderPainted(false);
		closeButton.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\close1.png"));

		mainPanel.add(scropane);

		mainPanel.add(noticeTextPane);
		// mainPanel.add(groupFaceLabel);
		// mainPanel.add(groupName);
		// mainPanel.add(groupSign);
		mainPanel.add(groupAdLabel);
		// Stop
		// groupFaceLabel.setIcon(new javax.swing.ImageIcon(
		// ".\\image\\face\\1.jpg"));
		fontPanel.setVisible(false);
		showFontPanelButton.setBounds(5, 310, 74, 22);

		closeButton1.setBounds(250, 445, 74, 22);
		sendButton.setBounds(330, 445, 74, 22);
		displayScroll.setBounds(5, 53, 400, 252);
		inputScroll.setBounds(5, 335, 400, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 105, 580, 480);
		this.setUndecorated(true); // 去掉标题栏

		talklogButton.setBounds(327, 305, 80, 30);
		talklogButton.setContentAreaFilled(false); // 按钮透明
		talklogButton.setBorderPainted(false);
		talklogButton.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\talklog1.png"));
		talklogButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				talklogButton.setIcon(new ImageIcon(
						".\\image\\button\\talklog2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				talklogButton.setIcon(new ImageIcon(
						".\\image\\button\\talklog1.png"));
			}
		});

		talklogButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				File f = new File(".\\ChatRecord\\GroupRecord\\" + gut.getGid()
						+ ".txt");

				InputStreamReader read = null;
				try {
					read = new InputStreamReader(new FileInputStream(f),
							"utf-8");
				} catch (UnsupportedEncodingException ex) {
					ex.printStackTrace();
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				}
				SimpleAttributeSet blackAttrSet = new SimpleAttributeSet();
				StyleConstants.setForeground(blackAttrSet, Color.BLACK);
				talklogTextPane.setText("");
				Document doc = talklogTextPane.getDocument();
				try {
					BufferedReader reader = new BufferedReader(read);
					String line = reader.readLine();
					while (line != null) {
						line = new String(DESCoder.decrypt(DESCoder
								.decryptBASE64(line), Information.getDesKey()));
						if (line.startsWith("∵")) {
							line = line.substring(1);
						} else if (line.startsWith("∴")) {
							line = line.substring(1);
							doc.insertString(doc.getLength(), "  " + line
									+ "\n", blackAttrSet);
							displayPane.setSelectionStart(displayPane.getText()
									.length());
						}
						doc.insertString(doc.getLength(), line + "\n",
								blackAttrSet);
						talklogTextPane.setSelectionStart(talklogTextPane
								.getText().length());
						line = reader.readLine();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (isChatRecordVisible) {
					isChatRecordVisible = false;
					file = new String("talkToMany1");
					backGround();
					minisizeButton.setLocation(510, 4);
					closeButton.setLocation(540, 4);
					setSize(580, 480);
					RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0,
							0, 580, 480, 45, 45);
					WindowUtils.setWindowMask(thisFrame, mask);
					talklogTextPane.setVisible(false);
					groupAdLabel.setVisible(true);
					repaint();
				} else {
					isChatRecordVisible = true;
					setSize(800, 480);
					file = new String("talkToMany2");
					backGround();
					minisizeButton.setLocation(730, 4);
					closeButton.setLocation(760, 4);
					RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0,
							0, 800, 480, 45, 45);
					WindowUtils.setWindowMask(thisFrame, mask);
					talklogTextPane.setVisible(true);
					groupAdLabel.setVisible(false);
					repaint();
				}
			}
		});
		this.setTitle(" " + gut.getGName());
		topLabel.setText("   群id " + gut.getGid() + "(" + gut.getGName() + ")"
				+ ": " + gut.getGName());
		topLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isDraging = true;
				g_i = e.getX();
				g_j = e.getY();
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
					setLocation(left + e.getX() - g_i, top + e.getY() - g_j);
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

			public void mouseEntered(MouseEvent evt) {
				closeButton.setIcon(new ImageIcon(
						".\\image\\button\\close3.png"));
			}

			public void mouseExited(MouseEvent evt) {
				closeButton.setIcon(new ImageIcon(
						".\\image\\button\\close1.png"));
			}
		});

		closeButton1.setIcon(new ImageIcon(".\\image\\button\\close4.png"));
		closeButton1.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				closeButton1.setIcon(new ImageIcon(
						".\\image\\button\\close5.png"));
			}

			public void mouseExited(MouseEvent evt) {
				closeButton1.setIcon(new ImageIcon(
						".\\image\\button\\close4.png"));
			}
		});

		sendButton.setIcon(new ImageIcon(".\\image\\button\\send1.png"));
		sendButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				sendButton
						.setIcon(new ImageIcon(".\\image\\button\\send2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				sendButton
						.setIcon(new ImageIcon(".\\image\\button\\send1.png"));
			}
		});

		showFontPanelButton.setIcon(new ImageIcon(".\\image\\button\\A1.png"));
		showFontPanelButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				showFontPanelButton.setIcon(new ImageIcon(
						".\\image\\button\\A2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				showFontPanelButton.setIcon(new ImageIcon(
						".\\image\\button\\A1.png"));
			}
		});

		showFontPanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fontPanel.isVisible()) {
					fontPanel.setVisible(false);
				} else {
					fontPanel.setBounds(showFontPanelButton.getX(),
							showFontPanelButton.getY() - 30, 350, 30);
					fontPanel.setVisible(true);
				}
			}
		});

		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tempColor = JColorChooser.showDialog(null, "选择字体颜色",
						Color.BLACK);
				inputPane.requestFocusInWindow();
			}
		});

		inputPane.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == 10) {
					sendCheck();
				}
			}
		});// 快捷键，Ctrl+Enter

		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendCheck();
			}
		});

		inputPane.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				inputPane.setCharacterAttributes(getMessageWithAttrib()
						.getAttrSet(), true);
			}

			@Override
			public void focusLost(FocusEvent e) {
				inputPane.setCharacterAttributes(getMessageWithAttrib()
						.getAttrSet(), true);
			}
		});

		displayPane.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		inputPane.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		// 关闭窗口
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {

				Hashtable<String, TalkToMany> tempHash = Information
						.getOpenedGroupWindow();
				tempHash.remove(gut.getGid());
				TalkToMany.this.setVisible(false);
			}

			public void windowClosed(WindowEvent e) {
				System.out.println("成功关闭群" + gut.getGid() + "聊天的窗口");
			}
		});

		closeButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Hashtable<String, TalkToMany> tempHash = Information
						.getOpenedGroupWindow();
				tempHash.remove(gut.getGid());
				TalkToMany.this.setVisible(false);
			}
		});
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Hashtable<String, TalkToMany> tempHash = Information
						.getOpenedGroupWindow();
				tempHash.remove(gut.getGid());
				TalkToMany.this.setVisible(false);
			}
		});

		// 关闭窗口
		setVisible(true);
	}// 初始化图形界面

	private boolean sendMsg() {

		if (fontPanel.isVisible()) {
			fontPanel.setVisible(false);
		}
		try {
			g_fa = getMessageWithAttrib();
			for (FriendUnitType fut : gut.getMemberList()) {

				if (!fut.getUid().equals(Information.getUid())) {

					if (fut.getIp() != null) {

						Msg sendMsg = new Msg();
						sendMsg.setHead(Option.MESSAGE_WITH_ATTRIB);
						sendMsg.setBody(g_fa);
						System.out.println("##########---------->>>： 发送给"
								+ fut.getUid() + " 其IP为" + fut.getIp());
						sendPacket = toDatagram(sendMsg, InetAddress
								.getByName(fut.getIp()), Information
								.getUdpPort());
						datagramClient.send(sendPacket);
						inputPane.requestFocusInWindow();
					} else {
						System.out.println("##########---------->>>： "
								+ fut.getUid() + " 不在线，不发送");

					}
				}

			}
			g_msg = ("我说  " + new Date() + " \n" + g_fa.getText() + "\n");
			talkLogBw.write(new String(DESCoder.encryptBASE64(DESCoder.encrypt(
					("∵" + g_msg).getBytes(), Information.getDesKey()))));
			talkLogBw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

		return true;
	}// 发送信息，返回一个布尔值，来验证是否发送成功

	private void insertString(MessageWithAttrib attr) {
		try {
			g_time = g_sdf.format(new Date());
			styledDoc.insertString(styledDoc.getLength(), "我说: " + g_time
					+ "\n", getDefaultAttr());
			styledDoc.insertString(styledDoc.getLength(),
					attr.getText() + "\n", attr.getAttrSet());
			displayPane.setSelectionStart(displayPane.getText().length());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void displayReceivedMessage(MessageWithAttrib attr) {
		try {
			g_time = g_sdf.format(new Date());

			styledDoc.insertString(styledDoc.getLength(), attr.getSenderUid()
					+ " 说: " + g_time + "\n", getDefaultAttr());
			styledDoc.insertString(styledDoc.getLength(),
					attr.getText() + "\n", attr.getAttrSet());
			displayPane.setSelectionStart(displayPane.getText().length());

			g_msg = (attr.getSenderUid() + " 说  " + g_time + " \n"
					+ attr.getText() + "\n");
			talkLogBw.write(new String(DESCoder.encryptBASE64(DESCoder.encrypt(
					("∴" + g_msg).getBytes(), Information.getDesKey()))));
			talkLogBw.flush();

		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendCheck() {
		if (inputPane.getText() == null) {
			System.out.println("输入框为空");
		} else {
			if (sendMsg()) {
				insertString(g_fa);

				inputPane.setText("");// 发送成功则将发送文本清空
			} else {
				System.out.println("Error, can't send messages!");
			}
		}

	}// 发动的真实动作，先接受IP，然后调用sendMsg发送消息

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

	private void initSocket() throws HeadlessException {
		try {
			datagramClient = new DatagramSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getNoticeString() {
		return noticeString;
	}

	public void setNoticeString(String noticeString) {
		this.noticeString = noticeString;
	}

	public void backGround() {
		((JPanel) this.getContentPane()).setOpaque(false);
		ImageIcon img1 = new ImageIcon(".\\image\\" + file + "\\ttm"
				+ Information.getThemeNo() + ".png");
		if (backgroundLabel != null) {
			getLayeredPane().remove(backgroundLabel);
			getLayeredPane().validate();
		}
		backgroundLabel = new JLabel(img1);
		this.getLayeredPane().add(backgroundLabel,
				new Integer(Integer.MIN_VALUE));
		backgroundLabel.setBounds(0, 0, img1.getIconWidth(), img1
				.getIconHeight());

	}

	private void mini(MouseEvent evt) {

		this.setExtendedState(JFrame.ICONIFIED);

	}

	private MessageWithAttrib getMessageWithAttrib() {

		MessageWithAttrib tempAtt = new MessageWithAttrib();
		tempAtt.setGid(gut.getGid());
		tempAtt.setSenderUid(Information.getUid());
		tempAtt.setReceiverUid(uid);

		tempAtt.setText(inputPane.getText());

		tempAtt.setFontName((String) fontNameBox.getSelectedItem());

		tempAtt.setFontSize(Integer.parseInt((String) fontSizeBox
				.getSelectedItem()));

		String tempStyle = (String) fontStyleBox.getSelectedItem();
		if (tempStyle.equals("常规")) {
			tempAtt.setFontStyle(MessageWithAttrib.GENERAL);
		} else if (tempStyle.equals("粗体")) {
			tempAtt.setFontStyle(MessageWithAttrib.BOLD);
		} else if (tempStyle.equals("斜体")) {
			tempAtt.setFontStyle(MessageWithAttrib.ITALIC);
		} else if (tempStyle.equals("粗斜体")) {
			tempAtt.setFontStyle(MessageWithAttrib.BOLD_ITALIC);
		}

		tempAtt.setFontColor(tempColor);
		tempAtt.setSenderIp(Information.getIp());
		tempAtt.setSendTime(new Date());
		return tempAtt;
	}// 得到发送的内容的属性集

	private SimpleAttributeSet getDefaultAttr() {

		defaultAttr = new SimpleAttributeSet();
		StyleConstants.setBold(defaultAttr, false);
		StyleConstants.setItalic(defaultAttr, false);
		StyleConstants.setFontSize(defaultAttr, 20);
		StyleConstants.setForeground(defaultAttr, Color.BLACK);
		return defaultAttr;
	}// 获得默认的系统字体，供程序提示用户使用

	// 私有变量的声明
	private JScrollPane displayScroll, inputScroll;
	private JTextPane displayPane;
	private JTextPane inputPane;
	private JButton sendButton;
	private JButton closeButton1;
	private JPanel mainPanel;

	// 图形化界面变量的初始化

	private byte[] sendBuf = null;
	private DatagramSocket datagramClient = null;
	private DatagramPacket sendPacket = null;

	private BufferedWriter talkLogBw = null;
	private FileWriter talkLogFw = null;
	private String uid = null;
	// 通信单元变量的初始化

	private JComboBox fontNameBox, fontSizeBox, fontStyleBox = null;
	private StyledDocument styledDoc = null;
	private Color tempColor = null;
	private String[] fontNameLibrary, fontSizeLibrary, fontStyleLibrary = null;
	private JButton colorButton, showFontPanelButton = null;
	private JPanel fontPanel;
	private MessageWithAttrib g_fa = null;
	private SimpleAttributeSet defaultAttr = null;

	// 字体控制初始化
	private SimpleDateFormat g_sdf = null;
	private String g_time = null;
	private String g_msg = null;
	private GroupUnitType gut = null;

	private JTextPane noticeTextPane;
	private String noticeString = "群主已死";
	//
	// private JLabel groupFaceLabel;
	// private JLabel groupName;
	// private JLabel groupSign;
	private JFrame thisFrame;

	private JLabel groupAdLabel;
	private JList friendList = null;
	private DefaultListModel dlm;
	private JLabel backgroundLabel;
	private JLabel topLabel;
	private JButton minisizeButton;
	private JButton closeButton;
	private boolean isDraging;
	private int g_i, g_j;
	private String file = "talkToMany1";
	private JButton talklogButton;
	private JTextPane talklogTextPane;
	private boolean isChatRecordVisible = false;
}
