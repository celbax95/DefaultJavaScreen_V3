package fr.init;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import fr.datafilesmanager.DatafilesManager;

public class ConfFiles {// String name, String path

	private static final String DIR_NAME = "conf/";

	private static final Map<String, String> FILES = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			this.put("winConf", "winConf.xml");
			this.put("controls", "controls.xml");
			this.put("profile", "profile.xml");
			this.put("gameSettings", "gameSettings.xml");
			this.put("serverConf", "serverConf.xml");
		}
	};

	private static void addConfFiles(DatafilesManager dfm) {
		for (String name : FILES.keySet()) {
			dfm.addFile(name, DIR_NAME + FILES.get(name));
		}
	}

	private static void createConfIfNotSet() {
		if (!dirExists(DIR_NAME)) {
			new File(DIR_NAME).mkdir();
		}

		for (String name : FILES.keySet()) {
			try {
				String path = DIR_NAME + FILES.get(name);

				File f = new File(path);

				if (f == null || !f.exists()) {

					InputStream is = ConfFiles.class.getResourceAsStream("/" + path);

					int read;
					byte[] buffer = new byte[1024];

					FileOutputStream fo = new FileOutputStream(path);

					while ((read = is.read(buffer)) != -1) {
						fo.write(buffer, 0, read);
					}
					fo.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean dirExists(String name) {
		File f = new File(DIR_NAME);

		return f != null && f.exists() && f.isDirectory();
	}

	public static void initConfFiles(DatafilesManager dfm) {
		createConfIfNotSet();

		addConfFiles(dfm);
	}
}
