package fr.skyforce77.towerminer;

import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import javax.crypto.SealedObject;

public class Data implements Serializable{

	private static final long serialVersionUID = 7411024786937542168L;

	public static Data data = new Data();

    public String player = "Player"+new Random().nextInt(99999);
    public UUID id = UUID.randomUUID();
    public SealedObject passtable;
    public byte[] publick;
    public HashMap<String, Object> storage = new HashMap<String, Object>();

    public static void save() {
        try {
            FileOutputStream bos = new FileOutputStream(new File(Launcher.getLauncherDirectory(),"database"));
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(data);
            bos.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        File f = new File(Launcher.getLauncherDirectory(),"database");
        f.getParentFile().mkdirs();
        try {
            if(!f.exists()) {
                f.createNewFile();
                save();
                return;
            }
            FileInputStream fis = new FileInputStream(f);
            ObjectInput in = new ObjectInputStream(fis);
            data = (Data)in.readObject();
            fis.close();
            in.close();
        } catch (Exception e) {}
    }
}
