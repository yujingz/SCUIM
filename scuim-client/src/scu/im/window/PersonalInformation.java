/*************************************************************************************************
 * @author : 彭则荣
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 个人信息界面的构建。可被继承作为好友个人信息界面<只供查看>，
 *                亦可（曾亦可的亦可）被继承作为好友个人信息界面<可修改信息>。
 *                信息涵盖俱全，完全参照公安局记录犯人信息的方式。在这我们是本着，你可以保持
 *                不沉默，但我们会让你沉默的原则。保护用户信息是我们的职责！当然了，说是这么说哈。
 * 
 *
 *************************************************************************************************/

package scu.im.window;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import scu.im.msgtype.Msg;
import scu.im.msgtype.Option;
import scu.im.msgtype.SubmitUserInfoType;
import scu.im.msgtype.UserInfoRequestType;
import scu.im.msgtype.UserInfoResponseType;
import scu.im.socket.IMSocket;
import scu.im.utils.Information;

import com.sun.jna.examples.WindowUtils;

public class PersonalInformation extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel backgroundlabel;
	JLabel faceLabel = new JLabel();
	JLabel topLabel = new JLabel();
	JButton changeButton = new JButton();
	JButton surebutton = new JButton();
	JButton updateButton = new JButton();
	JButton cancelbutton = new JButton();
	JButton minisizebutton = new JButton();// 最小化按钮
	JButton closebutton = new JButton();// 关闭按钮

	JPanel jpanel1 = new JPanel();
	JLabel nameLabel = new JLabel("昵称 : ");
	JLabel numberLabel = new JLabel("账号 : (绑定) ");
	JLabel signLabel = new JLabel("个性签名 : ");
	JLabel sexLabel = new JLabel("性别 : ");
	JLabel birthdayLabel = new JLabel("生日 : ");
	JLabel ageLabel = new JLabel("年龄 : ");
	JLabel animalLabel = new JLabel("生肖 : ");
	JLabel constellationLabel = new JLabel("星座 : ");
	JLabel bloodTypeLabel = new JLabel("血型 :　");
	JLabel countryLabel = new JLabel("国家/地区 : ");
	JLabel provinceLabel = new JLabel("省份/州 : ");
	JLabel cityLabel = new JLabel("城市/城镇 : ");
	JLabel phoneLabel = new JLabel("电话 : ");
	JLabel mailLabel = new JLabel("e-mail : ");
	JLabel instructionLabel = new JLabel("个人说明 : ");
	JTextField numText = new JTextField();
	JTextField nickNameText = new JTextField();
	JTextField countryText = new JTextField();
	JTextField provinceText = new JTextField();
	JTextField cityText = new JTextField();
	JTextField phoneText = new JTextField();
	JTextField mailText = new JTextField();
	JTextPane signText = new JTextPane();
	JTextPane intruductionText = new JTextPane();
	JComboBox sexComboBox = new JComboBox();
	JComboBox yearComboBox = new JComboBox();
	JComboBox monthComboBox = new JComboBox();
	JComboBox dayComboBox = new JComboBox();
	JComboBox ageComboBox = new JComboBox();
	JComboBox animalComboBox = new JComboBox();
	JComboBox constellationComboBox = new JComboBox();
	JComboBox bloodTypeComboBox = new JComboBox();

	// private MyScuimFrame mSf = new MyScuimFrame();

	private ChangeFace changeFaceDialog;
	private int imageNO;
	private boolean isDraging;
	private int m, n;
	private String g_uid;
	private JFrame thisFrame;
	// private int accountNum = 12345;

	// public static void main(String args[]) {
	// // personalInformation f = new personalInformation(null);
	// // f.setSize(new Dimension(650, 620));//窗体大小
	// // f.setResizable(false);// 固定窗体大小
	// // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// java.awt.EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// new personalInformation(mSf).setVisible(true);
	// }
	// });
	// //
	// }

	public PersonalInformation(String uid) {
		g_uid = uid;
		Information.getPersonalInfoHash().put(g_uid, this);
		initComponents();
		updateUserInfo();

	}

	private void initComponents() {
		backGround();
		setLocation(100, 40);
		this.setSize(new Dimension(540, 490));// 窗体大小
		this.setResizable(false);// 固定窗体大小
		this.setUndecorated(true); // 去掉标题栏
		thisFrame = this;

		RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0, 0,
				thisFrame.getWidth(), thisFrame
						.getHeight(), 45, 45);
		WindowUtils.setWindowMask(thisFrame, mask);
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage(".\\image\\trayImg\\logo.png");
		this.setIconImage(image);
		this.setTitle(" 修改资料");
		Container container = getContentPane(); // 获取JFrame面板
		JPanel jpanel = new JPanel(); // 创建个JPanel
		jpanel.setOpaque(false);// jp透明
		// jpanel.setBorder(javax.swing.BorderFactory
		// .createLineBorder(new java.awt.Color(0, 0, 0)));// 边框
		jpanel1.setOpaque(false);

		jpanel1.setLayout(null);// 把JPanel设置为透明 这样就不会遮住后面的背景 这样你就能在JPanel随意加组件了
		container.add(jpanel);
		jpanel.setLayout(null);// jp的布局为空
		jpanel.setBounds(0, 0, 638, 582);
		jpanel.add(faceLabel);
		jpanel.add(topLabel);
		jpanel.add(changeButton);
		jpanel.add(surebutton);
		jpanel.add(updateButton);
		jpanel.add(cancelbutton);
		jpanel.add(jpanel1);
		jpanel.add(minisizebutton);
		jpanel.add(closebutton);
		// jpanel1.add(showBorder(
		// new BevelBorder(BevelBorder.RAISED)));

		numText.setText(g_uid);

		jpanel1.add(nameLabel);
		jpanel1.add(numberLabel);
		jpanel1.add(signLabel);
		jpanel1.add(sexLabel);
		jpanel1.add(birthdayLabel);
		jpanel1.add(ageLabel);
		jpanel1.add(animalLabel);
		jpanel1.add(constellationLabel);
		jpanel1.add(bloodTypeLabel);
		jpanel1.add(countryLabel);
		jpanel1.add(provinceLabel);
		jpanel1.add(cityLabel);
		jpanel1.add(phoneLabel);
		jpanel1.add(mailLabel);
		jpanel1.add(instructionLabel);
		jpanel1.add(numText);
		jpanel1.add(nickNameText);
		jpanel1.add(countryText);
		jpanel1.add(provinceText);
		jpanel1.add(cityText);
		jpanel1.add(phoneText);
		jpanel1.add(mailText);
		jpanel1.add(signText);
		jpanel1.add(intruductionText);
		jpanel1.add(sexComboBox);
		jpanel1.add(yearComboBox);
		jpanel1.add(monthComboBox);
		jpanel1.add(dayComboBox);
		jpanel1.add(ageComboBox);
		jpanel1.add(animalComboBox);
		jpanel1.add(constellationComboBox);
		jpanel1.add(bloodTypeComboBox);

		// img1.setImage(img1.getImage().getScaledInstance(80,90,10));

		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (changeFaceDialog != null) {
					if (changeFaceDialog.isVisible()) {
						changeFaceDialog.setVisible(false);
					}
				}
			}
		});

		topLabel.setBounds(0, 0, 470, 25);
		topLabel.setText("  " + g_uid + "的信息");
		topLabel.setFont(new Font("  " + g_uid + "的信息", Font.BOLD, 13));
		topLabel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}

			public void mouseExited(MouseEvent evt) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		topLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isDraging = true;
				m = e.getX();
				n = e.getY();
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				
				if (changeFaceDialog != null) {
					if (changeFaceDialog.isVisible()) {
						changeFaceDialog.setVisible(false);
					}
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
					setLocation(left + e.getX() - m, top + e.getY() - n);
				}
			}
		});
		faceLabel.setBounds(15, 23, 64, 64);
		// faceLabel.setBorder(javax.swing.BorderFactory
		// .createLineBorder(new java.awt.Color(0, 0, 0)));

		changeButton.setBounds(15, 100, 65, 23);
		changeButton.setIcon(new ImageIcon(
				".\\image\\button\\change1.png"));
		changeButton.setContentAreaFilled(false); // 按钮透明
		changeButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				changeFace(evt);
			
			}

			public void mouseEntered(MouseEvent evt) {
				changeButton.setIcon(new ImageIcon(
						".\\image\\button\\change2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				changeButton.setIcon(new ImageIcon(
						".\\image\\button\\change1.png"));
			}
		});

		surebutton.setBounds(270, 460, 75, 23);
		surebutton.setIcon(new ImageIcon(
				".\\image\\button\\sure1.png"));
		surebutton.setContentAreaFilled(false); // 按钮透明
		surebutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				makesure(evt);
				if (changeFaceDialog != null) {
					if (changeFaceDialog.isVisible()) {
						changeFaceDialog.setVisible(false);
					}
				}
			}

			public void mouseEntered(MouseEvent evt) {
				surebutton.setIcon(new ImageIcon(
						".\\image\\button\\sure2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				surebutton.setIcon(new ImageIcon(
						".\\image\\button\\sure1.png"));
			}
		});

		updateButton.setBounds(358, 460, 75, 23);
		updateButton.setIcon(new ImageIcon(
				".\\image\\button\\update1.png"));
		updateButton.setContentAreaFilled(false); // 按钮透明

		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateUserInfo();
				if (changeFaceDialog != null) {
					if (changeFaceDialog.isVisible()) {
						changeFaceDialog.setVisible(false);
					}
				}
			}
		});

		cancelbutton.setBounds(445, 460, 75, 23);
		cancelbutton.setContentAreaFilled(false); // 按钮透明
		cancelbutton.setIcon(new ImageIcon(
				".\\image\\button\\cancel1.png"));
		cancelbutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (changeFaceDialog != null) {
					changeFaceDialog.setVisible(false);
					changeFaceDialog = null;
				}
				PersonalInformation.this.setVisible(false);
			}

			public void mouseEntered(MouseEvent evt) {
				cancelbutton.setIcon(new ImageIcon(
						".\\image\\button\\cancel2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				cancelbutton.setIcon(new ImageIcon(
						".\\image\\button\\cancel1.png"));
			}
		});

		updateButton.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent evt) {
				updateButton.setIcon(new ImageIcon(
						".\\image\\button\\update2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				updateButton.setIcon(new ImageIcon(
						".\\image\\button\\update1.png"));
			}
		});

		minisizebutton.setBounds(465, 0, 35, 23);
		minisizebutton.setContentAreaFilled(false); // 按钮透明
		minisizebutton.setBorderPainted(false);
		minisizebutton.setIcon(new ImageIcon(
				".\\image\\button\\zxh1.png"));
		closebutton.setBounds(493, 0, 35, 23);
		closebutton.setContentAreaFilled(false); // 按钮透明
		closebutton.setBorderPainted(false);
		closebutton.setIcon(new ImageIcon(
				".\\image\\button\\close1.png"));
		minisizebutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				mini(evt);
				if (changeFaceDialog != null) {
					if (changeFaceDialog.isVisible()) {
						changeFaceDialog.setVisible(false);
					}
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

		jpanel1.setBounds(100, 23, 430, 420);
		nameLabel.setBounds(10, 10, 100, 16);
		numberLabel.setBounds(220, 10, 100, 16);
		nickNameText.setBounds(10, 30, 200, 20);
		numText.setBounds(220, 30, 200, 20);
		numText.setEditable(false);
		signLabel.setBounds(10, 55, 100, 16);
		signText.setBounds(10, 75, 410, 65);
		sexLabel.setBounds(10, 145, 80, 16);
		sexComboBox.setBounds(10, 165, 80, 20);
		sexComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"女", "男", "其它" }));
		birthdayLabel.setBounds(100, 145, 90, 16);
		yearComboBox.setBounds(100, 165, 100, 20);
		yearComboBox.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "1999", "1998", "1997", "1996", "1995", "1994",
						"1993", "1992", "1991", "1990", "1989", "1988", "1987",
						"1986", "1985", "1984", "1983", "1982", "1981", "1980",
						"1979", "1978", "1977", "1976", "1975", "1974", "1973",
						"1972", "1971", "1970", "1969", "1968", "1967", "1966",
						"1965", "1964", "1963", "1962", "1961", "1960" }));
		monthComboBox.setBounds(210, 165, 100, 20);
		monthComboBox.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
						"10", "11", "12" }));
		dayComboBox.setBounds(320, 165, 100, 20);
		dayComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
				"13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
				"23", "24", "25", "26", "27", "28", "29", "30", "31" }));
		ageLabel.setBounds(10, 190, 80, 16);
		animalLabel.setBounds(100, 190, 80, 16);
		constellationLabel.setBounds(210, 190, 80, 16);
		bloodTypeLabel.setBounds(320, 190, 80, 16);
		ageComboBox.setBounds(10, 210, 80, 20);
		ageComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"50", "49", "48", "47", "46", "45", "44", "43", "42", "41",
				"40", "39", "38", "37", "36", "35", "34", "33", "32", "31",
				"30", "29", "28", "27", "26", "25", "24", "23", "22", "21",
				"20", "19", "18", "17", "16", "15", "14", "13", "12", "11",
				"10", "9", "8", "7", "6", "5", "4", "3", "2", "1" }));
		animalComboBox.setBounds(100, 210, 100, 20);
		animalComboBox.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴",
						"鸡", "狗", "猪" }));
		constellationComboBox.setBounds(210, 210, 100, 20);
		constellationComboBox.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座",
						"处女座", "天秤座", "天蝎座", "射手座", "摩羯座" }));
		bloodTypeComboBox.setBounds(320, 210, 100, 20);
		bloodTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "A", "B", "O", "AB", "其他" }));
		countryLabel.setBounds(10, 235, 100, 16);
		provinceLabel.setBounds(148, 235, 100, 16);
		cityLabel.setBounds(286, 235, 100, 16);
		countryText.setBounds(10, 255, 133, 20);
		provinceText.setBounds(148, 255, 133, 20);
		cityText.setBounds(286, 255, 133, 20);
		phoneLabel.setBounds(10, 280, 160, 16);
		mailLabel.setBounds(260, 280, 160, 16);
		phoneText.setBounds(10, 300, 200, 20);
		mailText.setBounds(220, 300, 200, 20);
		instructionLabel.setBounds(10, 325, 160, 16);
		intruductionText.setBounds(10, 345, 410, 70);

		yearComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ageComboBox
						.setSelectedIndex((Integer
								.parseInt((String) yearComboBox
										.getSelectedItem())) - 1959);
				animalComboBox.setSelectedIndex(animalIndex((Integer
						.parseInt((String) yearComboBox.getSelectedItem()))));
			}
		});

		ageComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				yearComboBox
						.setSelectedIndex((Integer
								.parseInt((String) ageComboBox
										.getSelectedItem())) - 11);
			}
		});

		monthComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				constellationComboBox.setSelectedIndex(constellationIndex(
						Integer.parseInt((String) monthComboBox
								.getSelectedItem()), Integer
								.parseInt((String) dayComboBox
										.getSelectedItem())));

			}
		});

		dayComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				constellationComboBox.setSelectedIndex(constellationIndex(
						Integer.parseInt((String) monthComboBox
								.getSelectedItem()), Integer
								.parseInt((String) dayComboBox
										.getSelectedItem())));
			}
		});

		// 设置text的长度
		nickNameText.setColumns(15);
		countryText.setColumns(25);

		setVisible(true);
	}

	public void updateUserInfo() {
		Msg infoMsg = new Msg();
		infoMsg.setHead(Option.USER_INFO_REQUEST);
		UserInfoRequestType uirt = new UserInfoRequestType();
		infoMsg.setBody(uirt);
		uirt.setRequestId(g_uid);
		try {
			ObjectOutputStream outToServer = new ObjectOutputStream(IMSocket
					.getInstance().getOutputStream());
			outToServer.writeObject(infoMsg);
			outToServer.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void makesure(MouseEvent evt) {
		
		if (g_uid.equals(Information.getUid())) {
			Information.getMainGui().setImage(imageNO);
			Msg modifyUserInfoMsg = new Msg();
			SubmitUserInfoType suit = new SubmitUserInfoType();
			modifyUserInfoMsg.setHead(Option.SUBMIT_USER_INFO_TYPE);
			modifyUserInfoMsg.setBody(suit);

			suit.setBlood(bloodTypeComboBox.getSelectedIndex());
			suit.setCity(cityText.getText());
			suit.setCountry(countryText.getText());
			suit.setDescription(intruductionText.getText());
			suit.setEmail(mailText.getText());
			suit.setGender(sexComboBox.getSelectedIndex());
			suit.setNickname(nickNameText.getText());
			suit.setPhone(phoneText.getText());
			suit.setPicture(imageNO);
			suit.setProvince(provinceText.getText());
			suit.setScratch(signText.getText());
			suit.setUid(g_uid);
			Date tempDate = new Date(Integer.parseInt((String) yearComboBox
					.getSelectedItem()), Integer.parseInt((String) monthComboBox
					.getSelectedItem())-1, Integer.parseInt((String) dayComboBox
					.getSelectedItem()));
			suit.setBirthday(tempDate);
			

			try {
				ObjectOutputStream outToServer = new ObjectOutputStream(IMSocket
						.getInstance().getOutputStream());
				outToServer.writeObject(modifyUserInfoMsg);
				outToServer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (changeFaceDialog != null) {
				changeFaceDialog.setVisible(false);
			}
			Information.getUserInfo().setScratch(signText.getText());
			Information.getMainGui().signtextfield.setText(signText.getText());
			this.setVisible(false);
		} else {
			JOptionPane.showMessageDialog(null, "只能修改自己的信息", "Info", JOptionPane.INFORMATION_MESSAGE);
		}

		if (changeFaceDialog != null) {
			changeFaceDialog.setVisible(false);
			changeFaceDialog = null;
		}


	}

	public void backGround() {
		((JPanel) this.getContentPane()).setOpaque(false);
		ImageIcon img1 = new ImageIcon(".\\image\\skin\\pi"
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

	private void changeFace(MouseEvent evt) {
		int x = this.getX() + 97;
		int y = this.getY() + 27;

		if (changeFaceDialog != null) {
			changeFaceDialog.setVisible(false);
			changeFaceDialog = null;
		} else {
			changeFaceDialog = new ChangeFace(x, y,this);
			RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0, 0,
					changeFaceDialog.getWidth(), changeFaceDialog.getHeight(), 50, 50);
			WindowUtils.setWindowMask(changeFaceDialog, mask);
			changeFaceDialog.setVisible(true);
		}

//		if (changeFaceDialog.isVisible() == true) {
//			changeFaceDialog.setVisible(false);
//		} else if (changeFaceDialog.isVisible() == false) {
//			changeFaceDialog.setVisible(true);
//		}
	}

	private void mini(MouseEvent evt) {
		this.setExtendedState(JFrame.ICONIFIED);
	}

	private void close(MouseEvent evt) {
		if (changeFaceDialog != null) {
			changeFaceDialog.setVisible(false);
			changeFaceDialog = null;
		}
		this.setVisible(false);
	}

	public void setImage(int num) {
		imageNO = num;
		faceLabel.setIcon(new ImageIcon(".\\image\\face\\" + num
				+ ".jpg"));
	}

	public void showUserInfo(UserInfoResponseType uirt) {
		faceLabel.setIcon(new ImageIcon(".\\image\\face\\"
				+ uirt.getPicture() + ".jpg"));
		nickNameText.setText(uirt.getNickname());
		signText.setText(uirt.getScratch());
		sexComboBox.setSelectedIndex(uirt.getGender());
		yearComboBox.setSelectedIndex(((100 - uirt.getBirthday().getYear())));
		monthComboBox.setSelectedIndex(uirt.getBirthday().getMonth());
		dayComboBox.setSelectedIndex(uirt.getBirthday().getDate() - 1);
		countryText.setText(uirt.getCountry());
		provinceText.setText(uirt.getProvince());
		cityText.setText(uirt.getCity());
		phoneText.setText(uirt.getPhone());
		mailText.setText(uirt.getEmail());
		intruductionText.setText(uirt.getDescription());
		imageNO = uirt.getPicture();
		this.repaint();

	}

	public int animalIndex(int year) {
		int index = 0;
		int start = 1901;
		int x = (start - year) % 12;
		if (x == 1 || x == -11) {
			index = 0;
		}
		if (x == 0) {
			index = 1;
		}
		if (x == 11 || x == -1) {
			index = 2;
		}
		if (x == 10 || x == -2) {
			index = 3;
		}
		if (x == 9 || x == -3) {
			index = 4;
		}
		if (x == 8 || x == -4) {
			index = 5;
		}
		if (x == 7 || x == -5) {
			index = 6;
		}
		if (x == 6 || x == -6) {
			index = 7;
		}
		if (x == 5 || x == -7) {
			index = 8;
		}
		if (x == 4 || x == -8) {
			index = 9;
		}
		if (x == 3 || x == -9) {
			index = 10;
		}
		if (x == 2 || x == -10) {
			index = 11;
		}
		return index;
	}

	public int constellationIndex(int month, int day) {
		int index = 0;
		if (month == 1 && day >= 20 || month == 2 && day <= 18) {
			index = 0;
		}

		if (month == 2 && day >= 19 || month == 3 && day <= 20) {
			index = 1;
		}

		if (month == 3 && day >= 21 || month == 4 && day <= 19) {
			index = 2;
		}

		if (month == 4 && day >= 20 || month == 5 && day <= 20) {
			index = 3;
		}

		if (month == 5 && day >= 21 || month == 6 && day <= 21) {
			index = 4;
		}

		if (month == 6 && day >= 22 || month == 7 && day <= 22) {
			index = 5;
		}

		if (month == 7 && day >= 23 || month == 8 && day <= 22) {
			index = 6;
		}

		if (month == 8 && day >= 23 || month == 9 && day <= 22) {
			index = 7;
		}

		if (month == 9 && day >= 23 || month == 10 && day <= 22) {
			index = 8;
		}

		if (month == 10 && day >= 23 || month == 11 && day <= 21) {
			index = 9;
		}

		if (month == 11 && day >= 22 || month == 12 && day <= 21) {
			index = 10;
		}

		if (month == 12 && day >= 22 || month == 1 && day <= 19) {
			index = 11;
		}
		return index;
	}
}
