package tesi.util;

import java.io.File;

public final class Utility {
	
	public static final String PROJECT_ROOT = System.getProperty("user.dir") + File.separator + "InMemoryDB";
	public static final String RESOURCE_PATH = PROJECT_ROOT + File.separator + "resources";
	public static final String CONFIG_PATH = RESOURCE_PATH + File.separator + "config.properties";

	private Utility() { }
}
