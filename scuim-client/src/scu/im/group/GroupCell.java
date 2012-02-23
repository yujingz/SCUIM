package scu.im.group;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;


public class GroupCell extends JLabel implements ListCellRenderer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2696651440092475907L;
	private Border
				selectedBorder = BorderFactory.createLineBorder(Color.BLUE,1),
				emptyBorder = BorderFactory.createEmptyBorder(1,1,1,1);
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		GroupListItem item = (GroupListItem) value;
		
		this.setIcon(item.getIcon());
		this.setText(item.getNickName() + "(" + item.getUid() + ")");		
		if (isSelected){
			setBorder(selectedBorder);
		}else{
			setBorder(emptyBorder);
		}
		return this;
	}
	
	

}
