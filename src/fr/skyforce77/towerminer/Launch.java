package fr.skyforce77.towerminer;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.UUID;

import javax.swing.JOptionPane;

public class Launch {

	public static void launch(String tmversion, String arg) throws Exception{
		URL[] urls = { Launcher.getGame().toURI().toURL() };
		@SuppressWarnings("resource")
		URLClassLoader loader = new URLClassLoader(urls);
		Class<?> cls = loader.loadClass("fr.skyforce77.towerminer.TowerMiner");
		if(!launch_A_07(arg, cls) && !launch_A_08(arg, cls)) {
			JOptionPane.showMessageDialog(null, "Une erreur est survenue,\nelle peut être causée par une mise à jour du launcher requise.","Information",JOptionPane.ERROR_MESSAGE);
		}
	}

	public static boolean launch_A_07(String arg, Class<?> cls) throws Exception{
		try {
			Method main = cls.getDeclaredMethod("startGame", int.class, String.class, String.class);
			main.invoke(cls.newInstance(), new Object[]{Launcher.version,arg,Launcher.getOS()});
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	public static boolean launch_A_08(String arg, Class<?> cls) throws Exception{
		try {
			Method main = cls.getDeclaredMethod("startGame", int.class, String.class, String.class, String.class, UUID.class);
			main.invoke(cls.newInstance(), new Object[]{Launcher.version,arg,Launcher.getOS(),Data.data.player, Data.data.id});
		} catch(Exception e) {
			return false;
		}
		return true;
	}
}
