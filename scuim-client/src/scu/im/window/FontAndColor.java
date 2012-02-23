package scu.im.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class FontAndColor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane = null;
	private JTextPane displayPane = null;
	private Box box, boxH1, boxH2 = null;
	private JButton sendButton, clearButton, insertPicButton,
			colorButton = null;
	private JTextField inputTextField = null;
	private JComboBox fontNameBox, fontSizeBox, fontStyleBox = null;
	private StyledDocument styledDoc = null;
	private Color tempColor = null;
	private String[] fontNameLibrary, fontSizeLibrary = null;

	public FontAndColor() {
		super("Change fonts and colors");
		try { // 使用Windows的界面风格
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		displayPane = new JTextPane();
		displayPane.setEditable(false);
		styledDoc = displayPane.getStyledDocument();

		scrollPane = new JScrollPane(displayPane);
		inputTextField = new JTextField(20);
		fontNameLibrary = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();
		fontSizeLibrary = new String[60];
		for (int i = 0; i < 60; i++) {
			fontSizeLibrary[i] = String.valueOf(i + 10);
		}
		String[] fontStyleLibrary = { "常规", "斜体", "粗体", "粗斜体" };

		fontNameBox = new JComboBox(fontNameLibrary);
		fontSizeBox = new JComboBox(fontSizeLibrary);
		fontStyleBox = new JComboBox(fontStyleLibrary);

		sendButton = new JButton("发送");
		clearButton = new JButton("清除");
		colorButton = new JButton("颜色");
		insertPicButton = new JButton("图片");

		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insert(getFontAttrib());
				inputTextField.setText("");
				inputTextField.requestFocusInWindow();
			}
		});

		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tempColor = JColorChooser.showDialog(null, "选择颜色", Color.black);
				inputTextField.requestFocusInWindow();
			}
		});

		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayPane.setText("");
				inputTextField.requestFocusInWindow();
			}
		});

		insertPicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser picChooser = new JFileChooser();
				picChooser.showOpenDialog(null);
				insertPic(picChooser.getSelectedFile());
				inputTextField.requestFocusInWindow();
			}
		});

		box = Box.createVerticalBox();
		boxH1 = Box.createHorizontalBox();
		boxH2 = Box.createHorizontalBox();
		box.add(boxH1);
		box.add(Box.createVerticalStrut(8));
		box.add(boxH2);
		box.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		boxH1.add(new Label("Font"));
		boxH1.add(Box.createHorizontalStrut(8));
		boxH1.add(fontNameBox);
		boxH1.add(Box.createHorizontalStrut(8));
		boxH1.add(new Label("Style"));
		boxH1.add(Box.createHorizontalStrut(8));
		boxH1.add(fontStyleBox);
		boxH1.add(Box.createHorizontalStrut(8));
		boxH1.add(new Label("Size"));
		boxH1.add(Box.createHorizontalStrut(8));
		boxH1.add(fontSizeBox);
		boxH1.add(Box.createHorizontalStrut(8));
		boxH1.add(colorButton);
		boxH1.add(Box.createHorizontalStrut(8));
		boxH1.add(insertPicButton);

		boxH2.add(inputTextField);
		boxH2.add(Box.createHorizontalStrut(8));
		boxH2.add(sendButton);
		boxH2.add(Box.createHorizontalStrut(8));
		boxH2.add(clearButton);
		boxH2.add(Box.createHorizontalStrut(8));
		// 功能区初始化结束

		getContentPane().add(scrollPane);
		getContentPane().add(box, BorderLayout.SOUTH);
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		inputTextField.requestFocusInWindow();
		// 初始化界面，设置焦点于文字输入框
	}

	private void insertPic(File file) {
		displayPane.setCaretPosition(styledDoc.getLength());
		displayPane.insertIcon(new ImageIcon(file.getPath() + ""));
		insert(new FontAttrib());

	}

	private void insert(FontAttrib attrib) {
		try {
			styledDoc.insertString(styledDoc.getLength(), attrib.getText()
					+ "\n", attrib.getAttrSet());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private FontAttrib getFontAttrib() {
		FontAttrib tempAtt = new FontAttrib();
		tempAtt.setText(inputTextField.getText());
		tempAtt.setFontName((String) fontNameBox.getSelectedItem());
		tempAtt.setFontSize(Integer.parseInt((String) fontSizeBox
				.getSelectedItem()));
		String tempStyle = (String) fontStyleBox.getSelectedItem();

		if (tempStyle.equals("常规")) {
			tempAtt.setFontStyle(FontAttrib.GENERAL);
		} else if (tempStyle.equals("粗体")) {
			tempAtt.setFontStyle(FontAttrib.BOLD);
		} else if (tempStyle.equals("斜体")) {
			tempAtt.setFontStyle(FontAttrib.ITALIC);
		} else if (tempStyle.equals("粗斜体")) {
			tempAtt.setFontStyle(FontAttrib.BOLD_ITALIC);
		}
		tempAtt.setFontColor(tempColor);
		return tempAtt;
	}

	private class FontAttrib {
		private static final int GENERAL = 0;
		private static final int BOLD = 1;
		private static final int ITALIC = 2;
		private static final int BOLD_ITALIC = 3;
		private SimpleAttributeSet attrSet = null;
		private String text, fontName = null;
		private int fontStyle, fontSize = 0;
		private Color fontColor = null;

		public SimpleAttributeSet getAttrSet() {
			attrSet = new SimpleAttributeSet();
			if (fontName != null) {
				StyleConstants.setFontFamily(attrSet, fontName);
			}

			switch (fontStyle) {
			case FontAttrib.GENERAL:
				StyleConstants.setBold(attrSet, false);
				StyleConstants.setItalic(attrSet, false);
				break;
			case FontAttrib.BOLD:
				StyleConstants.setBold(attrSet, true);
				StyleConstants.setItalic(attrSet, false);
				break;
			case FontAttrib.ITALIC:
				StyleConstants.setBold(attrSet, false);
				StyleConstants.setItalic(attrSet, true);
				break;
			case FontAttrib.BOLD_ITALIC:
				StyleConstants.setBold(attrSet, true);
				StyleConstants.setItalic(attrSet, true);
				break;
			}

			StyleConstants.setFontSize(attrSet, fontSize);
			if (fontColor != null) {
				StyleConstants.setForeground(attrSet, fontColor);
			}
			return attrSet;
		}

		public void setAttrSet(SimpleAttributeSet attr) {
			this.attrSet = attr;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getFontName() {
			return fontName;
		}

		public void setFontName(String fontName) {
			this.fontName = fontName;
		}

		public int getFontStyle() {
			return fontStyle;
		}

		public void setFontStyle(int fontStyle) {
			this.fontStyle = fontStyle;
		}

		public int getFontSize() {
			return fontSize;
		}

		public void setFontSize(int fontSize) {
			this.fontSize = fontSize;
		}

		public Color getFontColor() {
			return fontColor;
		}

		public void setFontColor(Color fontColor) {
			this.fontColor = fontColor;
		}

	}

	public static void main(String[] args) {
		new FontAndColor();
	}
}
