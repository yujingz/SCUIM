package scu.im.msgtype;

import java.awt.Color;
import java.io.Serializable;
import java.util.Date;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class MessageWithAttrib implements Serializable {

	private static final long serialVersionUID = 8771496493241755012L;
	public static final int GENERAL = 0;
	public static final int BOLD = 1;
	public static final int ITALIC = 2;
	public static final int BOLD_ITALIC = 3;
	private SimpleAttributeSet attrSet = null;
	private String text, fontName = null;
	private int fontStyle, fontSize = 0;
	private Color fontColor = null;

	private String senderUid = null;
	private String receiverUid = null;
	private String senderIp = null;
	private String receiverIp = null;
	private String gid;
	private String senderName;

	private Date sendTime = null;

	public SimpleAttributeSet getAttrSet() {
		attrSet = new SimpleAttributeSet();
		if (fontName != null) {
			StyleConstants.setFontFamily(attrSet, fontName);
		}

		switch (fontStyle) {
		case MessageWithAttrib.GENERAL:
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, false);
			break;
		case MessageWithAttrib.BOLD:
			StyleConstants.setBold(attrSet, true);
			StyleConstants.setItalic(attrSet, false);
			break;
		case MessageWithAttrib.ITALIC:
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, true);
			break;
		case MessageWithAttrib.BOLD_ITALIC:
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

	public String getSenderUid() {
		return senderUid;
	}

	public void setSenderUid(String senderUid) {
		this.senderUid = senderUid;
	}

	public String getReceiverUid() {
		return receiverUid;
	}

	public void setReceiverUid(String receiverUid) {
		this.receiverUid = receiverUid;
	}

	public String getSenderIp() {
		return senderIp;
	}

	public void setSenderIp(String senderIp) {
		this.senderIp = senderIp;
	}

	public String getReceiverIp() {
		return receiverIp;
	}

	public void setReceiverIp(String receiverIp) {
		this.receiverIp = receiverIp;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

}