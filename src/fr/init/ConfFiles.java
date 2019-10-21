package fr.init;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import fr.datafilesmanager.DatafilesManager;

public class ConfFiles {// String name, String path

	private static final String DIR_NAME = "conf";

	private static final String[] NAMES = { "controls", "gameSettings", "profile", "winConf" };

	private static final String EXTENSION = ".xml";

	private static void addConfFiles(DatafilesManager dfm) {
		dfm.addFile("winConf", "conf/winConf.xml");
		dfm.addFile("controls", "conf/controls.xml");
		dfm.addFile("profile", "conf/profile.xml");
		dfm.addFile("gameSettings", "conf/gameSettings.xml");
	}

	private static void createConfIfNotSet() {
		if (!dirExists(DIR_NAME)) {
			new File(DIR_NAME).mkdir();
		}

		for (String name : NAMES) {
			try {
				String path = DIR_NAME + "/" + name + EXTENSION;

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
