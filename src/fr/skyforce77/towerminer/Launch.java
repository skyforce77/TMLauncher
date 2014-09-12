package fr.skyforce77.towerminer;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.UUID;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Launch {

	public static void launch(String tmversion, String arg) throws Exception{
		addURLToSystemClassLoader(Launcher.getGame().toURI().toURL());
		Class<?> cls = ClassLoader.getSystemClassLoader().loadClass("fr.skyforce77.towerminer.TowerMiner");
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		if(!launch_A_07(arg, cls) && !launch_A_08(arg, cls) && !launch_B_03(arg, cls)) {
			JOptionPane.showMessageDialog(null, "Une erreur est survenue,\nelle peut Etre causee par une mise a jour du launcher requise.","Information",JOptionPane.ERROR_MESSAGE);
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
	
	public static boolean launch_B_03(String arg, Class<?> cls) throws Exception{
		try {
			Method main = cls.getDeclaredMethod("startGame", int.class, String.class, String.class, String.class, UUID.class, int.class);
			main.invoke(cls.newInstance(), new Object[]{Launcher.version,arg,Launcher.getOS(),Data.data.player, Data.data.id, 0});
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean addURLToSystemClassLoader(URL url) throws IntrospectionException { 
		URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader(); 
		Class<URLClassLoader> classLoaderClass = URLClassLoader.class; 
		try { 
			Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[]{URL.class}); 
			method.setAccessible(true); 
			method.invoke(systemClassLoader, new Object[]{url}); 
		} catch (Throwable t) { 
			t.printStackTrace();
			return false;
		}
		return true;
	}
	
}
