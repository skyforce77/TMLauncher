package fr.skyforce77.towerminer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

public class LauncherPanel extends JPanel{

	private static final long serialVersionUID = -8098337044846442718L;

	JEditorPane textArea;
	JTextField pseudo;
	JButton play;
	JButton close;
	final LauncherPanel pn = this;

	@SuppressWarnings("serial")
	public LauncherPanel() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		setSize(new Dimension(600,400));
		setBackground(new Color(255,255,255,125));
		textArea = new JEditorPane();
		textArea.setContentType("text/html");
		textArea.setBackground(new Color(0, 0, 0, 0));
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBackground(new Color(255,255,255,130));
		textArea.setEditable(false);
		add(scrollPane);

		try {
			textArea.setPage("http://dl.dropboxusercontent.com/u/38885163/TowerMiner/launcher/news.html");
		}catch (Exception e){
			textArea.setText("Impossible d'afficher le texte\nSi vous etes correctement connecte a Internet,\nceci est probablement en rapport\navec l'apparition d'un nouveau launcher.\nMerci de bien vouloir verifier sur http://www.skyforce77.fr");
		}

		JTextField dl = new JTextField("Votre pseudonyme: ") {
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7F));
				super.paint(g2);
				g2.dispose();
			}
		};
		dl.setEditable(false);
		dl.setPreferredSize(new Dimension(219, 40));
		add(dl);
		add(Box.createHorizontalGlue());
		pseudo = new JTextField() {
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7F));
				super.paint(g2);
				g2.dispose();
			}
		};
		pseudo.setPreferredSize(new Dimension(219, 40));
		pseudo.setDocument(new Launcher.JTextFieldLimit(12));
		pseudo.setText(Data.data.player);
		add(pseudo);

		play = new JButton("Jouer") {
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8F));
				super.paint(g2);
				g2.dispose();
			}
		};
		play.setPreferredSize(new Dimension(442, 40));
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Data.data.player = pseudo.getText();
				Launcher.instance.setVisible(false);
				Launcher.launch("ok");
			}
		});
		play.setOpaque(false);
		add(play);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(scrollPane)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
						5, 20)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(dl, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(pseudo, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(play, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				);

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(scrollPane)
				.addGroup(layout.createSequentialGroup().addComponent(dl).addComponent(pseudo))
				.addComponent(play, 500, 550, Integer.MAX_VALUE));
		setVisible(true);
		setEnabled(true);

		new Thread() {
			public void run() {
				while(pn.isVisible()) {
					try {
						Thread.sleep(1l);
						pn.repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(Launcher.getIBackground(), 0, 0, pn.getWidth(), pn.getHeight(), null);
		super.paint(g);
	}
}
