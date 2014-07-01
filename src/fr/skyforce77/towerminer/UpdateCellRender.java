package fr.skyforce77.towerminer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class UpdateCellRender extends DefaultTableCellRenderer{

	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row,int col) {

		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		String s = value.toString();
		
		if(isSelected) {
			c.setBackground(Color.LIGHT_GRAY);
		} else {
			c.setBackground(Color.WHITE);
		}

		if (s.equalsIgnoreCase(EditableJTableModel.INSTALLED)) {
			c.setForeground(new Color(0x0F00C030));
		} else if (s.equalsIgnoreCase(EditableJTableModel.DOWNLOAD)) {
			c.setForeground(new Color(0x0F20B2F2));
		} else if (s.equalsIgnoreCase(EditableJTableModel.UPDATE)) {
			c.setForeground(new Color(0x0FC26136));
		} else {
			c.setForeground(Color.BLACK);
		}

		return c;
	}
}
