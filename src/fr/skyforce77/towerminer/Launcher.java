package fr.skyforce77.towerminer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Launcher extends JFrame{

	private static final long serialVersionUID = -3444205831495972681L;
	public static String versionurl = "http://dl.dropboxusercontent.com/u/38885163/TowerMiner/version/version.txt";
	public static String downloadurl = "http://dl.dropboxusercontent.com/u/38885163/TowerMiner/version/TowerMiner.jar";
	public static int version = 6;
	public static Launcher instance;
	public static String actual = "";
	public static String actualdesc = "";

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
				if(!Download.update("Mise à jour "+actual, actualdesc)) {
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
			if(!arg.equals("install")) { 
				URL[] urls = { getGame().toURI().toURL() };  
				@SuppressWarnings("resource")
				URLClassLoader loader = new URLClassLoader(urls);  
				Class<?> cls = loader.loadClass("fr.skyforce77.towerminer.TowerMiner");
				Method main = cls.getDeclaredMethod("startGame", int.class, String.class, String.class);
				main.invoke(cls.newInstance(), new Object[]{version,arg,getOS()});
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(instance, "Une erreur est survenue,\nelle peut être causée par une mise à jour du launcher requise.","Information",JOptionPane.ERROR_MESSAGE);
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

}
