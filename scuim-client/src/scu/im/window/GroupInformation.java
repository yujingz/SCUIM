/*************************************************************************************************
 * @author : 彭则荣
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 群信息界面，可供查看信息，或供群主修改信息。
 *                包含了群基本信息及群公告、群论坛链接等。
 * 
 * 
 *************************************************************************************************/
package scu.im.window;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.sun.jna.examples.WindowUtils;

import scu.im.msgtype.GroupInfoRequest;
import scu.im.msgtype.GroupInfoType;
import scu.im.msgtype.Msg;
import scu.im.msgtype.Option;
import scu.im.msgtype.SubmitGroupInfoType;
import scu.im.socket.IMSocket;
import scu.im.utils.Information;

public class GroupInformation extends javax.swing.JFrame {

	private static final long serialVersionUID = -4163249190091998367L;
	private JLabel backgroundlabel;
	private JLabel groupFace;
	private JLabel groupNum;
	private JLabel groupName;
	private JLabel groupNotice;
	private JLabel groupIntroduce;
	private JLabel groupBoss;
	private JTextField groupNumText;
	private JTextField groupNameText;
	private JTextField groupBossText;
	private JTextPane groupNoticePane;
	private JTextPane groupIntroducePane;
	private JButton sureButton;
	private JButton cancelButton;
	private JLabel topLabel;
	private JButton minisizeButton;
	private JButton closeButton;
	private JButton updateButton;

	private JPanel panel1;

	private boolean isDraging;
	private int i, j;
	private String g_gid;
	private JFrame thisFrame;

	public GroupInformation(String gid) {
		g_gid = gid;
		initComponents();

		Information.getGroupInfoHash().put(g_gid, this);
		this.setSize(535, 472);
		updateGroupInfo();
	}

	private void initComponents() {
		backGround();
		this.setLocation(80, 50);// 窗体位置
		this.setUndecorated(true); // 去掉标题栏
		// setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		Container container = getContentPane();
		JPanel jpanel = new JPanel(); // 创建个JPanel
		jpanel.setOpaque(false);// jpanel透明
		jpanel.setLayout(null);// jpanel的布局为空
		container.add(jpanel);
		jpanel.setLayout(null);
		jpanel.setBounds(0, 0, 535, 472);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage(".\\image\\trayImg\\logo.png");
		this.setIconImage(image);
		this.setTitle(" 群资料");
		thisFrame = this;
		RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0, 0, 535,
				472, 45, 45);
		WindowUtils.setWindowMask(thisFrame, mask);
		updateButton = new JButton();
		groupFace = new JLabel();
		groupNum = new JLabel("群号");
		groupName = new JLabel("群名称");
		groupNotice = new JLabel("群公告");
		groupIntroduce = new JLabel("群简介");
		groupBoss = new JLabel("群主账号");
		groupNumText = new JTextField(g_gid);
		groupNumText.setEditable(false);
		groupNameText = new JTextField();
		groupBossText = new JTextField();
		groupNoticePane = new JTextPane();
		groupIntroducePane = new JTextPane();
		sureButton = new JButton();
		cancelButton = new JButton();
		topLabel = new JLabel();
		minisizeButton = new JButton();
		closeButton = new JButton();
		panel1 = new JPanel();

		panel1.setBounds(90, 30, 440, 400);

		panel1.setLayout(null);
		panel1.setOpaque(false);// jpanel透明
		jpanel.add(panel1);
		jpanel.add(groupFace);
		groupFace.setIcon(new ImageIcon(".\\image\\face\\50.jpg"));
		jpanel.add(sureButton);
		jpanel.add(cancelButton);
		jpanel.add(minisizeButton);
		jpanel.add(closeButton);
		jpanel.add(topLabel);
		jpanel.add(updateButton);
		
		panel1.add(groupNum);
		panel1.add(groupName);
		panel1.add(groupIntroduce);
		panel1.add(groupBoss);
		panel1.add(groupNumText);
		panel1.add(groupNameText);
		panel1.add(groupBossText);
		panel1.add(groupIntroducePane);
		panel1.add(groupNotice);
		panel1.add(groupNoticePane);

		topLabel.setBounds(0, 0, 470, 25);
		minisizeButton.setBounds(460, 4, 30, 16);
		minisizeButton.setContentAreaFilled(false); // 按钮透明
		minisizeButton.setBorderPainted(false);
		minisizeButton.setIcon(new ImageIcon(".\\image\\button\\zxh1.png"));
		closeButton.setBounds(490, 4, 30, 16);
		closeButton.setContentAreaFilled(false); // 按钮透明
		closeButton.setBorderPainted(false);
		closeButton.setIcon(new ImageIcon(".\\image\\button\\close1.png"));

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
				dispose();
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
		updateButton.setIcon(new ImageIcon(".\\image\\button\\update1.png"));
		updateButton.setContentAreaFilled(false); // 按钮透明
		updateButton.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent evt) {
				updateGroupInfo();
			}

			public void mouseEntered(MouseEvent evt) {
				updateButton.setIcon(new ImageIcon(
						".\\image\\button\\update2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				updateButton.setIcon(new ImageIcon(
						".\\image\\button\\update1.png"));
			}
		});

		sureButton.setIcon(new ImageIcon(".\\image\\button\\sure1.png"));
		sureButton.setContentAreaFilled(false); // 按钮透明
		sureButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				System.out.println("aaaaaaaaaa"+ groupBossText.getText());
				System.out.println("aaaaaaaaaa"+ Information.getUid());
				if (groupBossText.getText().equals(Information.getUid())) {
					Msg modifyGroupInfoMsg = new Msg();
					SubmitGroupInfoType sgit = new SubmitGroupInfoType();
					modifyGroupInfoMsg.setHead(Option.SUBMIT_GROUP_INFO_TYPE);
					modifyGroupInfoMsg.setBody(sgit);
					sgit.setGid(g_gid);
					sgit.setDescription(groupIntroducePane.getText());
					sgit.setGName(groupNameText.getText());
					sgit.setNotice(groupNoticePane.getText());

					try {
						ObjectOutputStream outToServer = new ObjectOutputStream(
								IMSocket.getInstance().getOutputStream());
						outToServer.writeObject(modifyGroupInfoMsg);
						outToServer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}

					GroupInformation.this.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "只有群主才能修改群信息", "info",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

			public void mouseEntered(MouseEvent evt) {
				sureButton
						.setIcon(new ImageIcon(".\\image\\button\\sure2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				sureButton
						.setIcon(new ImageIcon(".\\image\\button\\sure1.png"));
			}
		});

		cancelButton.setContentAreaFilled(false); // 按钮透明
		cancelButton.setIcon(new ImageIcon(".\\image\\button\\cancel1.png"));
		cancelButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				dispose();
			}

			public void mouseEntered(MouseEvent evt) {
				cancelButton.setIcon(new ImageIcon(
						".\\image\\button\\cancel2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				cancelButton.setIcon(new ImageIcon(
						".\\image\\button\\cancel1.png"));
			}
		});

		groupFace.setBounds(10, 30, 65, 65);
		groupBoss.setBounds(10, 5, 100, 23);
		groupBossText.setBounds(10, 30, 200, 25);
		groupNum.setBounds(10, 60, 100, 23);
		groupNumText.setBounds(10, 85, 200, 25);
		groupName.setBounds(225, 60, 100, 23);
		groupNameText.setBounds(225, 85, 200, 25);
		groupIntroduce.setBounds(10, 115, 100, 23);
		groupIntroducePane.setBounds(10, 140, 410, 100);
		groupIntroducePane.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		groupNotice.setBounds(10, 250, 100, 23);
		groupNoticePane.setBounds(10, 275, 410, 100);
		groupNoticePane.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		sureButton.setBounds(345, 445, 74, 22);
		cancelButton.setBounds(445, 445, 74, 22);
		updateButton.setBounds(245, 445, 74, 22);

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
				i = e.getX();
				j = e.getY();
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
					setLocation(left + e.getX() - i, top + e.getY() - j);
				}
			}
		});

		pack();
		setVisible(true);
	}

	public static void main(String args[]) {
		new GroupInformation("62301");
	}

	public void backGround() {
		((JPanel) this.getContentPane()).setOpaque(false);
		ImageIcon img1 = new ImageIcon(".\\image\\skin\\gSet"+Information.getThemeNo()+".png");
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

	private void mini(MouseEvent evt) {
		this.setExtendedState(JFrame.ICONIFIED);
	}

	public void showGroupInfo(GroupInfoType git) {
		groupBossText.setText(git.getCreatorUid());
		groupIntroducePane.setText(git.getDescription());
		groupNameText.setText(git.getGname());
		groupNoticePane.setText(git.getNotice());
	}

	private void updateGroupInfo() {
		Msg groupInfoMsg = new Msg();
		GroupInfoRequest girt = new GroupInfoRequest();
		groupInfoMsg.setBody(girt);
		groupInfoMsg.setHead(Option.GROUP_INFO_REQUEST);
		System.out.println(g_gid);
		girt.setGid(g_gid);
		girt.setUid(Information.getUid());
		ObjectOutputStream outToServer;
		try {
			outToServer = new ObjectOutputStream(IMSocket.getInstance()
					.getOutputStream());
			outToServer.writeObject(groupInfoMsg);
			outToServer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
