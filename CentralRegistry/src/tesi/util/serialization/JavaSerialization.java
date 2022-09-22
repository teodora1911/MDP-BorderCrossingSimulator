package tesi.util.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;

import tesi.model.Terminal;

public class JavaSerialization extends SerializationUtility {
	
	public JavaSerialization(String serializationFolder) {
		super(serializationFolder, SerializationType.JAVA, "ser");
	}

	@Override
	public void serialize(Terminal terminal) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(getPath(terminal)));
		out.writeObject(terminal);
		out.close();
	}

	@Override
	public Terminal deserialize(String path) {
		Terminal terminal = null;
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))){
            terminal = (Terminal) in.readObject();
        } catch (IOException | ClassNotFoundException e){
        	SerializationFactory.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }
		return terminal;
	}

}
