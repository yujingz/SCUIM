/*************************************************************************************************
 * @author : 彭则荣
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 群聊天窗口，有快捷键，可选字体大小颜色，有消息记录功能。带群成员列表及群公告。
 *                挺帅气一个窗口，点消息记录时可变形。
 * 
 * 
 *************************************************************************************************/

package scu.im.window;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
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
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import scu.im.msgtype.FriendUnitType;
import scu.im.msgtype.MessageWithAttrib;
import scu.im.msgtype.Msg;
import scu.im.msgtype.Option;
import scu.im.msgtype.TCPFileRequestType;
import scu.im.msgtype.TCPFileResponseType;
import scu.im.msgtype.VoicChatRequestType;
import scu.im.msgtype.VoiceChatResponseType;
import scu.im.security.DESCoder;
import scu.im.transfile.TCPFileReceiver;
import scu.im.transfile.TCPFileSender;
import scu.im.utils.Information;
import scu.im.voice.Capture;
import scu.im.voice.CaptureSocket;
import scu.im.voice.PlayBackSocket;

import com.sun.jmx.remote.internal.ClientCommunicatorAdmin;
import com.sun.jna.examples.WindowUtils;

public class TalkToOneWithStyle extends JFrame {

	private static final long serialVersionUID = 1L;

	public TalkToOneWithStyle(FriendUnitType fut) {
		// 需要uid，ip，port
		friendinfo = fut;
		userName = fut.getNickname();
		this.g_uid = friendinfo.getUid();
		initMisc();
		initCompoments();

		initSocket(friendinfo.getIp(),
				String.valueOf(Information.getUdpPort()), friendinfo.getUid());
		inputPane.requestFocusInWindow();
	}

	public TalkToOneWithStyle(TCPFileRequestType frt) {

		fileRequestInfo = frt;
		userName = frt.getSenderName();
		this.g_uid = fileRequestInfo.getSenderUid();
		initMisc();
		initCompoments();
		initSocket(fileRequestInfo.getSenderIp(), String.valueOf(Information
				.getUdpPort()), fileRequestInfo.getSenderUid());
		inputPane.requestFocusInWindow();
	}

	public TalkToOneWithStyle(MessageWithAttrib mwa) {

		messageInfo = mwa;
		this.g_uid = messageInfo.getSenderUid();
		userName = mwa.getSenderName();
		initMisc();
		initCompoments();
		initSocket(messageInfo.getSenderIp(), String.valueOf(Information
				.getUdpPort()), messageInfo.getSenderUid());
		inputPane.requestFocusInWindow();
	}

	public TalkToOneWithStyle(TCPFileResponseType receivedResponse) {
		responseInfo = receivedResponse;
		this.g_uid = responseInfo.getSenderUid();
		userName = receivedResponse.getSenderName();
		initMisc();
		initCompoments();
		initSocket(responseInfo.getSenderIp(), String.valueOf(Information
				.getUdpPort()), responseInfo.getSenderUid());
		inputPane.requestFocusInWindow();
	}

	public TalkToOneWithStyle(String uid, String ip, String userName) {
		this.g_uid = uid;
		this.userName = userName;
		initMisc();
		initCompoments();
		initSocket(ip, String.valueOf(Information.getUdpPort()), uid);
		inputPane.requestFocusInWindow();
	}

	private void initMisc() {

		fontNameLibrary = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();

		fontSizeLibrary = new String[40];
		for (int i = 0; i < 40; i++) {
			fontSizeLibrary[i] = String.valueOf(i + 25);
		}

		fontStyleLibrary = new String[] { "常规", "斜体", "粗体", "粗斜体" };

		try {
			talkLogFw = new FileWriter(".\\ChatRecord\\" + g_uid + ".txt", true);
			talkLogBw = new BufferedWriter(talkLogFw);
		} catch (IOException e) {

			e.printStackTrace();
		}

		g_sdf = new SimpleDateFormat("HH:mm:ss");

		tempHash = Information.getOpenedWindow();
		tempHash.put(g_uid, TalkToOneWithStyle.this);
		Information.setOpenedWindow(tempHash);

		if (Information.getFriendList().get(g_uid) == null) {
			isOnline = false;
		} else {
			isOnline = true;
		}

	}

	private void initCompoments() {

		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage(".\\image\\trayImg\\logo.png");
		this.setIconImage(image);

		backGround();
		thisFrame = this;
		RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0, 0, 420,
				480, 45, 45);
		WindowUtils.setWindowMask(thisFrame, mask);
		this.setUndecorated(true); // 去掉标题栏

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		minisizeButton = new JButton();
		closeButton = new JButton();
		topLabel = new JLabel();
		displayPane = new JTextPane();
		styledDoc = displayPane.getStyledDocument();
		inputPane = new JTextPane();
		displayPane.setEditable(false);
		displayScroll = new JScrollPane(displayPane);
		displayScroll.getVerticalScrollBar();

		talklogTextPane = new JTextPane();
		talklogTextPane.setEditable(false);
		chatLogScroll = new JScrollPane(talklogTextPane);
		chatLogScroll.getVerticalScrollBar();

		inputScroll = new JScrollPane(inputPane);
		sendButton = new JButton();
		closeButton1 = new JButton();
		fontNameBox = new JComboBox(fontNameLibrary);
		fontSizeBox = new JComboBox(fontSizeLibrary);
		fontStyleBox = new JComboBox(fontStyleLibrary);

		colorButton = new JButton("颜色");
		/* testButton = new JButton("test"); */

		showFontPanelButton = new JButton();
		fileTransButton = new JButton();
		talklogButton = new JButton();
		voiceTalk = new JButton();

		fontPanel = new JPanel();
		fontPanel.setLayout(new FlowLayout());
		fontPanel.add(fontNameBox);
		fontPanel.add(fontStyleBox);
		fontPanel.add(fontSizeBox);
		fontPanel.add(colorButton);
		fontPanel.setOpaque(true);

		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setOpaque(false);// jpanel透明
		getContentPane().add(mainPanel);
		mainPanel.add(fontPanel);
		mainPanel.add(sendButton);
		/* mainPanel.add(testButton); */
		mainPanel.add(closeButton1);
		mainPanel.add(displayScroll);

		mainPanel.add(inputScroll);
		mainPanel.add(showFontPanelButton);
		mainPanel.add(fileTransButton);

		mainPanel.add(talklogButton);
		mainPanel.add(voiceTalk);
		// mainPanel.add(talklogTextPane);
		mainPanel.add(chatLogScroll);

		mainPanel.add(topLabel);
		mainPanel.add(minisizeButton);
		mainPanel.add(closeButton);
		topLabel.setBounds(0, 0, 350, 30);

		minisizeButton.setBounds(350, 4, 30, 16);
		minisizeButton.setContentAreaFilled(false); // 按钮透明
		minisizeButton.setBorderPainted(false);
		minisizeButton.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\zxh1.png"));
		closeButton.setBounds(380, 4, 30, 16);
		closeButton.setContentAreaFilled(false); // 按钮透明
		closeButton.setBorderPainted(false);
		closeButton.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\close1.png"));
		// mainPanel.setBounds(0, 0, 550, 480);
		fontPanel.setVisible(false);
		showFontPanelButton.setBounds(10, 305, 60, 30);
		showFontPanelButton.setContentAreaFilled(false); // 按钮透明
		showFontPanelButton.setBorderPainted(false);
		fileTransButton.setBounds(75, 305, 60, 30);
		/* testButton.setBounds(140, 335, 60, 30); */
		fileTransButton.setContentAreaFilled(false); // 按钮透明
		fileTransButton.setBorderPainted(false);
		closeButton1.setBounds(280, 445, 60, 25);
		closeButton1.setContentAreaFilled(false); // 按钮透明
		closeButton1.setBorderPainted(false);
		sendButton.setBounds(350, 445, 60, 25);
		sendButton.setContentAreaFilled(false); // 按钮透明
		sendButton.setBorderPainted(false);
		displayScroll.setBounds(10, 50, 400, 250);
		inputScroll.setBounds(10, 340, 400, 100);

		chatLogScroll.setBounds(420, 50, 380, 420);
		// chatLogScroll.setBorder(javax.swing.BorderFactory
		// .createLineBorder(new java.awt.Color(0, 0, 0)));

		voiceTalk.setBounds(245, 305, 80, 30);
		voiceTalk.setContentAreaFilled(false); // 按钮透明
		voiceTalk.setBorderPainted(false);
		voiceTalk.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\voice1.png"));
		voiceTalk.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				voiceTalk.setIcon(new javax.swing.ImageIcon(
						".\\image\\button\\voice2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				voiceTalk
						.setIcon(new ImageIcon(".\\image\\button\\voice1.png"));
			}
		});
		talklogButton.setBounds(330, 305, 80, 30);
		talklogButton.setContentAreaFilled(false); // 按钮透明
		talklogButton.setBorderPainted(false);
		talklogButton.setIcon(new ImageIcon(".\\image\\button\\talklog1.png"));
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
				File f = new File(".\\ChatRecord\\" + g_uid + ".txt");
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
//							doc.insertString(doc.getLength(), "  " + line
//									+ "\n", blackAttrSet);
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
					file = new String("talkToOne1");
					backGround();
					minisizeButton.setLocation(350, 4);
					closeButton.setLocation(380, 4);
					setSize(420, 480);
					RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0,
							0, 420, 480, 45, 45);
					WindowUtils.setWindowMask(thisFrame, mask);
					repaint();
				} else {
					isChatRecordVisible = true;
					setSize(810, 480);
					file = new String("talkToOne2");
					backGround();
					minisizeButton.setLocation(740, 4);
					closeButton.setLocation(770, 4);
					RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0,
							0, 810, 480, 45, 45);
					WindowUtils.setWindowMask(thisFrame, mask);
					repaint();
				}
			}
		});

		closeButton1.setIcon(new ImageIcon(".\\image\\button\\close4.png"));

		fontNameBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tempStr = inputPane.getText();
				inputPane.setText(null);
				MessageWithAttrib ma = getMessageWithAttrib();
				StyledDocument tempsd = inputPane.getStyledDocument();
				try {
					tempsd.insertString(0, tempStr, ma.getAttrSet());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				inputPane.requestFocusInWindow();
			}
		});

		fontSizeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tempStr = inputPane.getText();
				inputPane.setText(null);
				MessageWithAttrib ma = getMessageWithAttrib();
				StyledDocument tempsd = inputPane.getStyledDocument();
				try {
					tempsd.insertString(0, tempStr, ma.getAttrSet());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				inputPane.requestFocusInWindow();
			}
		});

		fontStyleBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tempStr = inputPane.getText();
				inputPane.setText(null);
				MessageWithAttrib ma = getMessageWithAttrib();
				StyledDocument tempsd = inputPane.getStyledDocument();
				try {
					tempsd.insertString(0, tempStr, ma.getAttrSet());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				inputPane.requestFocusInWindow();
			}
		});

		/*
		 * testButton.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { String tempStr =
		 * inputPane.getText(); inputPane.setText(null); MessageWithAttrib ma =
		 * new MessageWithAttrib(); ma.setFontSize(50); StyledDocument tempsd =
		 * inputPane.getStyledDocument(); try { tempsd.insertString(0, tempStr,
		 * ma.getAttrSet()); } catch (BadLocationException e1) {
		 * e1.printStackTrace(); } } });
		 */

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
			public void mouseEntered(MouseEvent evt) {
				showFontPanelButton.setIcon(new ImageIcon(
						".\\image\\button\\A2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				showFontPanelButton.setIcon(new ImageIcon(
						".\\image\\button\\A1.png"));
			}
		});

		fileTransButton.setIcon(new ImageIcon(".\\image\\button\\file1.png"));
		fileTransButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				fileTransButton.setIcon(new ImageIcon(
						".\\image\\button\\file2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				fileTransButton.setIcon(new ImageIcon(
						".\\image\\button\\file1.png"));
			}
		});
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 80, 420, 480);
		this.setTitle(" 与 " + userName + " 聊天中");
		topLabel.setText("   与 " + userName + " 聊天中");
		topLabel.setFont(new Font("   与 " + userName + " 聊天中", Font.BOLD, 13));
		topLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isDraging = true;
				i = e.getX();
				j = e.getY();
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
					setLocation(left + e.getX() - i, top + e.getY() - j);
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
		minisizeButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
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

		showFontPanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fontPanel.isVisible()) {
					fontPanel.setVisible(false);
				} else {
					fontPanel.setBounds(showFontPanelButton.getX(),
							showFontPanelButton.getY() - 30, 400, 30);
					fontPanel.setVisible(true);
				}
			}
		});

		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tempColor = JColorChooser.showDialog(null, "选择字体颜色",
						Color.BLACK);
				String tempStr = inputPane.getText();
				inputPane.setText(null);
				MessageWithAttrib ma = getMessageWithAttrib();
				StyledDocument tempsd = inputPane.getStyledDocument();
				try {
					tempsd.insertString(0, tempStr, ma.getAttrSet());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
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

		voiceTalk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Msg voiceChatReqestMsg = new Msg();
					VoicChatRequestType vcrt = new VoicChatRequestType();
					voiceChatReqestMsg.setHead(Option.VOICE_CHAT_REQUEST);
					voiceChatReqestMsg.setBody(vcrt);
					vcrt.setRequestIp(Information.getIp());
					vcrt
							.setRequestName(Information.getUserInfo()
									.getNickname());
					vcrt.setRequestUid(Information.getUid());
					datagramClient = new DatagramSocket();
					sendPacket = toDatagram(voiceChatReqestMsg, inetAddr,
							Information.getUdpPort());
					datagramClient.send(sendPacket);
					PlayBackSocket pbs = new PlayBackSocket(62301);
					pbs.start();

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		fileTransButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fileChooser = new JFileChooser("C://");
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int retVal = fileChooser.showOpenDialog(null);
					if (retVal == JFileChooser.APPROVE_OPTION) {
						g_file = fileChooser.getSelectedFile();
						// 初始化一个file对象，对话框选择文件后初始化
						TCPFileRequestType request = new TCPFileRequestType();

						request.setFilename(g_file.getName());
						request.setReceiverUid(g_uid);
						request.setReceiverIp(inetAddr.getHostAddress());
						request.setSenderIp(Information.getIp());
						request.setSenderUid(Information.getUid());
						request.setSenderName(Information.getUserInfo()
								.getNickname());
						// 初始化TCPFileRequestType对象,文件名，双方的uid，ip
						Msg fileRequestMsg = new Msg();

						fileRequestMsg.setHead(Option.TCP_FILE_REQUEST_TYPE);
						fileRequestMsg.setBody(request);
						// 包装成MSG类型

						sendPacket = toDatagram(fileRequestMsg, inetAddr,
								Information.getUdpPort());
						datagramClient.send(sendPacket);
						/*
						 * 成包，发送，使用UDP端口9528. 下一步在ListenUDPThread中接收
						 */
					} else {
						g_time = g_sdf.format(new Date());
						styledDoc.insertString(styledDoc.getLength(), "  "
								+ g_time + "  您已经取消了文件传输  " + "\n",
								getDefaultAttr());
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});// 发送文件第一步，发出传送文件请求

		displayPane.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		inputPane.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		// 关闭窗口
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {

				Hashtable<String, TalkToOneWithStyle> tempHash = Information
						.getOpenedWindow();
				tempHash.remove(g_uid);
				TalkToOneWithStyle.this.setVisible(false);
			}

			public void windowClosed(WindowEvent e) {
				scu.im.utils.Print.print("成功关闭与" + g_uid + "聊天的窗口");
			}
		});

		closeButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Hashtable<String, TalkToOneWithStyle> tempHash = Information
						.getOpenedWindow();
				tempHash.remove(g_uid);
				TalkToOneWithStyle.this.setVisible(false);
			}
		});

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Hashtable<String, TalkToOneWithStyle> tempHash = Information
						.getOpenedWindow();
				tempHash.remove(g_uid);
				TalkToOneWithStyle.this.setVisible(false);
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
			if (isOnline) {

				Msg sendMsg = new Msg();
				sendMsg.setHead(Option.MESSAGE_WITH_ATTRIB);
				sendMsg.setBody(g_fa);
				sendPacket = toDatagram(sendMsg, inetAddr, portNumber);
				datagramClient.send(sendPacket);
			} else {
				System.out.println("这B没在线，不发了");
			}

			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
			g_msg = (Information.getUserInfo().getNickname() + "  "
					+ dateFormat.format(now) + " \n" + g_fa.getText() + "\n");
			talkLogBw.write(new String(DESCoder.encryptBASE64(DESCoder.encrypt(
					("∵" + g_msg).getBytes(), Information.getDesKey()))));
			talkLogBw.flush();
			inputPane.requestFocusInWindow();
			return true;
		} catch (Exception e) {

			return false;
		}

	}// 发送信息，返回一个布尔值，来验证是否发送成功

	private void insertString(MessageWithAttrib attr) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
			styledDoc.insertString(styledDoc.getLength(), Information
					.getUserInfo().getNickname()
					+ "  " + dateFormat.format(attr.getSendTime()) + "\n", getDefaultAttr());
			styledDoc.insertString(styledDoc.getLength(), "  " + attr.getText()
					+ "\n", attr.getAttrSet());
			displayPane.setSelectionStart(displayPane.getText().length());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	// ∵∴

	public void displayReceivedMessage(MessageWithAttrib attr) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
			styledDoc.insertString(styledDoc.getLength(), attr.getSenderName()
					+ " : " + dateFormat.format(attr.getSendTime()) + "\n", getDefaultAttr());
			styledDoc.insertString(styledDoc.getLength(), "  " + attr.getText()
					+ "\n", attr.getAttrSet());
			displayPane.setSelectionStart(displayPane.getText().length());
			g_msg = (attr.getSenderName() + "  "
					+ dateFormat.format(attr.getSendTime()) + " \n"
					+ attr.getText() + "\n");
			talkLogBw.write(new String(DESCoder.encryptBASE64(DESCoder.encrypt(
					("∴" + g_msg).getBytes(), Information.getDesKey()))));
			// talkLogBw.newLine();
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
				inputPane.setText(null);// 发送成功则将发送文本清空
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
			scu.im.utils.Print.print("translation" + e.getMessage());
			e.printStackTrace();
		}
		return tempByte;
	}

	private void initSocket(String ip, String port, String uid)
			throws HeadlessException {
		try {
			this.g_uid = uid;
			inetAddr = InetAddress.getByName(ip);
			portNumber = Integer.parseInt(port);
			datagramClient = new DatagramSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void willStartVoice(VoicChatRequestType vcrt) {
		int n = JOptionPane.showConfirmDialog(null, "收到"
				+ vcrt.getRequestName() + "的语音聊天请求", "Info",
				JOptionPane.YES_NO_OPTION);
		Msg voiceResponseMsg = new Msg();
		VoiceChatResponseType responseVcrt = new VoiceChatResponseType();
		voiceResponseMsg.setHead(Option.VOICE_CHAT_RESPONSE);
		voiceResponseMsg.setBody(responseVcrt);
		responseVcrt.setResponseIp(Information.getIp());
		responseVcrt.setResponseName(Information.getUserInfo().getNickname());
		responseVcrt.setResponseUid(Information.getUid());

		if (n == 0) {
			// accecpt
			responseVcrt.setAccepted(true);
			new CaptureSocket(vcrt.getRequestIp(), 62301).start();
			new PlayBackSocket(2301).start();
			Function funciton = new Function(g_uid, inetAddr);
			RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0, 0,
					funciton.getWidth(), funciton.getHeight(), 50, 50);
			WindowUtils.setWindowMask(funciton, mask);
			funciton.setVisible(true);

		} else if (n == 1) {
			// refuse
			responseVcrt.setAccepted(false);
		}

		// 获得对方ip，使用udp端口成包
		try {
			InetAddress fileInet = InetAddress.getByName(vcrt.getRequestIp());
			sendPacket = toDatagram(voiceResponseMsg, fileInet, Information
					.getUdpPort());
			datagramClient.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 发送
		if (n == 0) {
			g_time = g_sdf.format(new Date());
			try {
				styledDoc.insertString(styledDoc.getLength(), g_time + "\n"
						+ "接受了对方的语音聊天请求" + "\n", getDefaultAttr());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	public void startVoice(VoiceChatResponseType responseVcrt) {
		if (responseVcrt.isAccepted()) {

			Socket voiceclientSocket;
			try {
				styledDoc.insertString(styledDoc.getLength(), responseVcrt
						.getResponseName()
						+ "接受了你的语音请求" + "\n", getDefaultAttr());
				voiceclientSocket = new Socket(responseVcrt.getResponseIp(),
						2301);
				Capture cap = new Capture(voiceclientSocket);
				cap.start();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}

			Function funciton = new Function(g_uid, inetAddr);
			RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0, 0,
					funciton.getWidth(), funciton.getHeight(), 50, 50);
			WindowUtils.setWindowMask(funciton, mask);
			funciton.setVisible(true);

		} else {
			try {
				styledDoc.insertString(styledDoc.getLength(), responseVcrt
						.getResponseName()
						+ "拒绝了你的语音请求" + "\n", getDefaultAttr());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}

			try {
				Information.getVoiceRequestServerSocket().setSoTimeout(10);
				Information.getVoiceRequestServerSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("语音聊天监听线程已关闭");
			}

		}
	}

	public void stopVoice() {
		try {
			if ((Information.getVoiceResponseClientSocket() != null)
					&& (!Information.getVoiceResponseClientSocket().isClosed())) {
				Information.getVoiceResponseClientSocket().close();
			}
			if ((Information.getVoiceRequestServerSocket() != null)
					&& (!Information.getVoiceRequestServerSocket().isClosed())) {
				Information.getVoiceRequestServerSocket().close();
			}
			styledDoc.insertString(styledDoc.getLength(), g_time + "\n"
					+ "语音聊天已经中止" + "\n", getDefaultAttr());

			Msg stopVoiceResponseMsg = new Msg();
			String responseVoiceStr = Information.getUid();
			stopVoiceResponseMsg.setHead(Option.STOP_VOICE_RESPONSE_TYPE);
			stopVoiceResponseMsg.setBody(responseVoiceStr);
			sendPacket = toDatagram(stopVoiceResponseMsg, inetAddr, Information
					.getUdpPort());
			datagramClient.send(sendPacket);

		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopVoice2() {
		try {
			if ((Information.getVoiceRequestServerSocket() != null)
					&& (!Information.getVoiceRequestServerSocket().isClosed())) {
				Information.getVoiceRequestServerSocket().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void isFileAccept(TCPFileRequestType request) {
		// 传送文件第三步，接收方选择是否接收将要传输的文件
		try {

			int n = JOptionPane.showConfirmDialog(null, "要接受来自"
					+ request.getSenderName() + "(" + request.getSenderUid()
					+ ")" + "的" + request.getFilename(), "收到文件请求",
					JOptionPane.YES_NO_OPTION);
			// 选择是否接收

			Msg fileTransMsg = new Msg();
			fileTransMsg.setHead(Option.TCP_FILE_RESPONSE_TYPE);
			TCPFileResponseType fileResponse = new TCPFileResponseType();
			fileResponse.setSenderUid(Information.getUid());
			fileResponse.setSenderName(Information.getUserInfo().getNickname());
			fileResponse.setSenderIp(Information.getIp());

			if (n == 0) {
				fileResponse.setAccecpted(true);
				// 接受
			} else {
				fileResponse.setAccecpted(false);
				// refuse
			}

			fileTransMsg.setBody(fileResponse);
			// 新的TCPFileResponesType，包含回应（true/flase）
			InetAddress fileInet = InetAddress.getByName(request.getSenderIp());

			sendPacket = toDatagram(fileTransMsg, fileInet, Information
					.getUdpPort());
			// 获得对方ip，使用udp端口成包
			datagramClient.send(sendPacket);
			// 发送
			if (n == 0) {
				new TCPFileReceiver();
				g_time = g_sdf.format(new Date());
				styledDoc.insertString(styledDoc.getLength(), g_time + "\n"
						+ "文件接受完毕" + "\n", getDefaultAttr());

			}
			// 启动接收线程,第三步结束，下一步回到发送方分析回应
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void willFileSend(TCPFileResponseType frt) {
		if (frt.isAccecpted()) {
			TCPFileSender sender = new TCPFileSender(frt.getSenderIp(),
					Information.getTransFilePort());

			if (sender.isConnected()) {
				try {
					g_time = g_sdf.format(new Date());
					styledDoc.insertString(styledDoc.getLength(), g_time + "\n"
							+ "开始传输文件 " + g_file.getName() + "\n",
							getDefaultAttr());
					sender.sendFile(g_file);
					g_time = g_sdf.format(new Date());
					styledDoc.insertString(styledDoc.getLength(), g_time + "\n"
							+ "文件传输完毕" + "\n", getDefaultAttr());
					sender.close();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}

			}
			// 第五步:对方接收，启动传送文件
		} else {
			try {
				g_time = g_sdf.format(new Date());
				styledDoc.insertString(styledDoc.getLength(), g_time + "\n"
						+ "对方拒绝接受文件" + "\n", getDefaultAttr());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			// 对方拒绝
		}
	}

	private MessageWithAttrib getMessageWithAttrib() {

		MessageWithAttrib tempAtt = new MessageWithAttrib();
		tempAtt.setGid(null);
		tempAtt.setSenderUid(Information.getUid());
		tempAtt.setReceiverUid(g_uid);
		tempAtt.setSenderName(Information.getUserInfo().getNickname());
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
		tempAtt.setReceiverIp(inetAddr.getHostAddress());
		tempAtt.setSendTime(new Date());

		return tempAtt;
	}// 得到发送的内容的属性集

	private SimpleAttributeSet getDefaultAttr() {

		defaultAttr = new SimpleAttributeSet();
		StyleConstants.setBold(defaultAttr, false);
		StyleConstants.setItalic(defaultAttr, false);
		StyleConstants.setFontSize(defaultAttr, 20);
		StyleConstants.setForeground(defaultAttr, new Color(51, 51, 255));
		StyleConstants.setFontFamily(defaultAttr, "微软雅黑");
		return defaultAttr;
	}// 获得默认的系统字体，供程序提示用户使用

	public void backGround() {
		((JPanel) this.getContentPane()).setOpaque(false);
		ImageIcon img1 = new ImageIcon(".\\image\\" + file + "\\stto"
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

	private void mini(java.awt.event.MouseEvent evt) {
		this.setExtendedState(JFrame.ICONIFIED);
	}

	// 私有变量的声明
	private JScrollPane displayScroll, inputScroll, chatLogScroll;
	private JTextPane displayPane;
	private JTextPane inputPane;
	private JButton sendButton;
	private JButton closeButton1;
	/* private JButton testButton; */
	private JPanel mainPanel;
	private byte[] sendBuf = null;
	private DatagramSocket datagramClient = null;
	private DatagramPacket sendPacket = null;
	private static InetAddress inetAddr = null;// 这是对方的ip
	private static int portNumber;
	private BufferedWriter talkLogBw = null;
	private FileWriter talkLogFw = null;
	private String g_uid = null;
	private String userName;
	// 通信单元变量的初始化

	private JComboBox fontNameBox, fontSizeBox, fontStyleBox = null;
	private StyledDocument styledDoc = null;
	private Color tempColor = null;
	private String[] fontNameLibrary, fontSizeLibrary, fontStyleLibrary = null;
	private JButton colorButton, showFontPanelButton, fileTransButton = null;
	private JPanel fontPanel;
	private MessageWithAttrib g_fa = null;
	private SimpleAttributeSet defaultAttr = null;

	// 字体控制初始化
	private SimpleDateFormat g_sdf = null;
	private String g_time = null;
	private String g_msg = null;
	private FriendUnitType friendinfo = null;
	private TCPFileRequestType fileRequestInfo = null;
	private MessageWithAttrib messageInfo = null;
	private TCPFileResponseType responseInfo = null;
	private File g_file = null;
	private Hashtable<String, TalkToOneWithStyle> tempHash = null;
	private JLabel backgroundLabel;
	private JLabel topLabel;
	private JButton minisizeButton;
	private JButton closeButton;
	private boolean isDraging;
	private int i, j;
	private JFrame thisFrame;

	private JButton talklogButton;
	private JTextPane talklogTextPane;
	private JButton voiceTalk;
	private boolean isChatRecordVisible = false;
	private String file = "talkToOne1";
	private boolean isOnline;

	public static void main(String[] args) {
		new TalkToOneWithStyle("603977", "127.0.0.1", "mo");
	}
}
