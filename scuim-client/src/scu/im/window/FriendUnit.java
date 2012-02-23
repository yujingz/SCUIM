/*************************************************************************************************
 * @author : 彭则荣 
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 每一个好友都是一个对象，这段代码就是创建这个对象的框架的，布置其所需对应接口及功能。
 * 
 * 
 *************************************************************************************************/
package scu.im.window;

import java.awt.Color;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import scu.im.msgtype.FriendUnitType;
import scu.im.msgtype.MessageWithAttrib;
import scu.im.utils.Information;
import scu.im.utils.MessageContainer;

import com.sun.jna.examples.WindowUtils;

public class FriendUnit extends Panel implements Serializable {

	private static final long serialVersionUID = 3031893090082143002L;
	public FriendUnitType friendinfo = null;
	public JLabel faceLabel;
	private JLabel nameLabel;
	private JLabel signLabel;
	public JCheckBox roarCheckbox;

	public FriendUnit(FriendUnitType fut) {
		this.friendinfo = fut;
		faceLabel = new JLabel();// 头像
		nameLabel = new JLabel(fut.getNickname());// 名字
		signLabel = new JLabel(fut.getSignature());
		roarCheckbox = new JCheckBox();
		roarCheckbox.setContentAreaFilled(false);
		System.out.println(fut.getNickname() + " 的状态是" + fut.getState());
		Image image = null;
		if (fut.getState().equals("1")) {
			image = new ImageIcon(".\\image\\face\\" + fut.getImage() + ".jpg")
					.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
			System.out.println("初始化头像" + fut.getNickname() + "在线");
		} else {
			image = new ImageIcon(".\\image\\whiteface\\" + fut.getImage()
					+ ".jpg").getImage().getScaledInstance(50, 50,
					Image.SCALE_DEFAULT);
			System.out.println("初始化头像" + fut.getNickname() + "不在线");
		}
		ImageIcon imageIcon = new ImageIcon(image);
		faceLabel.setIcon(imageIcon);

		setBounds(0, 0, 200, 55);
		setBackground(Color.LIGHT_GRAY);
		// setLayout(new GridLayout(1, 5, 1, 1));
		setLayout(null);
		setVisible(true);
		add(faceLabel);
		add(nameLabel);
		add(signLabel);
		add(roarCheckbox);
		faceLabel.setBounds(2, 5, 50, 50);
		nameLabel.setBounds(65, 3, 60, 20);
		signLabel.setBounds(65, 29, 60, 20);
		roarCheckbox.setBounds(192, 15, 20, 20);
		nameLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				new PersonalInformation(friendinfo.getUid());
				// RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0,
				// 0,
				// pi.getWidth(), pi.getHeight(), 45, 45);
				// WindowUtils.setWindowMask(pi, mask);

			}
		});
		this.addMouseListener(new MyMouseListener());

		Information.getFriendUnitHash().put(friendinfo.getUid(), this);
		System.out.println("Unit" + friendinfo.getUid() + "已加入哈希表");
	}

	class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent arg0) {

			if (!Information.getOpenedWindow().containsKey(friendinfo.getUid())) {

				if (Information.getShakingUnit().containsKey(
						friendinfo.getUid())) {
					System.out.println("在抖");
					Information.getShakingUnit().get(friendinfo.getUid())
							.stopShake();
					// 停止抖动
					ArrayList<MessageWithAttrib> msgAl = MessageContainer
							.getMessageList();
					System.out.println(msgAl.size());
					for (int i = 0; i < msgAl.size(); i++) {
						System.out.println("循环中当前： " + i);
						// 遍历消息array，将所有消息显示
						if (Information.getOpenedWindow().containsKey(
								friendinfo.getUid())) {
							Information.getOpenedWindow().get(
									friendinfo.getUid())
									.displayReceivedMessage(msgAl.get(i));

						} else {

							TalkToOneWithStyle talk = new TalkToOneWithStyle(
									friendinfo);

							talk.displayReceivedMessage(msgAl.get(i));

						}
					}
				} else {
					System.out.println("不在抖");

					new TalkToOneWithStyle(friendinfo);
					// RoundRectangle2D.Float mask = new
					// RoundRectangle2D.Float(0,
					// 0, talk.getWidth(), talk.getHeight(), 45, 45);
					// WindowUtils.setWindowMask(talk, mask);
				}

				Iterator<MessageWithAttrib> it = MessageContainer
						.getMessageList().iterator();
				while (it.hasNext()) {
					if (it.next().getSenderUid().equals(friendinfo.getUid())) {

						new TalkToOneWithStyle(friendinfo);
						// RoundRectangle2D.Float mask = new
						// RoundRectangle2D.Float(0, 0,
						// talk.getWidth(), talk.getHeight(), 45, 45);
						// WindowUtils.setWindowMask(talk, mask);
					}

					Iterator<MessageWithAttrib> iter = MessageContainer
							.getMessageList().iterator();
					while (iter.hasNext()) {
						if (iter.next().getSenderUid().equals(
								friendinfo.getUid())) {

							iter.remove();

						}
					}

					System.out
							.println(MessageContainer.getMessageList().size());
					if (MessageContainer.getMessageList().isEmpty()) {
						Information.getMainGui().stopBlink();
						// 关闭blink
					}
				}
			}
		}

	}

	public void offLine() {
		Image image = new ImageIcon(".\\image\\whiteface\\"
				+ friendinfo.getImage() + ".jpg").getImage().getScaledInstance(
				50, 50, Image.SCALE_DEFAULT);
		ImageIcon imageIcon = new ImageIcon(image);
		faceLabel.setIcon(imageIcon);

	}
}
