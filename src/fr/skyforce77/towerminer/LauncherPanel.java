package fr.skyforce77.towerminer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

public class LauncherPanel extends JPanel{

	private static final long serialVersionUID = -8098337044846442718L;

	JTextArea textArea;
	JTextField pseudo;
	JButton play;
	JButton close;

	@SuppressWarnings("serial")
	public LauncherPanel() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		setSize(new Dimension(600,400));
		setBackground(new Color(255,255,255,125));
		setVisible(true);
		setEnabled(true);
		textArea = new JTextArea(10, 40);
		textArea.setBackground(new Color(0, 0, 0, 0));
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBackground(new Color(255,255,255,130));
		textArea.setEditable(false);
		add(scrollPane);

		try {
			BufferedReader out = new BufferedReader(new InputStreamReader(new URL("http://dl.dropboxusercontent.com/u/38885163/TowerMiner/launcher/news.txt").openStream()));
			String s = "";
			String line;
			while((line = out.readLine()) != null) {
				s = s+"\n" +line;
			}
			out.close();
			textArea.setText(s.replaceFirst("\n", ""));
		}catch (Exception e){}

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
				.addComponent(play, 500, 550, 600));

	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(Launcher.getIBackground(), 0, 0, 600, 400, null);
		super.paint(g);
	}
}
