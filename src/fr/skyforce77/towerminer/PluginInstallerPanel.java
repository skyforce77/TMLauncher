package fr.skyforce77.towerminer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import fr.skyforce77.towerminer.EditableJTableModel.Info;

public class PluginInstallerPanel extends JPanel{

	private static final long serialVersionUID = -8098337044846442718L;

	JTable table;
	final PluginInstallerPanel pn = this;

	public PluginInstallerPanel() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		setSize(new Dimension(600,400));
		setBackground(new Color(255,255,255,125));
			try {
				ArrayList<Info> i = new ArrayList<EditableJTableModel.Info>();
				try {
					final BufferedReader out = new BufferedReader(new InputStreamReader(new URL("http://dl.dropboxusercontent.com/u/38885163/TowerMiner/launcher/plugins.txt").openStream()));
					String line = "";
					while((line = out.readLine()) != null) {
						String[] lines = line.split("@");
						i.add(new Info(lines[0], lines[1], lines[2]));
					}
				} catch(Exception e) {}
				table = new JTable(new EditableJTableModel(i));
			} catch (Exception e) {
				e.printStackTrace();
				table = new JTable(new EditableJTableModel());
			}
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					if(table.getSelectionModel().getMaxSelectionIndex() == -1 && table.getSelectionModel().getMinSelectionIndex() == -1) {
						int rowNumber = table.rowAtPoint(e.getPoint());
						table.getSelectionModel().setSelectionInterval(rowNumber, rowNumber);
					}
					new PassLinePopupMenu().show(table, e.getX(), e.getY());
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(scrollPane));

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(scrollPane));
		setVisible(true);
		setEnabled(true);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(Launcher.getIBackground(), 0, 0, pn.getWidth(), pn.getHeight(), null);
		super.paint(g);
	}

	public class PassLinePopupMenu extends JPopupMenu {
		private static final long serialVersionUID = -341919318715839376L;

		public PassLinePopupMenu() {
			JMenuItem read = new JMenuItem("Installer", Launcher.getIcon("key"));
			read.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int s = table.getSelectedRow();
					final Info i = ((EditableJTableModel)table.getModel()).getInfos().get(s);
					new Thread() {
						public void run() {
							Download.downloadPlugin(i.plugin, i.version, i.url);
						};
					}.start();
				}
			});
			JMenuItem delete = new JMenuItem("Supprimer", Launcher.getIcon("delete"));
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					for(int s : table.getSelectedRows()) {
						File d = new File(Launcher.getDirectory(), "/plugins");
						for(File f : d.listFiles()) {
				    		if(f.getName().contains(((EditableJTableModel)table.getModel()).getInfos().get(s).plugin)) {
				    			f.delete();
				    		}
				    	}
						Info i = ((EditableJTableModel)table.getModel()).getInfos().get(s);
						((EditableJTableModel)table.getModel()).removeSite(s);
						((EditableJTableModel)table.getModel()).addSite(i.plugin, i.version, i.url);
					}
				}
			});
			
			if(!table.getSelectionModel().isSelectionEmpty() && table.getSelectionModel().getMaxSelectionIndex() == table.getSelectionModel().getMinSelectionIndex()) {
				add(read);
			}
			add(delete);
		}
	}
}
