/*************************************************************************************************
 * @author : 彭则荣
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 主要实现用户更换头像的功能，并同步上传修改数据，同步更新。
 * 				     相当牛辄的头像分类，制作精良的头像选择界面
 * 				     及符合现代化建设主题的设计风格，让人流血五步，天下缟素。
 * 
 *************************************************************************************************/

package scu.im.window;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import scu.im.utils.Information;

public class ChangeFace extends JDialog {

	private static final long serialVersionUID = 1L;
	private JLabel backgroundlabel;
	JLabel[] faceLabel = new JLabel[80];
	JLabel faceKind1;
	JLabel faceKind2;
	JLabel faceKind3;
	JLabel faceKind4;
	JLabel faceKind5;
	JLabel faceKind6;
	JLabel faceKind7;
	JLabel faceKind8;
	JButton closeButton;
	JButton kind1;
	JButton kind2;
	JButton kind3;
	JButton kind4;
	JButton kind5;
	JButton kind6;
	JButton kind7;
	JButton kind8;

	PersonalInformation personalInformation = null;

	// public static void main(String args[]) {
	//
	// java.awt.EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// new ChangeFace(1, 1).setVisible(true);
	// }
	// });
	// }

	public ChangeFace(int x, int y, PersonalInformation per) {
		personalInformation = per;
		initComponents(x, y);
		this.setSize(new Dimension(305, 290));
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initComponents(int x, int y) {
		backGround();
		this.setUndecorated(true);
		this.setLocation(x, y);
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Container container1 = getContentPane();
		JPanel panel = new JPanel();
		container1.add(panel);
		panel.setLayout(null);
		panel.setOpaque(false);
		panel.setBounds(0, 0, 305, 290);

		for (int i = 0; i < 80; i++) {
			ImageIcon image = new ImageIcon(".\\image\\face\\" + i + ".jpg");
			image.setImage(image.getImage().getScaledInstance(45, 45, 10));
			faceLabel[i] = new JLabel(image);
		}

		kind1 = new JButton();
		kind2 = new JButton();
		kind3 = new JButton();
		kind4 = new JButton();
		kind5 = new JButton();
		kind6 = new JButton();
		kind7 = new JButton();
		kind8 = new JButton();
		panel.add(kind1);
		panel.add(kind2);
		panel.add(kind3);
		panel.add(kind4);
		panel.add(kind5);
		panel.add(kind6);
		panel.add(kind7);
		panel.add(kind8);
		kind1.setBounds(10, 190, 70, 25);
		kind2.setBounds(80, 190, 70, 25);
		kind3.setBounds(150, 190, 70, 25);
		kind4.setBounds(220, 190, 70, 25);
		kind5.setBounds(10, 220, 70, 25);
		kind6.setBounds(80, 220, 70, 25);
		kind7.setBounds(150, 220, 70, 25);
		kind8.setBounds(220, 220, 70, 25);
		faceKind1 = new JLabel();
		faceKind2 = new JLabel();
		faceKind3 = new JLabel();
		faceKind4 = new JLabel();
		faceKind5 = new JLabel();
		faceKind6 = new JLabel();
		faceKind7 = new JLabel();
		faceKind8 = new JLabel();
		faceKind1.setBounds(15, 5, 290, 190);
		faceKind2.setBounds(15, 5, 290, 190);
		faceKind3.setBounds(15, 5, 290, 190);
		faceKind4.setBounds(15, 5, 290, 190);
		faceKind5.setBounds(15, 5, 290, 190);
		faceKind6.setBounds(15, 5, 290, 190);
		faceKind7.setBounds(15, 5, 290, 190);
		faceKind8.setBounds(15, 5, 290, 190);
		faceKind1.setVisible(true);
		faceKind2.setVisible(false);
		faceKind3.setVisible(false);
		faceKind4.setVisible(false);
		faceKind5.setVisible(false);
		faceKind6.setVisible(false);
		faceKind7.setVisible(false);
		faceKind8.setVisible(false);

		panel.add(faceKind1);
		panel.add(faceKind2);
		panel.add(faceKind3);
		panel.add(faceKind4);
		panel.add(faceKind5);
		panel.add(faceKind6);
		panel.add(faceKind7);
		panel.add(faceKind8);

		closeButton = new JButton();
		panel.add(closeButton);
		closeButton.setBounds(260, 3, 40, 25);
		closeButton.setContentAreaFilled(false); // 按钮透明
		closeButton.setBorderPainted(false);
		closeButton.setIcon(new javax.swing.ImageIcon(
				".\\image\\button\\close1.png"));

		for (int i = 0; i < 5; i++) {
			faceKind1.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + i * 50), 40, 45, 45);
		}
		for (int i = 5; i < 10; i++) {
			faceKind1.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 5) * 50), 90, 45, 45);
		}

		for (int i = 10; i < 15; i++) {
			faceKind2.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 10) * 50), 45, 45, 45);
		}
		for (int i = 15; i < 20; i++) {
			faceKind2.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 15) * 50), 90, 45, 45);
		}

		for (int i = 20; i < 25; i++) {
			faceKind3.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 20) * 50), 45, 45, 45);
		}
		for (int i = 25; i < 30; i++) {
			faceKind3.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 25) * 50), 90, 45, 45);
		}

		for (int i = 30; i < 35; i++) {
			faceKind4.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 30) * 50), 45, 45, 45);
		}
		for (int i = 35; i < 40; i++) {
			faceKind4.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 35) * 50), 90, 45, 45);
		}

		for (int i = 40; i < 45; i++) {
			faceKind5.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 40) * 50), 45, 45, 45);
		}
		for (int i = 45; i < 50; i++) {
			faceKind5.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 45) * 50), 90, 45, 45);
		}

		for (int i = 50; i < 55; i++) {
			faceKind6.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 50) * 50), 45, 45, 45);
		}
		for (int i = 55; i < 60; i++) {
			faceKind6.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 55) * 50), 90, 45, 45);
		}

		for (int i = 60; i < 65; i++) {
			faceKind7.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 60) * 50), 45, 45, 45);
		}
		for (int i = 65; i < 70; i++) {
			faceKind7.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 65) * 50), 90, 45, 45);
		}

		for (int i = 70; i < 75; i++) {
			faceKind8.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 70) * 50), 45, 45, 45);
		}
		for (int i = 75; i < 80; i++) {
			faceKind8.add(faceLabel[i]);
			faceLabel[i].setBounds((10 + (i - 75) * 50), 90, 45, 45);
		}

		kind1.setContentAreaFilled(false); // 按钮透明
		kind1.setIcon(new ImageIcon(".\\image\\button\\k12.png"));
		kind1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				faceKind1.setVisible(true);
				faceKind2.setVisible(false);
				faceKind3.setVisible(false);
				faceKind4.setVisible(false);
				faceKind5.setVisible(false);
				faceKind6.setVisible(false);
				faceKind7.setVisible(false);
				faceKind8.setVisible(false);
				kind1.setIcon(new ImageIcon(".\\image\\button\\k12.png"));
				kind2.setIcon(new ImageIcon(".\\image\\button\\k21.png"));
				kind3.setIcon(new ImageIcon(".\\image\\button\\k31.png"));
				kind4.setIcon(new ImageIcon(".\\image\\button\\k41.png"));
				kind5.setIcon(new ImageIcon(".\\image\\button\\k51.png"));
				kind6.setIcon(new ImageIcon(".\\image\\button\\k61.png"));
				kind7.setIcon(new ImageIcon(".\\image\\button\\k71.png"));
				kind8.setIcon(new ImageIcon(".\\image\\button\\k81.png"));
			}

		});

		kind2.setContentAreaFilled(false); // 按钮透明
		kind2.setIcon(new ImageIcon(".\\image\\button\\k21.png"));
		kind2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				faceKind2.setVisible(true);
				faceKind1.setVisible(false);
				faceKind3.setVisible(false);
				faceKind4.setVisible(false);
				faceKind5.setVisible(false);
				faceKind6.setVisible(false);
				faceKind7.setVisible(false);
				faceKind8.setVisible(false);

				kind1.setIcon(new ImageIcon(".\\image\\button\\k11.png"));
				kind2.setIcon(new ImageIcon(".\\image\\button\\k22.png"));
				kind3.setIcon(new ImageIcon(".\\image\\button\\k31.png"));
				kind4.setIcon(new ImageIcon(".\\image\\button\\k41.png"));
				kind5.setIcon(new ImageIcon(".\\image\\button\\k51.png"));
				kind6.setIcon(new ImageIcon(".\\image\\button\\k61.png"));
				kind7.setIcon(new ImageIcon(".\\image\\button\\k71.png"));
				kind8.setIcon(new ImageIcon(".\\image\\button\\k81.png"));
			}

		});

		kind3.setContentAreaFilled(false); // 按钮透明
		kind3.setIcon(new ImageIcon(".\\image\\button\\k31.png"));
		kind3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				faceKind3.setVisible(true);
				faceKind2.setVisible(false);
				faceKind1.setVisible(false);
				faceKind4.setVisible(false);
				faceKind5.setVisible(false);
				faceKind6.setVisible(false);
				faceKind7.setVisible(false);
				faceKind8.setVisible(false);

				kind1.setIcon(new ImageIcon(".\\image\\button\\k11.png"));
				kind2.setIcon(new ImageIcon(".\\image\\button\\k21.png"));
				kind3.setIcon(new ImageIcon(".\\image\\button\\k32.png"));
				kind4.setIcon(new ImageIcon(".\\image\\button\\k41.png"));
				kind5.setIcon(new ImageIcon(".\\image\\button\\k51.png"));
				kind6.setIcon(new ImageIcon(".\\image\\button\\k61.png"));
				kind7.setIcon(new ImageIcon(".\\image\\button\\k71.png"));
				kind8.setIcon(new ImageIcon(".\\image\\button\\k81.png"));
			}

		});

		kind4.setContentAreaFilled(false); // 按钮透明
		kind4.setIcon(new ImageIcon(".\\image\\button\\k41.png"));
		kind4.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				faceKind4.setVisible(true);
				faceKind2.setVisible(false);
				faceKind3.setVisible(false);
				faceKind1.setVisible(false);
				faceKind5.setVisible(false);
				faceKind6.setVisible(false);
				faceKind7.setVisible(false);
				faceKind8.setVisible(false);

				kind1.setIcon(new ImageIcon(".\\image\\button\\k11.png"));
				kind2.setIcon(new ImageIcon(".\\image\\button\\k21.png"));
				kind3.setIcon(new ImageIcon(".\\image\\button\\k31.png"));
				kind4.setIcon(new ImageIcon(".\\image\\button\\k42.png"));
				kind5.setIcon(new ImageIcon(".\\image\\button\\k51.png"));
				kind6.setIcon(new ImageIcon(".\\image\\button\\k61.png"));
				kind7.setIcon(new ImageIcon(".\\image\\button\\k71.png"));
				kind8.setIcon(new ImageIcon(".\\image\\button\\k81.png"));
			}

		});

		kind5.setContentAreaFilled(false); // 按钮透明
		kind5.setIcon(new ImageIcon(".\\image\\button\\k51.png"));
		kind5.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				faceKind5.setVisible(true);
				faceKind2.setVisible(false);
				faceKind3.setVisible(false);
				faceKind4.setVisible(false);
				faceKind1.setVisible(false);
				faceKind6.setVisible(false);
				faceKind7.setVisible(false);
				faceKind8.setVisible(false);

				kind1.setIcon(new ImageIcon(".\\image\\button\\k11.png"));
				kind2.setIcon(new ImageIcon(".\\image\\button\\k21.png"));
				kind3.setIcon(new ImageIcon(".\\image\\button\\k31.png"));
				kind4.setIcon(new ImageIcon(".\\image\\button\\k41.png"));
				kind5.setIcon(new ImageIcon(".\\image\\button\\k52.png"));
				kind6.setIcon(new ImageIcon(".\\image\\button\\k61.png"));
				kind7.setIcon(new ImageIcon(".\\image\\button\\k71.png"));
				kind8.setIcon(new ImageIcon(".\\image\\button\\k81.png"));
			}

		});

		kind6.setContentAreaFilled(false); // 按钮透明
		kind6.setIcon(new ImageIcon(".\\image\\button\\k61.png"));
		kind6.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				faceKind6.setVisible(true);
				faceKind2.setVisible(false);
				faceKind3.setVisible(false);
				faceKind4.setVisible(false);
				faceKind5.setVisible(false);
				faceKind1.setVisible(false);
				faceKind7.setVisible(false);
				faceKind8.setVisible(false);

				kind1.setIcon(new ImageIcon(".\\image\\button\\k11.png"));
				kind2.setIcon(new ImageIcon(".\\image\\button\\k21.png"));
				kind3.setIcon(new ImageIcon(".\\image\\button\\k31.png"));
				kind4.setIcon(new ImageIcon(".\\image\\button\\k41.png"));
				kind5.setIcon(new ImageIcon(".\\image\\button\\k51.png"));
				kind6.setIcon(new ImageIcon(".\\image\\button\\k62.png"));
				kind7.setIcon(new ImageIcon(".\\image\\button\\k71.png"));
				kind8.setIcon(new ImageIcon(".\\image\\button\\k81.png"));
			}

		});

		kind7.setContentAreaFilled(false); // 按钮透明
		kind7.setIcon(new ImageIcon(".\\image\\button\\k71.png"));
		kind7.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				faceKind7.setVisible(true);
				faceKind2.setVisible(false);
				faceKind3.setVisible(false);
				faceKind4.setVisible(false);
				faceKind5.setVisible(false);
				faceKind6.setVisible(false);
				faceKind1.setVisible(false);
				faceKind8.setVisible(false);

				kind1.setIcon(new ImageIcon(".\\image\\button\\k11.png"));
				kind2.setIcon(new ImageIcon(".\\image\\button\\k21.png"));
				kind3.setIcon(new ImageIcon(".\\image\\button\\k31.png"));
				kind4.setIcon(new ImageIcon(".\\image\\button\\k41.png"));
				kind5.setIcon(new ImageIcon(".\\image\\button\\k51.png"));
				kind6.setIcon(new ImageIcon(".\\image\\button\\k61.png"));
				kind7.setIcon(new ImageIcon(".\\image\\button\\k72.png"));
				kind8.setIcon(new ImageIcon(".\\image\\button\\k81.png"));
			}

		});

		kind8.setContentAreaFilled(false); // 按钮透明
		kind8.setIcon(new ImageIcon(".\\image\\button\\k81.png"));
		kind8.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				faceKind8.setVisible(true);
				faceKind2.setVisible(false);
				faceKind3.setVisible(false);
				faceKind4.setVisible(false);
				faceKind5.setVisible(false);
				faceKind6.setVisible(false);
				faceKind7.setVisible(false);
				faceKind1.setVisible(false);

				kind1.setIcon(new ImageIcon(".\\image\\button\\k11.png"));
				kind2.setIcon(new ImageIcon(".\\image\\button\\k21.png"));
				kind3.setIcon(new ImageIcon(".\\image\\button\\k31.png"));
				kind4.setIcon(new ImageIcon(".\\image\\button\\k41.png"));
				kind5.setIcon(new ImageIcon(".\\image\\button\\k51.png"));
				kind6.setIcon(new ImageIcon(".\\image\\button\\k61.png"));
				kind7.setIcon(new ImageIcon(".\\image\\button\\k71.png"));
				kind8.setIcon(new ImageIcon(".\\image\\button\\k82.png"));
			}

		});

		for (int i = 0; i < 80; i++) {
			faceLabel[i].addMouseListener(new MyMouseListener());
		}

		closeButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				setVisible(false);
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

	}

	class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			for (int i = 0; i < 80; i++) {
				if (faceLabel[i] == e.getSource()) {
					personalInformation.setImage(i);
					setVisible(false);
				}
			}

		}
	}

	public void backGround() {
		((JPanel) this.getContentPane()).setOpaque(false);
		ImageIcon img1 = new ImageIcon(".\\image\\skin\\cs"
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
