package fr.skyforce77.towerminer;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class EditableJTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -8145900105256258979L;

	private ArrayList<Info> infos = new ArrayList<Info>();
	 
    private final String[] columns = {"Site","Utilisateur"};
 
    public EditableJTableModel() {
        super();
    }
    
    public EditableJTableModel(ArrayList<Info> infos) {
        super();
        this.infos = infos;
    }
 
    public int getRowCount() {
        return infos.size();
    }
 
    public int getColumnCount() {
        return columns.length;
    }
 
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }
 
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return infos.get(rowIndex).site;
            case 1:
                return infos.get(rowIndex).user;
            default:
                return null;
        }
    }
 
    public void addSite(String site, String user, String pass) {
        infos.add(new Info(site, user, pass));
        fireTableRowsInserted(infos.size()-1, infos.size()-1);
    }
 
    public void removeSite(int rowIndex) {
        infos.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
    
    public static class Info implements Serializable{
		private static final long serialVersionUID = 7298260662741896214L;
		public String site,user,pass;
    	
    	public Info(String site, String user, String pass) {
    		this.site = site;
    		this.user = user;
    		this.pass = pass;
    	}
    }
    
    public String getPass(int rowIndex) {
    	return infos.get(rowIndex).pass;
    }
    
    public ArrayList<Info> getInfos() {
    	return infos;
    }

}
