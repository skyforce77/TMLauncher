package fr.skyforce77.towerminer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class HTMLEditPanel extends JPanel{

	private static final long serialVersionUID = -8098337044846442718L;

	JEditorPane html;
	JEditorPane text;
	final HTMLEditPanel pn = this;

	public HTMLEditPanel() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		setSize(new Dimension(600,400));
		setBackground(new Color(255,255,255,125));
		
		html = new JEditorPane();
		html.setEditable(false);
		html.setContentType("text/html");
		html.setText("<font size=5>Exemple de texte</font>");
		
		text = new JEditorPane();
		text.setText(html.getText());
		text.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				html.setText(text.getText());
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				html.setText(text.getText());
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				html.setText(text.getText());
			}
		});
		
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(html), new JScrollPane(text));
		split.setDividerLocation(200);
		add(split);
		
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(split));

		layout.setHorizontalGroup(layout.createParallelGroup().addComponent(split));

		setVisible(true);
		setEnabled(true);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(Launcher.getIBackground(), 0, 0, pn.getWidth(), pn.getHeight(), null);
		super.paint(g);
	}
}
