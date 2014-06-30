package fr.skyforce77.towerminer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

import javax.swing.ProgressMonitor;

import fr.skyforce77.towerminer.Launcher.updateThread;

public class Download{

	public static void update(String cause, String add, updateThread torun) {
		int count;
		boolean cancel = false;
		ProgressMonitor monitor = new ProgressMonitor(Launcher.instance, cause, add, 0, 100);
		File temp = new File(Launcher.getGame().toString().replace(".jar","-temp.jar"));
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
				monitor.setProgress((int)((total*100)/lenghtOfFile));
				output.write(data, 0, count);
				cancel = monitor.isCanceled();
			}

			output.flush();
			output.close();
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		monitor.close();

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
		ProgressMonitor monitor = new ProgressMonitor(Launcher.instance, "Telechargement du plugin", plugin, 0, 100);
		File temp = new File(Launcher.getDirectory(),"/temp/"+plugin+" v"+version+".jar");
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
				monitor.setProgress((int)((total*100)/lenghtOfFile));
				output.write(data, 0, count);
				cancel = monitor.isCanceled();
			}

			output.flush();
			output.close();
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		monitor.close();

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
		try {
			URL url = new URL(urlf);
			URLConnection conection = url.openConnection();
			conection.connect();
			InputStream input = new BufferedInputStream(url.openStream(), 8192);
			OutputStream output = new FileOutputStream(temp);

			byte data[] = new byte[1024];

			while ((count = input.read(data)) != -1 && !cancel) {
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

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
