package fr.skyforce77.towerminer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

import fr.skyforce77.towerminer.Launcher.updateThread;

public class Download{

	public static void update(String cause, String add, updateThread torun) {
		int count;
		boolean cancel = false;
		if(!add.equals("")) {
			TowerMinerPanel.setProgressTask(cause+": "+add);
		} else {
			TowerMinerPanel.setProgressTask(cause);
		}
		File temp = new File(Launcher.getGame().toString().replace(".jar","-temp.jar"));
		temp.getParentFile().mkdirs();
		try {
			temp.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			URL url = new URL(Launcher.downloadurl);
			URLConnection conection = url.openConnection();
			conection.connect();
			int lenghtOfFile = conection.getContentLength();
			InputStream input = new BufferedInputStream(url.openStream(), 8192);
			OutputStream output = new FileOutputStream(temp);

			byte data[] = new byte[1024];

			long total = 0;

			while ((count = input.read(data)) != -1 && !cancel) {
				total += count;
				TowerMinerPanel.setProgressTask(100, (int)((total*100)/lenghtOfFile));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		TowerMinerPanel.removeProgressTask();

		if(cancel) {
			temp.delete();
			torun.onUpdated(false);
		} else {
			move(temp,Launcher.getGame());
			temp.delete();
			torun.onUpdated(true);
		}
	}
	
	public static void downloadPlugin(String plugin, String version, String urlf) {
		int count;
		boolean cancel = false;
		TowerMinerPanel.setProgressTask("Telechargement du plugin: "+plugin);
		File temp = new File(Launcher.getDirectory(),"/temp/"+plugin+" v"+version+".jar");
		temp.getParentFile().mkdirs();
		try {
			temp.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			URL url = new URL(urlf);
			URLConnection conection = url.openConnection();
			conection.connect();
			int lenghtOfFile = conection.getContentLength();
			InputStream input = new BufferedInputStream(url.openStream(), 8192);
			OutputStream output = new FileOutputStream(temp);

			byte data[] = new byte[1024];

			long total = 0;

			while ((count = input.read(data)) != -1 && !cancel) {
				total += count;
				TowerMinerPanel.setProgressTask(100, (int)((total*100)/lenghtOfFile));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		TowerMinerPanel.removeProgressTask();

		if(cancel) {
			temp.delete();
		} else {
			move(temp,new File(Launcher.getDirectory(),"/plugins/"+plugin+" v"+version+".jar"));
			temp.delete();
		}
	}
	
	public static void download(String urlf, File directory, String filen) {
		int count;
		boolean cancel = false;
		File temp = new File(Launcher.getDirectory(),"/temp/"+filen);
		temp.getParentFile().mkdirs();
		try {
			temp.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			URL url = new URL(urlf);
			URLConnection conection = url.openConnection();
			conection.connect();
			int lenghtOfFile = conection.getContentLength();
			InputStream input = new BufferedInputStream(url.openStream(), 8192);
			OutputStream output = new FileOutputStream(temp);

			byte data[] = new byte[1024];

			long total = 0;
			while ((count = input.read(data)) != -1 && !cancel) {
				TowerMinerPanel.setProgressTask(100, (int)((total*100)/lenghtOfFile));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TowerMinerPanel.removeProgressTask();

		if(cancel) {
			temp.delete();
		} else {
			move(temp,new File(directory,filen));
			temp.delete();
		}
	}

	@SuppressWarnings("resource")
	public static void move(File source, File destination) {
		try {
			if(!destination.exists()) {
				destination.getParentFile().mkdirs();
				destination.createNewFile();
			}

			FileChannel sourcec = null;
			FileChannel destinationc = null;
			try {
				sourcec = new FileInputStream(source).getChannel();
				destinationc = new FileOutputStream(destination).getChannel();
				long count = 0;
				long size = sourcec.size();              
				while((count += destinationc.transferFrom(sourcec, count, size-count))<size);
				source.delete();
			}
			finally {
				if(sourcec != null) {
					sourcec.close();
				}
				if(destinationc != null) {
					destinationc.close();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
