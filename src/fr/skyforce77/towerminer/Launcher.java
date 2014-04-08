package fr.skyforce77.towerminer;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class Launcher extends JFrame{

	private static final long serialVersionUID = -3444205831495972681L;
	public static String versionurl = "http://dl.dropboxusercontent.com/u/38885163/TowerMiner/version/version.txt";
	public static String pagesurl = "http://dl.dropboxusercontent.com/u/38885163/TowerMiner/launcher/pages.txt";
	public static String downloadurl = "http://dl.dropboxusercontent.com/u/38885163/TowerMiner/version/TowerMiner.jar";
	public static int version = 10;
	public static Launcher instance;
	public static String actual = "";
	public static String actualdesc = "";
	public static JTabbedPane tabs;
	public static TowerMinerPanel launcherpanel;
	public static AboutPanel aboutpanel;
	public static int back = 0;

	public static void main(String[] args) {
		Data.load();
		
		getDirectory().mkdirs();
		
		verifiyLauncherVersion();
		back = new Random().nextInt(2);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		instance = new Launcher();
		instance.addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(1);
			}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowOpened(WindowEvent e) {}
		});
		instance.setIconImage(getIcon());
		instance.setSize(new Dimension(600,400));
		instance.setLocationRelativeTo(null);
		instance.setMinimumSize(new Dimension(600,400));
		instance.setTitle("Skyforce77 - Launcher v"+version);
		
		tabs = new JTabbedPane();
		instance.add(tabs);
		
		launcherpanel = new TowerMinerPanel();
		tabs.addTab("TowerMiner", getTowerMinerIcon(), launcherpanel);
		
		new Thread(){
			public void run() {createPages();};
		}.start();
		
		instance.setVisible(true);
		
		new Thread() {
			public void run() {
				while(instance.isVisible()) {
					try {
						Thread.sleep(1l);
						instance.repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
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
					new Thread() {
						public void run() {
							Download.update("Mise à jour "+actual, actualdesc, new updateThread(){
								public void onUpdated(boolean success) {
									if(success) {
										setVersion(actual);
										Launcher.launch("update");
									} else {
										JOptionPane.showMessageDialog(instance, "La mise à jour a échoué\nelle peut être causée par une mise à jour du launcher requise.","Information",JOptionPane.ERROR_MESSAGE);
									}
								};
							});
						};
					}.start();
					return;
				}
			}
		} else {
			getGame().getParentFile().mkdirs();
			new Thread() {
				public void run() {
					Download.update("Téléchargement des fichiers...","", new updateThread(){
						public void onUpdated(boolean success) {
							if(success) {
								setVersion(actual);
								JOptionPane.showMessageDialog(instance, "Téléchargement du jeu effectué, vous devez maintenant relancer le jeu","Information",JOptionPane.INFORMATION_MESSAGE);
								System.exit(1);
							} else {
								JOptionPane.showMessageDialog(instance, "Le téléchargement a échoué\nelle peut être causée par une mise à jour du launcher requise.","Information",JOptionPane.ERROR_MESSAGE);
							}
						};
					});
					return;
				};
			}.start();
			return;
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
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
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
	
	public static void createPages() {
		try {
			BufferedReader out = new BufferedReader(new InputStreamReader(new URL(pagesurl).openStream()));
			String s = "";
			while(s != null) {
				s = out.readLine();
				if(s != null && s != "") {
					String name = s.split("=")[0];
					aboutpanel = new AboutPanel(s.replace(name+"=",""));
					tabs.addTab(name, getAboutIcon(), aboutpanel);
				}
			}
			out.close();
		}catch (Exception e){}
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
	
	public static void verifiyLauncherVersion() {
		try {
			BufferedReader out = new BufferedReader(new InputStreamReader(new URL("http://dl.dropboxusercontent.com/u/38885163/TowerMiner/launcher/version.txt").openStream()));
			if(Integer.parseInt(out.readLine()) > version) {
				int i = JOptionPane.showConfirmDialog(null, "Voulez vous mettre a jour votre launcher?", "Mise a� jour du launcher diponible", JOptionPane.YES_NO_OPTION);
				if(i == JOptionPane.YES_OPTION) {
					Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
					if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
						try {
							desktop.browse(new URL(out.readLine()).toURI());
							System.exit(1);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				} else if(i == JOptionPane.CLOSED_OPTION) {
					System.exit(1);
				}
			}
		}catch (Exception e){}
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
	
	public static Image getIcon() {
        Image image = new ImageIcon(Launcher.class.getResource("/ressources/avatardaft.png")).getImage();
        return image;
    }
	
	public static ImageIcon getTowerMinerIcon() {
        return new ImageIcon(Launcher.class.getResource("/ressources/icon.png"));
    }
	
	public static ImageIcon getAboutIcon() {
        return new ImageIcon(Launcher.class.getResource("/ressources/paper.png"));
    }

    public static Image getIBackground() {
        Image image = new ImageIcon(Launcher.class.getResource("/ressources/background"+back+".png")).getImage();
        return image;
    }
    
	public static class updateThread extends Thread {
		public void onUpdated(boolean success) {}
	}

}
