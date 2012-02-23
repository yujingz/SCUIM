/*************************************************************************************************
 * @author : 彭则荣
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 对应语音聊天的控制窗口，实现提示，开启，显示进度，中断停止语音聊天的功能
 * 
 * 
 *************************************************************************************************/
package scu.im.window;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import scu.im.msgtype.Msg;
import scu.im.msgtype.Option;
import scu.im.utils.Information;

public class Function extends JDialog {

	private static final long serialVersionUID = 1L;
	private JLabel backgroundlabel;
	private JButton functionButton1;
	private JButton functionButton2;
	private JButton closeButton;
	private byte[] sendBuf = null;
	private InetAddress g_inet = null;

	// public static void main(String args[]) {
	//
	// java.awt.EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// new Function().setVisible(true);
	// }
	// });
	// }

	public Function(String uid, InetAddress inet) {
		g_inet = inet;
		initComponents();
		this.setSize(new Dimension(210, 130));
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initComponents() {
		backGround();
		this.setUndecorated(true);
		this.setLayout(null);
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenwidth = screenSize.width;
		int screenheight = screenSize.height;
		int x = screenwidth - 215;
		int y = screenheight - 160;
		setLocation(x, y);
		functionButton1 = new JButton();
		functionButton2 = new JButton();
		closeButton = new JButton();

		this.add(functionButton1);
		this.add(functionButton2);
		this.add(closeButton);

		functionButton1.setBounds(10, 105, 90, 25);
		functionButton2.setBounds(110, 105, 90, 25);
		closeButton.setBounds(170, 2, 40, 20);

		closeButton.setContentAreaFilled(false); // 按钮透明
		closeButton.setBorderPainted(false);
		closeButton.setIcon(new ImageIcon(".\\image\\button\\close1.png"));
		closeButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				dispose();
			}

			public void mouseEntered(MouseEvent evt) {
				closeButton.setIcon(new ImageIcon(
						".\\image\\button\\close3.png"));
			}

			public void mouseExited(MouseEvent evt) {
				closeButton.setIcon(new javax.swing.ImageIcon(
						".\\image\\button\\close1.png"));
			}
		});

		functionButton1.setContentAreaFilled(false); // 按钮透明
		functionButton1.setBorderPainted(false);
		functionButton1.setIcon(new ImageIcon(".\\image\\button\\stop1.png"));
		functionButton1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				try {
					if ((Information.getVoiceResponseClientSocket() != null)
							&& (!Information.getVoiceResponseClientSocket()
									.isClosed())) {
						Information.getVoiceResponseClientSocket().close();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				dispose();
			}

			public void mouseEntered(MouseEvent evt) {
				functionButton1.setIcon(new ImageIcon(
						".\\image\\button\\stop2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				functionButton1.setIcon(new javax.swing.ImageIcon(
						".\\image\\button\\stop1.png"));
			}
		});

		functionButton2.setContentAreaFilled(false); // 按钮透明
		functionButton2.setBorderPainted(false);
		functionButton2.setIcon(new ImageIcon(".\\image\\button\\shut1.png"));
		functionButton2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				try {
					DatagramSocket sender = new DatagramSocket();
					DatagramPacket sendPacket = null;
					Msg stopVoiceMsg = new Msg();
					String stopVoiceStr = Information.getUid();
					stopVoiceMsg.setHead(Option.STOP_VOICE_TYPE);
					stopVoiceMsg.setBody(stopVoiceStr);
					sendPacket = toDatagram(stopVoiceMsg, g_inet, Information
							.getUdpPort());
					sender.send(sendPacket);
				} catch (SocketException e1) {
					e1.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				try {
					if ((Information.getVoiceResponseClientSocket() != null)
							&& (!Information.getVoiceResponseClientSocket()
									.isClosed())) {
						Information.getVoiceResponseClientSocket().close();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				dispose();
			}

			public void mouseEntered(MouseEvent evt) {
				functionButton2.setIcon(new ImageIcon(
						".\\image\\button\\shut2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				functionButton2.setIcon(new javax.swing.ImageIcon(
						".\\image\\button\\shut1.png"));
			}
		});

	}

	public void backGround() {
		((JPanel) this.getContentPane()).setOpaque(false);
		ImageIcon img1 = new ImageIcon(".\\image\\skin\\v"
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
}