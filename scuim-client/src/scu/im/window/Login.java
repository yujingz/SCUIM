/*************************************************************************************************
 * @author : 彭则荣 
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 登录界面，实现与数据库交互，验证用户名及密码。本地记录登录信息。可选择记住密码。可选择
 *                登录状态。设有到注册账号页面的链接。
 *                这是一个虽然简单却修改比较多的窗口，修改MyscuimFrame.java的GUI时就
 *                基本都要对应修改此窗口。
 *                记录密码保存在本地，但做了加密处理，登录与加密，秋水长天一色，请客户放心。
 * 
 * 
 *************************************************************************************************/

package scu.im.window;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import scu.im.msgtype.LoginReturnType;
import scu.im.msgtype.LoginType;
import scu.im.msgtype.Msg;
import scu.im.msgtype.Option;
import scu.im.security.DESCoder;
import scu.im.service.ListenTCPThread;
import scu.im.service.ListenUDPThread;
import scu.im.socket.IMSocket;
import scu.im.utils.Information;

import com.sun.jna.examples.WindowUtils;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;

	// main方法
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				Login login = new Login();
				login.setVisible(true);
				// 圆角
				RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0, 0,
						login.getWidth(), login.getHeight(), 50, 50);
				WindowUtils.setWindowMask(login, mask);
			}
		});
	}

	public Login() {

		initAccount();
		initComponents();
		this.setSize(376, 245);

		for (String str : pwdTable.keySet()) {
			dcbm.addElement(str);
		}

		String defaultUser = (String) uidBox.getSelectedItem();
		if (defaultUser != null) {
			if (pwdTable.get(defaultUser).equals("00000")) {
				keepPasswordCheckBox.setSelected(false);
				passwordField.setText(null);
			} else {
				keepPasswordCheckBox.setSelected(true);
			}
			System.out.println(stateTable.get(defaultUser));
			if (stateTable.get(defaultUser).equals("1")) {
				stateComboBox.setSelectedIndex(0);
			} else if (stateTable.get(defaultUser).equals("2")) {
				stateComboBox.setSelectedIndex(1);
			}
			stateComboBox.repaint();
		} else {
			keepPasswordCheckBox.setSelected(false);
			passwordField.setText(null);
		}
	}

	private void initAccount() {
		pwdTable = new Hashtable<String, String>();
		stateTable = new Hashtable<String, String>();
		try {
			FileReader fr = new FileReader(".\\UserInfo\\log.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				line = new String(DESCoder.decrypt(
						DESCoder.decryptBASE64(line), Information
								.getAccountKey()));
				String[] result = line.split("\\#");
				pwdTable.put(result[0], result[1]);
				stateTable.put(result[0], String.valueOf((Integer
						.parseInt(result[2]))));
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {
		backGround();// 调用背景设置方法
		this.setUndecorated(true); // 去掉标题栏
		this.setTitle(" 登录 SCUIM");
		this.setLayout(null);

		// 修改窗体风格
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 让窗体出现在屏幕中间
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenwidth = screenSize.width;
		screenheight = screenSize.height;
		int x = screenwidth / 3;
		int y = screenheight / 3;
		setLocation(x, y);
		// 任务图标换为本软件logo
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage(".\\image\\trayImg\\logo.png");
		this.setIconImage(image);

		backgroundLabel = new JLabel();
		topLabel = new JLabel();
		numLabel = new JLabel("账号：");
		passwordLabel = new JLabel("密码：");
		typeLabel = new JLabel("状态");
		mention1 = new JLabel();
		mention2 = new JLabel();
		loginButton = new JButton();
		setButton = new JButton();
		minisizeButton = new JButton();
		closeButton = new JButton();
		keepPasswordCheckBox = new JCheckBox("记住密码");
		AutoLoginCheckBox = new JCheckBox();
		dcbm = new DefaultComboBoxModel();
		uidBox = new JComboBox(dcbm);
		uidBox.setEditable(true);
		stateComboBox = new JComboBox(new String[] { "在线", "隐身" });
		passwordField = new JPasswordField("123456");

		uidBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = (String) uidBox.getSelectedItem();
				if (pwdTable.containsKey(user)) {
					if (pwdTable.get(user).equals("00000")) {
						passwordField.setText(null);
						keepPasswordCheckBox.setSelected(false);
					} else {
						passwordField.setText(pwdTable.get(user));
						keepPasswordCheckBox.setSelected(true);
					}
				} else {
					passwordField.setText(null);
					keepPasswordCheckBox.setSelected(false);
				}
			}
		});
//则二你这个sb
		this.add(topLabel);
		this.add(numLabel);
		this.add(passwordLabel);
		this.add(typeLabel);
		this.add(mention1);
		this.add(mention2);
		this.add(loginButton);
		this.add(setButton);
		this.add(minisizeButton);
		this.add(closeButton);
		this.add(keepPasswordCheckBox);
		this.add(AutoLoginCheckBox);
		this.add(stateComboBox);
		this.add(passwordField);
		this.add(uidBox);

		topLabel.setBounds(0, 0, 307, 30);
		numLabel.setBounds(35, 78, 40, 23);
		passwordLabel.setBounds(35, 116, 40, 23);
		typeLabel.setBounds(35, 155, 40, 23);
		mention1.setBounds(290, 78, 50, 23);
		mention2.setBounds(290, 116, 50, 23);
		loginButton.setBounds(280, 210, 80, 28);
		setButton.setBounds(15, 210, 80, 28);
		minisizeButton.setBounds(305, 4, 35, 20);
		closeButton.setBounds(335, 4, 35, 20);
		keepPasswordCheckBox.setBounds(130, 155, 80, 21);
		AutoLoginCheckBox.setBounds(200, 155, 80, 21);
		stateComboBox.setBounds(75, 155, 50, 22);
		passwordField.setBounds(75, 116, 205, 22);
		uidBox.setBounds(75, 78, 205, 22);

		minisizeButton.setContentAreaFilled(false); // 按钮透明
		minisizeButton.setBorderPainted(false);// 去边框
		minisizeButton.setIcon(new ImageIcon(".\\image\\button\\zxh1.png"));

		closeButton.setContentAreaFilled(false); // 按钮透明
		closeButton.setBorderPainted(false);
		closeButton.setIcon(new ImageIcon(".\\image\\button\\close1.png"));

		loginButton.setContentAreaFilled(false); // 按钮透明
		loginButton.setBorderPainted(false);// 去边框
		loginButton.setIcon(new ImageIcon(".\\image\\login\\login3.png"));

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				loginActionPerformed(evt);// login事件
			}
		});
		loginButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				loginButton
						.setIcon(new ImageIcon(".\\image\\login\\login4.png"));
			}

			public void mouseExited(MouseEvent evt) {
				loginButton
						.setIcon(new ImageIcon(".\\image\\login\\login3.png"));
			}
		});

		setButton.setContentAreaFilled(false); // 按钮透明
		setButton.setBorderPainted(false);// 去边框
		setButton.setIcon(new ImageIcon(".\\image\\login\\set3.png"));
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.out.println("set what?");
			}
		});
		setButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				setButton.setIcon(new ImageIcon(".\\image\\login\\set4.png"));
			}

			public void mouseExited(MouseEvent evt) {
				setButton.setIcon(new ImageIcon(".\\image\\login\\set3.png"));
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

		mention1.setText("没有账号");
		mention1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				System.out.println("you can use xxxxxxxx ");
			}

			public void mouseEntered(MouseEvent evt) {
				mention1.setText("立即注册");
			}

			public void mouseExited(MouseEvent evt) {
				mention1.setText("没有账号");
			}
		});

		mention2.setText("忘记密码");
		mention2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				System.out.println("you can use xxxxxxxx ");
			}

			public void mouseEntered(MouseEvent evt) {
				mention2.setText("取回密码");
			}

			public void mouseExited(MouseEvent evt) {
				mention2.setText("忘记密码");
			}
		});

		keepPasswordCheckBox.setContentAreaFilled(false);
		AutoLoginCheckBox.setContentAreaFilled(false);
		AutoLoginCheckBox.setText("自动登录");

		// 拖拽窗口
		topLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isDraging = true;
				g_i = e.getX();
				g_j = e.getY();
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
					setLocation(left + e.getX() - g_i, top + e.getY() - g_j);
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

	}

	// 登录事件
	private void loginActionPerformed(ActionEvent evt) {
		LoginType loginType = new LoginType();
		g_uid = (String) uidBox.getSelectedItem();
		loginType.setUid(g_uid);
		loginType.setPwd(passwordField.getText());
		try {
			Socket clientSocket = IMSocket.getInstance();
			loginType.setPort(Integer.toString(clientSocket.getPort()));
			// StringTokenizer st = new
			// StringTokenizer(InetAddress.getLocalHost()
			// .toString(), "/");
			// st.nextToken();
			// ip = st.nextToken();
			// loginType.setIp(ip);
			loginType.setState(Integer.toString(1));

			if ("在线".equals(stateComboBox.getModel().getSelectedItem())) {
				loginType.setState("1");
			} else {
				loginType.setState("2");
			}
			ObjectOutputStream outToServer = new ObjectOutputStream(
					clientSocket.getOutputStream());
			ObjectInputStream inFromServer = new ObjectInputStream(clientSocket
					.getInputStream());

			Msg msg = new Msg();
			msg.setHead(Option.LOGIN);
			msg.setBody(loginType);
			outToServer.writeObject(msg);
			outToServer.flush();

			Msg msgReturn = (Msg) inFromServer.readObject();
			LoginReturnType loginReturnType = (LoginReturnType) msgReturn
					.getBody();
			if (false == loginReturnType.getResult()) {
				scu.im.utils.Print.print("登录失败！");
			} else {
				scu.im.utils.Print.print("登录成功！");

				/***********************************************************
				 **************************写入日志**************************
				 ************************************************************/

				if (keepPasswordCheckBox.isSelected()) {
					pwdTable.put(g_uid, passwordField.getText());
				} else {
					pwdTable.put(g_uid, "00000");
				}
				switch (stateComboBox.getSelectedIndex()) {
				case 0:
					stateTable.put(g_uid, "1");
					break;
				case 1:
					stateTable.put(g_uid, "2");
					break;
				}

				FileWriter fw = null;
				try {
					fw = new FileWriter(".\\UserInfo\\log.txt", false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				BufferedWriter bw = new BufferedWriter(fw);

				Enumeration<String> en = pwdTable.keys();
				while (en.hasMoreElements()) {
					String tempUid = en.nextElement();
					System.out.println(stateTable.get(tempUid));
					bw.write(new String(DESCoder.encryptBASE64(DESCoder
							.encrypt(
									(tempUid + "#" + pwdTable.get(tempUid)
											+ "#" + stateTable.get(tempUid))
											.getBytes(), Information
											.getAccountKey()))));
					bw.flush();
				}

				Information.setUid(g_uid);
				Information.setDesKey(DESCoder.initKey(g_uid
						+ loginType.getPwd()));
				Information.setIp(loginReturnType.getLogIp());// 获得并设置服务器发回的本用户的公网IP
				Information.setUserInfo(loginReturnType.getUserInfo());
				this.setVisible(false);
				ListenTCPThread listener = new ListenTCPThread();
				ListenUDPThread udpListener = new ListenUDPThread();
				Information.setListener(listener);
				Information.setUdpListener(udpListener);
				MyScuimFrame mainGui = new MyScuimFrame();
				mainGui.setSize(new Dimension(240, 610));// 窗体大小
				mainGui.setResizable(false);// 固定窗体大小
				RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0, 0,
						mainGui.getWidth(), mainGui.getHeight(), 45, 45);
				WindowUtils.setWindowMask(mainGui, mask);
				listener.start();
				udpListener.start();
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mini(MouseEvent evt) {
		this.setExtendedState(JFrame.ICONIFIED);
	}

	private void close(MouseEvent evt) {
		System.exit(0);
	}

	// 背景设置
	public void backGround() {
		((JPanel) this.getContentPane()).setOpaque(false);// 取一个透明panel--this
		ImageIcon backImage = new ImageIcon(".\\image\\login\\login_back1.png");

		// 确保可以更换背景
		if (backgroundLabel != null) {
			getLayeredPane().remove(backgroundLabel);// 移除承载背景的label
			getLayeredPane().validate();// 重新构入 确保组件具有有效布局
		}

		backgroundLabel = new JLabel(backImage);
		this.getLayeredPane().add(backgroundLabel,
				new Integer(Integer.MIN_VALUE));// 获取层次panel
		backgroundLabel.setBounds(0, 0, backImage.getIconWidth(), backImage
				.getIconHeight());
	}

	private JLabel backgroundLabel;
	private JLabel topLabel;
	private JLabel numLabel;
	private JLabel passwordLabel;
	private JLabel typeLabel;
	private JLabel mention1;
	private JLabel mention2;
	private JButton loginButton;
	private JButton setButton;
	private JCheckBox keepPasswordCheckBox;
	private JCheckBox AutoLoginCheckBox;
	private JComboBox stateComboBox;
	private JPasswordField passwordField;
	private JButton minisizeButton;
	private JButton closeButton;
	private String g_uid = null;
	private int screenwidth;
	private int screenheight;
	private boolean isDraging;

	private int g_i, g_j;
	private JComboBox uidBox;
	private Hashtable<String, String> pwdTable;
	private Hashtable<String, String> stateTable;
	private DefaultComboBoxModel dcbm;

}
