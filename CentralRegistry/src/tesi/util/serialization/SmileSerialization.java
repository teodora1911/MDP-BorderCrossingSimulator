package tesi.util.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;

import tesi.model.Terminal;
import tesi.util.Utility;

public class SmileSerialization extends SerializationUtility {
	
	private SmileMapper mapper = new SmileMapper();
	
	public SmileSerialization(String serializationFolder) {
		super(serializationFolder, SerializationType.SMILE, "smile");
	}

	@Override
	public void serialize(Terminal terminal) throws IOException {
		byte[] bytes = mapper.writeValueAsBytes(terminal);
		FileOutputStream output = new FileOutputStream(getPath(terminal));
		output.write(bytes);
		output.close();
	}

	@Override
	public Terminal deserialize(String path) {
		Terminal terminal = null;
		try(FileInputStream in = new FileInputStream(path)){
			terminal = mapper.readValue(Utility.readAllBytes(in), Terminal.class);
		} catch (Exception e) {
			SerializationFactory.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return terminal;
	}

}
