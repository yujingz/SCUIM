/*************************************************************************************************
 * @author : 彭则荣
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 每一个群都是一个对象，这段代码就是创建这个对象的框架的，布置其所需对应接口及功能。
 * 
 * 
 *************************************************************************************************/
package scu.im.window;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scu.im.msgtype.GroupUnitType;
import scu.im.utils.Information;

import com.sun.jna.examples.WindowUtils;

public class GroupUnit extends JPanel {
	// 由于主界面显示你有几个群的图标
	private static final long serialVersionUID = 7679124192857640004L;
	private GroupUnitType gut = null;

	public GroupUnit(GroupUnitType gut) {

		this.gut = gut;
		final String gid = gut.getGid();
		Image image = new ImageIcon(".\\image\\face\\1.jpg").getImage()
				.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		ImageIcon imageIcon = new ImageIcon(image);
		JLabel imageLabel = new JLabel();
		JLabel groupNameLabel = new JLabel(gut.getGName());
		imageLabel.setIcon(imageIcon);
		setBackground(Color.LIGHT_GRAY);
		setBounds(0, 0, 200, 55);
		setLayout(null);
		setVisible(true);
		add(imageLabel);
		add(groupNameLabel);
		imageLabel.setBounds(2, 5, 50, 50);
		groupNameLabel.setBounds(60, 5, 80, 50);
		groupNameLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new GroupInformation(gid);
			}
		});
		addMouseListener(new groupMouseListener());
		/* this.addMouseListener(l) */
	}

	class groupMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (!Information.getOpenedGroupWindow().containsKey(gut.getGid())) {
				new TalkToMany(gut);
				// RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0,
				// 0,
				// talk.getWidth(), talk.getHeight(), 45, 45);
				// WindowUtils.setWindowMask(talk, mask);
			}
		}

	}
}
