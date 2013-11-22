package fr.skyforce77.towerminer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Launcher extends JFrame{

	private static final long serialVersionUID = -3444205831495972681L;
	public static String versionurl = "http://dl.dropboxusercontent.com/u/38885163/TowerMiner/version/version.txt";
	public static String downloadurl = "http://dl.dropboxusercontent.com/u/38885163/TowerMiner/version/TowerMiner.jar";
	public static int version = 4;
	public static Launcher instance;
	public static String actual = "";

	public static void main(String[] args) {
		instance = new Launcher();
		instance.setVisible(false);
		String arg = "ok";
		if(!getActualVersion()) {
			arg = "offline";
		}
		else if(getGame().exists() && getFileVersion().exists()) {
			getGame().getParentFile().mkdirs();
			String version = getVersion();
			if(!version.equals(actual)) {
				if(!Download.update("Mise à jour en cours...", actual.replace(" ",""))) {
					return;
				}
				setVersion(actual);
				arg = "update";
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
			if(!arg.equals("install"))
				Runtime.getRuntime().exec("java -jar "+getGame()+" "+version+" "+arg+" "+getOS());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		String version = "";
		try {
			BufferedReader out = new BufferedReader(new InputStreamReader(new URL(versionurl).openStream()));
			version = out.readLine();
			out.close();
			actual = version;
			return true;
		}catch (Exception e){
			return false;
		}
	}

}
