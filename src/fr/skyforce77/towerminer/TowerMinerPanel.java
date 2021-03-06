package fr.skyforce77.towerminer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

public class TowerMinerPanel extends JPanel{

	private static final long serialVersionUID = -8098337044846442718L;

	public JEditorPane textArea;
	public JTextField pseudo;
	public JButton play;
	public JButton close;
	public JProgressBar progressbar;
	final TowerMinerPanel pn = this;

	@SuppressWarnings("serial")
	public TowerMinerPanel() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		setSize(new Dimension(600,400));
		setBackground(new Color(255,255,255,125));
		textArea = new JEditorPane();
		textArea.setBackground(new Color(0, 0, 0, 0));
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBackground(new Color(255,255,255,130));
		textArea.setEditable(false);
		HTMLEditorKit kit=new HTMLEditorKit();
		kit.setAutoFormSubmission(false);
        textArea.setEditorKit(kit);
		textArea.addHyperlinkListener(new HyperlinkListener() {
	        public void hyperlinkUpdate(HyperlinkEvent e) {
	            if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		            if(Desktop.isDesktopSupported() && !e.getURL().getPath().startsWith("https://dl.dropboxusercontent.com/u/38885163/")) {
		                try {
		                    Desktop.getDesktop().browse(e.getURL().toURI());
		                } catch (Exception e1) {
		                    e1.printStackTrace();
		                }
		            } else {
		            	try {
							textArea.setPage(e.getURL());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		            }
	            }
	        }
	    });
		add(scrollPane);

		try {
			textArea.setPage("https://dl.dropboxusercontent.com/u/38885163/TowerMiner/launcher/front2.html");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(textArea.getText().equals("")) {
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
		pseudo.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent arg0) {
				if(pseudo.getText() != null && pseudo.getText().length() >= 5) {
					play.setEnabled(true);
				} else {
					play.setEnabled(false);
				}
			}
		});
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
				Launcher.launch("ok");
			}
		});
		
		if(Data.data.player == null || Data.data.player.length() < 5) {
			play.setEnabled(false);
		}
		
		play.setOpaque(false);
		add(play);
		
		progressbar = new JProgressBar();
		progressbar.setVisible(false);
		progressbar.setStringPainted(true);
		add(progressbar);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(scrollPane)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
						5, 20)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(dl, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(pseudo, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(play, 30, 35, GroupLayout.PREFERRED_SIZE)
								.addComponent(progressbar, 30, 35, GroupLayout.PREFERRED_SIZE)
				);

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(scrollPane)
				.addComponent(progressbar, 500, 550, Integer.MAX_VALUE)
				.addGroup(layout.createSequentialGroup().addComponent(dl).addComponent(pseudo))
				.addComponent(play, 500, 550, Integer.MAX_VALUE));
		setVisible(true);
		setEnabled(true);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(Launcher.getIBackground(), 0, 0, pn.getWidth(), pn.getHeight(), null);
		super.paint(g);
	}
	
	public static void setProgressTask(String task) {
		Launcher.launcherpanel.progressbar.setString(task);
		Launcher.launcherpanel.play.setVisible(false);
		Launcher.launcherpanel.progressbar.setVisible(true);
	}
	
	public static void removeProgressTask() {
		Launcher.launcherpanel.progressbar.setString("");
		Launcher.launcherpanel.progressbar.setValue(0);
		Launcher.launcherpanel.play.setVisible(true);
		Launcher.launcherpanel.progressbar.setVisible(false);
	}
	
	public static void setProgressTask(int max, int progress) {
		Launcher.launcherpanel.progressbar.setValue(progress);
		Launcher.launcherpanel.progressbar.setMaximum(max);
	}
}
