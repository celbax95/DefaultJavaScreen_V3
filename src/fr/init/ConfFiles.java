package fr.init;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import fr.datafilesmanager.DatafilesManager;

public class ConfFiles {// String name, String path

	private static final String DIR_NAME = ".conf/";
	private static final String RES_DIR_NAME = "conf/";

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
		File dir = new File(DIR_NAME);
		if (!dirExists(dir)) {
			dir.mkdir();
		}

		if (!dir.isHidden()) {
			Path p = Paths.get(dir.getPath());
			try {
				Files.setAttribute(p, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}

		for (String name : FILES.keySet()) {
			try {
				String path = DIR_NAME + FILES.get(name);
				String resPath = RES_DIR_NAME + FILES.get(name);

				File f = new File(path);

				if (f == null || !f.exists()) {

					InputStream is = ConfFiles.class.getResourceAsStream("/" + resPath);

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

	public static boolean dirExists(File dir) {
		return dir != null && dir.exists() && dir.isDirectory();
	}

	public static void initConfFiles(DatafilesManager dfm) {
		createConfIfNotSet();

		addConfFiles(dfm);
	}
}
