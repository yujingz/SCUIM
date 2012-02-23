/*************************************************************************************************
 * @author : 彭则荣
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 开始菜单，实现系统设置，信息修改，帮助，退出，进入主页五大功能。
 *                此菜单按想象应该是附在主界面里的，之所以要另外写出来，是因为这样真的更无耻
 *                更方便使用，还不容易出错。
 * 
 *************************************************************************************************/

package scu.im.window;


import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.sun.jna.examples.WindowUtils;

import scu.im.utils.Information;

public class StartList extends JDialog {

	private static final long serialVersionUID = 1L;

	public StartList(int x, int y) {
		initComponents(x, y);
		this.setSize(new Dimension(135, 163));
	}

	private void initComponents(int x, int y) {
		this.setUndecorated(true);
		this.setLocation(x, y);
		backGround();// 调用背景设置方法
      
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		servercenterbutton = new JButton();
		updatedatabutton = new JButton();
		setbutton = new JButton();
		helpbutton = new JButton();
		exitbutton = new JButton();

		this.setLayout(null);
		this.add(servercenterbutton);
		this.add(updatedatabutton);
		this.add(setbutton);
		this.add(helpbutton);
		this.add(exitbutton);

		servercenterbutton.setBounds(0, 0, 130, 23);
		servercenterbutton.setContentAreaFilled(false); // 按钮透明
		servercenterbutton.setBorderPainted(false);
		servercenterbutton.setIcon(new ImageIcon(
				".\\image\\startlist\\center1.png"));
		updatedatabutton.setBounds(0, 23, 130, 23);
		updatedatabutton.setContentAreaFilled(false); // 按钮透明
		updatedatabutton.setBorderPainted(false);
		updatedatabutton.setIcon(new ImageIcon(
				".\\image\\startlist\\data1.png"));
		setbutton.setBounds(0, 46, 130, 23);
		setbutton.setContentAreaFilled(false); // 按钮透明
		setbutton.setBorderPainted(false);
		setbutton.setIcon(new ImageIcon(
				".\\image\\startlist\\set1.png"));
		helpbutton.setBounds(0, 69, 130, 23);
		helpbutton.setContentAreaFilled(false); // 按钮透明
		helpbutton.setBorderPainted(false);
		helpbutton.setIcon(new ImageIcon(
				".\\image\\startlist\\help1.png"));
		exitbutton.setBounds(0, 92, 130, 23);
		exitbutton.setContentAreaFilled(false); // 按钮透明
		exitbutton.setBorderPainted(false);
		exitbutton.setIcon(new ImageIcon(
				".\\image\\startlist\\exit1.png"));

		servercenterbutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				System.out.println("go to web");
			}

			public void mouseEntered(MouseEvent evt) {
				servercenterbutton.setIcon(new ImageIcon(
						".\\image\\startlist\\center2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				servercenterbutton.setIcon(new ImageIcon(
						".\\image\\startlist\\center1.png"));
			}
		});

		updatedatabutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				
			}

			public void mouseEntered(MouseEvent evt) {
				updatedatabutton.setIcon(new ImageIcon(
						".\\image\\startlist\\data2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				updatedatabutton.setIcon(new ImageIcon(
						".\\image\\startlist\\data1.png"));
			}
		});

		setbutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				System.out.println("set about system");
			}

			public void mouseEntered(MouseEvent evt) {
				setbutton.setIcon(new ImageIcon(
						".\\image\\startlist\\set2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				setbutton.setIcon(new ImageIcon(
						".\\image\\startlist\\set1.png"));
			}
		});

		helpbutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				System.out.println("something wrong? exit System");
			}

			public void mouseEntered(MouseEvent evt) {
				helpbutton.setIcon(new ImageIcon(
						".\\image\\startlist\\help2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				helpbutton.setIcon(new ImageIcon(
						".\\image\\startlist\\help1.png"));
			}
		});

		exitbutton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				System.exit(0);
			}

			public void mouseEntered(MouseEvent evt) {
				exitbutton.setIcon(new ImageIcon(
						".\\image\\startlist\\exit2.png"));
			}

			public void mouseExited(MouseEvent evt) {
				exitbutton.setIcon(new ImageIcon(
						".\\image\\startlist\\exit1.png"));
			}
		});

		pack();
		setVisible(true);
	}

	// 背景设置
	public void backGround() {
		((JPanel) this.getContentPane()).setOpaque(false);// 取一个透明panel--this
		ImageIcon backImage = new ImageIcon(".\\image\\startlist\\"+Information.getThemeNo()+".png");

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
		
		  WindowUtils.setWindowAlpha(this, 0.01F);   
	        WindowUtils.setWindowMask(this,backImage);   //图片的不规则部分完全透明

	}

//	public static void main(String args[]) {
//
//		StartList S1 = new StartList(0, 0);
//		S1.setVisible(true);
//	}

	JButton servercenterbutton;
	JButton updatedatabutton;
	JButton setbutton;
	JButton helpbutton;
	JButton exitbutton;
	JLabel backgroundLabel;

	// End of variables declaration//GEN-END:variables

}