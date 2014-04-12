package fr.skyforce77.towerminer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import fr.skyforce77.towerminer.EditableJTableModel.Info;

public class PassSaverPanel extends JPanel{

	private static final long serialVersionUID = -8098337044846442718L;

	JTable table;
	JTextField site;
	JTextField user;
	JPasswordField pass;
	JButton add;
	final PassSaverPanel pn = this;

	@SuppressWarnings({ "serial", "unchecked" })
	public PassSaverPanel() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		setSize(new Dimension(600,400));
		setBackground(new Color(255,255,255,125));
		if(Data.data.passtable != null) {
			SealedObject signedobject = Data.data.passtable;
			try {
				Object o = signedobject.getObject(new SecretKeySpec(Data.data.publick, 0, Data.data.publick.length, "DES"));
				table = new JTable(new EditableJTableModel((ArrayList<EditableJTableModel.Info>)o));
			} catch (Exception e) {
				e.printStackTrace();
				table = new JTable(new EditableJTableModel());
			}
		} else {
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

		site = new JTextField("Site") {
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7F));
				super.paint(g2);
				g2.dispose();
			}
		};
		site.setPreferredSize(new Dimension(219, 40));
		add(site);
		user = new JTextField("Utilisateur") {
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7F));
				super.paint(g2);
				g2.dispose();
			}
		};
		user.setPreferredSize(new Dimension(219, 40));
		add(user);
		add(Box.createHorizontalGlue());

		pass = new JPasswordField("pass") {
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7F));
				super.paint(g2);
				g2.dispose();
			}
		};
		pass.setEchoChar('*');
		pass.setPreferredSize(new Dimension(219, 40));
		add(pass);

		add = new JButton("Ajouter") {
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8F));
				super.paint(g2);
				g2.dispose();
			}
		};
		add.setPreferredSize(new Dimension(442, 40));
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditableJTableModel mod = (EditableJTableModel)table.getModel();
				mod.addSite(site.getText(), user.getText(), new String(pass.getPassword()));
				savePass();
			}
		});
		add(add);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(scrollPane)
				.addComponent(site, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(user, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(pass, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(add, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				);

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(scrollPane)
				.addComponent(site)
				.addGroup(layout.createSequentialGroup().addComponent(user).addComponent(pass))
				.addComponent(add, 500, 550, Integer.MAX_VALUE));
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
			JMenuItem read = new JMenuItem("Récupérer le mot de passe dans le presse papier", Launcher.getIcon("key"));
			read.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int s = table.getSelectedRow();
					Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
				    cb.setContents(new StringSelection(((EditableJTableModel)table.getModel()).getPass(s)), Launcher.instance);
				}
			});
			JMenuItem delete = new JMenuItem("Supprimer", Launcher.getIcon("delete"));
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					for(int s : table.getSelectedRows()) {
						((EditableJTableModel)table.getModel()).removeSite(s);
					}
					savePass();
				}
			});
			
			if(!table.getSelectionModel().isSelectionEmpty() && table.getSelectionModel().getMaxSelectionIndex() == table.getSelectionModel().getMinSelectionIndex()) {
				add(read);
			}
			add(delete);
		}
	}
	
	public void savePass() {
		try {
			EditableJTableModel mod = (EditableJTableModel)table.getModel();
			SecretKey key = KeyGenerator.getInstance("DES").generateKey();
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getEncoded(), key.getAlgorithm()));
			Data.data.publick = key.getEncoded();
			ArrayList<EditableJTableModel.Info> i = new ArrayList<EditableJTableModel.Info>();
			for(Info in : mod.getInfos()) {
				i.add(new Info(in.site, in.user, in.pass));
			}
			Data.data.passtable = new SealedObject(i, cipher);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Data.save();
	}
}
