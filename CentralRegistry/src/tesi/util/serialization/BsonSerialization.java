package tesi.util.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.undercouch.bson4jackson.BsonFactory;
import tesi.model.Terminal;
import tesi.util.Utility;

public class BsonSerialization extends SerializationUtility {
	
	public BsonSerialization(String serializationFolder) {
		super(serializationFolder, SerializationType.BSON, "bson");
	}

	@Override
	public void serialize(Terminal terminal) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper(new BsonFactory());
		mapper.writeValue(output, terminal);
		output.writeTo(new FileOutputStream(getPath(terminal)));
		output.close();
	}

	@Override
	public Terminal deserialize(String path) {
		Terminal terminal = null;
		try(FileInputStream in = new FileInputStream(path)){
			ByteArrayInputStream input = new ByteArrayInputStream(Utility.readAllBytes(in));
			ObjectMapper mapper = new ObjectMapper(new BsonFactory());
			terminal = mapper.readValue(input, Terminal.class);
		} catch (Exception e) {
			SerializationFactory.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return terminal;
	}

}
