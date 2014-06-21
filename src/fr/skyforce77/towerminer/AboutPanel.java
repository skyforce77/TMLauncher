package fr.skyforce77.towerminer;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class AboutPanel extends JPanel{

	private static final long serialVersionUID = -8098337044846442718L;

	JEditorPane textArea;
	final AboutPanel pn = this;

	public AboutPanel(String url) {
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
		textArea.addHyperlinkListener(new HyperlinkListener() {
	        public void hyperlinkUpdate(HyperlinkEvent e) {
	            if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		            if(Desktop.isDesktopSupported()) {
		                try {
		                    Desktop.getDesktop().browse(e.getURL().toURI());
		                } catch (Exception e1) {
		                    e1.printStackTrace();
		                }
		            }
	            }
	        }
	    });
		add(scrollPane);

		try {
			textArea.setPage(url);
		}catch (Exception e){
			textArea.setText("Impossible d'afficher le texte\nSi vous etes correctement connecte a Internet,\nceci est probablement en rapport\navec l'apparition d'un nouveau launcher.\nMerci de bien vouloir verifier sur http://www.skyforce77.fr");
		}
		
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
}
