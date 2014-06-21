package fr.skyforce77.towerminer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

public class EditableJTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -8145900105256258979L;

	private ArrayList<Info> infos = new ArrayList<Info>();
	public static String INSTALLED = "Installe";
	public static String UPDATE = "Mise a jour disponible";
	public static String DOWNLOAD = "Disponible";
	 
    private final String[] columns = {"Plugin","Version","Statut"};
 
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
                return infos.get(rowIndex).plugin;
            case 1:
                return infos.get(rowIndex).version;
            case 2:
                return getStatus(infos.get(rowIndex));
            default:
                return null;
        }
    }
    
    public String getStatus(Info i) {
    	File d = new File(Launcher.getDirectory(), "/plugins");
    	boolean exists = false;
    	boolean update = true;
    	for(File f : d.listFiles()) {
    		if(f.getName().contains(i.plugin)) {
    			exists = true;
    			if(f.getName().contains(i.version)) {
    				update = false;
    			}
    		}
    	}
    	if(exists) {
    		if(update && !PluginInstallerPanel.notified.contains(i.plugin)) {
    			PluginInstallerPanel.notified.add(i.plugin);
    			JOptionPane.showMessageDialog(null, "Mise a jour disponible\nPlugin: "+i.plugin+"\nVersion: "+i.version,"Information",JOptionPane.INFORMATION_MESSAGE);
    			Launcher.tabs.setSelectedIndex(1);
    		}
    		return update ? UPDATE : INSTALLED;
    	} else {
    		return DOWNLOAD;
    	}
    }
    
    public int getIntStatus(Info i) {
    	File d = new File(Launcher.getDirectory(), "/plugins");
    	boolean exists = false;
    	boolean update = true;
    	for(File f : d.listFiles()) {
    		if(f.getName().contains(i.plugin)) {
    			exists = true;
    			if(f.getName().contains(i.version)) {
    				update = false;
    			}
    		}
    	}
    	if(exists) {
    		return update ? 2 : 1;
    	} else {
    		return 0;
    	}
    }
 
    public void addSite(String plugin, String version, String url) {
        infos.add(new Info(plugin, version, url));
        fireTableRowsInserted(infos.size()-1, infos.size()-1);
    }
 
    public void removeSite(int rowIndex) {
        infos.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
    
    public static class Info implements Serializable{
		private static final long serialVersionUID = 7298260662741896214L;
		public String plugin,version,url;
    	
    	public Info(String plugin, String version,String url) {
    		this.plugin = plugin;
    		this.version = version;
    		this.url = url;
    	}
    }
    
    public String getVersion(int rowIndex) {
    	return infos.get(rowIndex).version;
    }
    
    public ArrayList<Info> getInfos() {
    	return infos;
    }

}
