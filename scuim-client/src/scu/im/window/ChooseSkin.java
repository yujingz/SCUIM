/*************************************************************************************************
 * @author : 彭则荣
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 实现选择、更换皮肤的功能。
 *                有限的人力，紧张的时间，饱含曲折皮肤制作过程，肴绕着何琪同学指尖的温热及内心深处的
 *                母性光辉。PS：皮肤是一套一套的，更换时所有窗口同步更换（在这要感谢Information.java
 *                这个文件的开写者郑宇靖同学，给彭则荣先生对于GUI功能更好的实现提供了方便）
 * 
 * 
 *************************************************************************************************/
package scu.im.window;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import scu.im.utils.Information;

public class ChooseSkin extends JDialog {

	private static final long serialVersionUID = 1L;

	public ChooseSkin(int x, int y, MyScuimFrame mSf) {
		myscuimframe = mSf;
		initComponents(x, y);
		this.setSize(new Dimension(135, 161));
	}

	private void initComponents(int x, int y) {
		this.setUndecorated(true);
		this.setLocation(x, y);
		backGround();
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Container container1 = getContentPane();
		JPanel jpanel = new JPanel(); // 创建个JPanel
		jpanel.setOpaque(false);// jpanel透明
		jpanel.setLayout(null);// jpanel的布局为空
		container1.add(jpanel);
		jpanel.setLayout(null);
		jpanel.setOpaque(false);
		jpanel.setBounds(0, 0, 136, 161);
		jpanel.setBackground(Color.white);
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		label5 = new JLabel();
		label6 = new JLabel();
		label7 = new JLabel();
		label8 = new JLabel();
		label9 = new JLabel();
		label10 = new JLabel();
		t1Label = new JLabel("蝴蝶系列");
		t2Label = new JLabel("纯色系列");
		t3Label = new JLabel("炫彩系列");
		jpanel.add(label1);
		jpanel.add(label2);
		jpanel.add(label3);
		jpanel.add(label4);
		jpanel.add(label5);
		jpanel.add(label6);
		jpanel.add(label7);
		jpanel.add(label8);
		jpanel.add(label9);
		jpanel.add(label10);
		jpanel.add(t1Label);
		jpanel.add(t2Label);
		jpanel.add(t3Label);

		t1Label.setBounds(3, 3, 60, 20);
		label1.setBounds(3, 25, 30, 27);
		label2.setBounds(35, 25, 30, 27);

		t2Label.setBounds(3, 55, 60, 20);
		label3.setBounds(3, 77, 30, 27);
		label4.setBounds(35, 77, 30, 27);
		label5.setBounds(67, 77, 30, 27);
		label6.setBounds(99, 77, 30, 27);

		t3Label.setBounds(3, 107, 60, 20);
		label7.setBounds(3, 129, 30, 27);
		label8.setBounds(35, 129, 30, 27);
		label9.setBounds(67, 129, 30, 27);
		label10.setBounds(99, 129, 30, 27);

//		jpanel.setBorder(javax.swing.BorderFactory
//				.createLineBorder(new java.awt.Color(0, 0, 0)));

		label1.setIcon(new ImageIcon(".\\image\\skin\\c9.jpg"));
		label2.setIcon(new ImageIcon(".\\image\\skin\\c10.jpg"));
		label3.setIcon(new ImageIcon(".\\image\\skin\\c5.jpg"));
		label4.setIcon(new ImageIcon(".\\image\\skin\\c6.jpg"));
		label5.setIcon(new ImageIcon(".\\image\\skin\\c7.jpg"));
		label6.setIcon(new ImageIcon(".\\image\\skin\\c8.jpg"));
		label7.setIcon(new ImageIcon(".\\image\\skin\\c1.jpg"));
		label8.setIcon(new ImageIcon(".\\image\\skin\\c2.jpg"));
		label9.setIcon(new ImageIcon(".\\image\\skin\\c3.jpg"));
		label10.setIcon(new ImageIcon(".\\image\\skin\\c4.jpg"));

		label1.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		label1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				skinChoose = 9;
				myscuimframe.changeSkin(skinChoose);
				Information.setThemeNo(9);
				myscuimframe.chooseSkinClose(1);
				if(Information.getStartListPanel()!=null){
					Information.getStartListPanel().backGround();
				}
				dispose();

			}
		});
		label2.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		label2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				skinChoose = 10;
				Information.setThemeNo(10);

				myscuimframe.changeSkin(skinChoose);
				myscuimframe.chooseSkinClose(1);
				if(Information.getStartListPanel()!=null){
					Information.getStartListPanel().backGround();
				}
				dispose();
			}
		});
		label3.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		label3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				skinChoose = 5;
				Information.setThemeNo(5);
				myscuimframe.changeSkin(skinChoose);
				myscuimframe.chooseSkinClose(1);
				if(Information.getStartListPanel()!=null){
					Information.getStartListPanel().backGround();
				}
				dispose();
			}
		});
		label4.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		label4.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				skinChoose = 6;
				Information.setThemeNo(6);
				myscuimframe.changeSkin(skinChoose);
				myscuimframe.chooseSkinClose(1);
				if(Information.getStartListPanel()!=null){
					Information.getStartListPanel().backGround();
				}
				dispose();
			}
		});
		label5.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		label5.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				skinChoose = 7;
				Information.setThemeNo(7);
				myscuimframe.changeSkin(skinChoose);
				myscuimframe.chooseSkinClose(1);
				if(Information.getStartListPanel()!=null){
					Information.getStartListPanel().backGround();
				}
				dispose();
			}
		});
		label6.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		label6.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				skinChoose = 8;
				Information.setThemeNo(8);
				myscuimframe.changeSkin(skinChoose);
				myscuimframe.chooseSkinClose(1);
				if(Information.getStartListPanel()!=null){
					Information.getStartListPanel().backGround();
				}
				dispose();

			}
		});
		label7.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		label7.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				skinChoose = 1;
				Information.setThemeNo(1);
				for (PersonalInformation pi : Information.getPersonalInfoHash()
						.values()) {
					pi.backGround();
				}
				for (GroupInformation gi : Information.getGroupInfoHash()
						.values()) {
					gi.backGround();
				}
				if(Information.getSearchPanel()!=null){
					Information.getSearchPanel().backGround();
				}
				if(Information.getStartListPanel()!=null){
					Information.getStartListPanel().backGround();
				}
				if(Information.getChooseSkinPanel()!=null){
					Information.getChooseSkinPanel().backGround();
				}
				for(TalkToOneWithStyle tempWindow: Information.getOpenedWindow().values()){
					tempWindow.backGround();
				}
				for(TalkToMany openedGroupWindow: Information.getOpenedGroupWindow().values()){
					openedGroupWindow.backGround();
				}
				if(Information.getChangeFace()!=null){
					Information.getChangeFace().backGround();
				}
				if(Information.getFunction()!=null){
					Information.getFunction().backGround();
				}
				myscuimframe.changeSkin(skinChoose);
				myscuimframe.chooseSkinClose(1);
				dispose();

			}
		});
		label8.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		label8.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				skinChoose = 2;
				Information.setThemeNo(2);
				for (PersonalInformation pi : Information.getPersonalInfoHash()
						.values()) {
					pi.backGround();
				}
				for (GroupInformation gi : Information.getGroupInfoHash()
						.values()) {
					gi.backGround();
				}
				if(Information.getSearchPanel()!=null){
					Information.getSearchPanel().backGround();
				}
				if(Information.getStartListPanel()!=null){
					Information.getStartListPanel().backGround();
				}
				if(Information.getChooseSkinPanel()!=null){
					Information.getChooseSkinPanel().backGround();
				}
				for(TalkToOneWithStyle tempWindow: Information.getOpenedWindow().values()){
					tempWindow.backGround();
				}
				for(TalkToMany Window: Information.getOpenedGroupWindow().values()){
					Window.backGround();
				}
				if(Information.getChangeFace()!=null){
					Information.getChangeFace().backGround();
				}
				if(Information.getFunction()!=null){
					Information.getFunction().backGround();
				}
				myscuimframe.changeSkin(skinChoose);
				myscuimframe.chooseSkinClose(1);
				dispose();

			}
		});
		label9.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		label9.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				skinChoose = 3;
				Information.setThemeNo(3);
				for (PersonalInformation pi : Information.getPersonalInfoHash()
						.values()) {
					pi.backGround();
				}
				for (GroupInformation gi : Information.getGroupInfoHash()
						.values()) {
					gi.backGround();
				}
				if(Information.getSearchPanel()!=null){
					Information.getSearchPanel().backGround();
				}
				if(Information.getStartListPanel()!=null){
					Information.getStartListPanel().backGround();
				}
				if(Information.getChooseSkinPanel()!=null){
					Information.getChooseSkinPanel().backGround();
				}
				for(TalkToOneWithStyle tempWindow: Information.getOpenedWindow().values()){
					tempWindow.backGround();
				}
				for(TalkToMany Window: Information.getOpenedGroupWindow().values()){
					Window.backGround();
				}
				if(Information.getChangeFace()!=null){
					Information.getChangeFace().backGround();
				}
				if(Information.getFunction()!=null){
					Information.getFunction().backGround();
				}
				myscuimframe.changeSkin(skinChoose);
				myscuimframe.chooseSkinClose(1);
				dispose();

			}
		});
		label10.setBorder(javax.swing.BorderFactory
				.createLineBorder(new Color(0, 0, 0)));
		label10.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				skinChoose = 4;
				Information.setThemeNo(4);
				for (PersonalInformation pi : Information.getPersonalInfoHash()
						.values()) {
					pi.backGround();
				}
				for (GroupInformation gi : Information.getGroupInfoHash()
						.values()) {
					gi.backGround();
				}
				if(Information.getSearchPanel()!=null){
					Information.getSearchPanel().backGround();
				}
				
				if(Information.getStartListPanel()!=null){
					Information.getStartListPanel().backGround();
				}
				
				if(Information.getChooseSkinPanel()!=null){
					Information.getChooseSkinPanel().backGround();
				}
				
				for(TalkToOneWithStyle tempWindow: Information.getOpenedWindow().values()){
					tempWindow.backGround();
				}
				
				for(TalkToMany Window: Information.getOpenedGroupWindow().values()){
					Window.backGround();
				}
				
				if(Information.getChangeFace()!=null){
					Information.getChangeFace().backGround();
				}
				if(Information.getFunction()!=null){
					Information.getFunction().backGround();
				}
				myscuimframe.changeSkin(skinChoose);
				myscuimframe.chooseSkinClose(1);
				dispose();

			}
		});

		pack();
		setVisible(true);
	}

//	public static void main(String args[]) {
//
//		ChooseSkin cS1 = new ChooseSkin(0, 0, myscuimframe);
//		cS1.setVisible(true);
//	}
//	
	public void backGround() {
		((JPanel) this.getContentPane()).setOpaque(false);// 取一个透明panel--this
		ImageIcon backImage = new ImageIcon(".\\image\\chooseSkin\\"+ Information.getThemeNo()+".png");

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

	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JLabel label5;
	private JLabel label6;
	private JLabel label7;
	private JLabel label8;
	private JLabel label9;
	private JLabel label10;
	private JLabel t1Label;
	private JLabel t2Label;
	private JLabel t3Label;
	private JLabel backgroundLabel;

	private static MyScuimFrame myscuimframe = null;
	private int skinChoose;

	// End of variables declaration//GEN-END:variables

}