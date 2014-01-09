package fr.skyforce77.towerminer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class Launcher extends JFrame{

	private static final long serialVersionUID = -3444205831495972681L;
	public static String versionurl = "http://dl.dropboxusercontent.com/u/38885163/TowerMiner/version/version.txt";
	public static String downloadurl = "http://dl.dropboxusercontent.com/u/38885163/TowerMiner/version/TowerMiner.jar";
	public static int version = 7;
	public static Launcher instance;
	public static String actual = "";
	public static String actualdesc = "";

	public static void main(String[] args) {
		Data.load();
		instance = new Launcher();
		instance.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				launch("ok");
			}
		});
		instance.setVisible(false);
		int changename = JOptionPane.showConfirmDialog (null, "Voulez vous utiliser le pseudo "+Data.data.player+" ?","Pseudo", JOptionPane.YES_NO_OPTION);
		if(changename == JOptionPane.NO_OPTION) {
			instance.setSize(new Dimension(300,140));
			instance.setLocationRelativeTo(null);
			instance.setTitle("Choix du pseudo");
			JPanel panel = new JPanel();
			final JTextField field = new JTextField();
			field.setPreferredSize(new Dimension(250, 40));
			field.setDocument(new JTextFieldLimit(12));
			field.setText(Data.data.player);
			panel.add(field);
			JButton valid = new JButton("Valider");
			valid.setPreferredSize(new Dimension(125, 40));
			valid.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Data.data.player = field.getText();
					instance.setVisible(false);
					launch("ok");
				}
			});
			panel.add(valid);
			instance.add(panel);
			instance.setVisible(true);
		} else {
			launch("ok");
		}
	}

	public static void launch(String arg) {
		if(!getActualVersion()) {
			arg = "offline";
		}
		else if(getGame().exists() && getFileVersion().exists()) {
			getGame().getParentFile().mkdirs();
			String version = getVersion();
			if(!version.equals(actual)) {
				int update = JOptionPane.showConfirmDialog(null, "Voulez vous mettre à jour?\n"+actual+"\n"+actualdesc, "Mise à jour diponible", JOptionPane.YES_NO_OPTION);
				if(update == JOptionPane.YES_OPTION) {
					if(!Download.update("Mise à jour "+actual, actualdesc)) {
						return;
					}
					setVersion(actual);
					arg = "update";
				}
			}
		} else {
			getGame().getParentFile().mkdirs();
			if(!Download.update("Téléchargement des fichiers...","")) {
				return;
			}
			setVersion(actual);
			arg = "install";
			JOptionPane.showMessageDialog(instance, "Téléchargement du jeu effectué, vous devez maintenant relancer le jeu","Information",JOptionPane.INFORMATION_MESSAGE);
		}
		try {
			if(!arg.equals("install")) {
				Launch.launch(getVersion(),arg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(instance, "Une erreur est survenue,\nelle peut être causée par une mise à jour du launcher requise.","Information",JOptionPane.ERROR_MESSAGE);
		}
		Data.save();
		instance.dispose();
	}

	public static File getDirectory() {
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN"))
			return new File(System.getenv("APPDATA"),"/.towerminer");
		else if (OS.contains("MAC"))
			return new File(System.getProperty("user.home") + "/Library/Application "
					+ "Support","/.towerminer");
		else if (OS.contains("NUX"))
			return new File(System.getProperty("user.home"),"/.towerminer");
		return new File(System.getProperty("user.dir"),"/.towerminer");
	}

	public static String getOS() {
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN"))
			return "windows";
		else if (OS.contains("MAC"))
			return "mac";
		else if (OS.contains("NUX"))
			return "linux";
		else
			return "wtf";
	}

	public static File getMapsDirectory() {
		return new File(getDirectory(), "/maps");
	}

	public static File getLauncherDirectory() {
		return new File(getDirectory(), "/launcher");
	}

	public static File getGame() {
		return new File(getDirectory(), "/launcher/TowerMiner.jar");
	}

	public static File getFileVersion() {
		return new File(getDirectory(), "/launcher/version.txt");
	}

	public static void setVersion(String version) {
		try {
			FileWriter fstream = new FileWriter(getFileVersion());
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(version);
			out.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static String getVersion() {
		String version = "";
		try {
			FileReader fstream = new FileReader(getFileVersion());
			BufferedReader out = new BufferedReader(fstream);
			version = out.readLine();
			out.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		return version;
	}

	public static boolean getActualVersion() {
		try {
			BufferedReader out = new BufferedReader(new InputStreamReader(new URL(versionurl).openStream()));
			actual = out.readLine();
			actualdesc = out.readLine();
			out.close();
			return true;
		}catch (Exception e){
			return false;
		}
	}

	static class JTextFieldLimit extends PlainDocument{
		private static final long serialVersionUID = -1166486069367323819L;
		private int limit;
		JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}

		JTextFieldLimit(int limit, boolean upper) {
			super();
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null)
				return;

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			}
		}
	}

}
