package tesi.util.serialization;

import java.io.File;
import java.io.IOException;

import tesi.model.Terminal;

public abstract class SerializationUtility {

	protected SerializationType type;
	protected String rootPath;
	protected String extension;
	
	public SerializationUtility(String serializationFolder, SerializationType type, String extension) {
		this.rootPath = serializationFolder;
		this.type = type;
		this.extension = extension;
	}
	
	protected String getPath(Terminal terminal) {
		return rootPath +File.separator +  terminal.getName() + "-" + terminal.getID() + "." + extension;
	}
	
	public abstract void serialize(Terminal terminal) throws IOException;
	public abstract Terminal deserialize(String path);
}
