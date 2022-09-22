package tesi.util.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import tesi.model.Terminal;

public class KryoSerialization extends SerializationUtility {
	
	private Kryo kryo = new Kryo();
	
	public KryoSerialization(String serializationFolder) {
		super(serializationFolder, SerializationType.KRYO, "kryo");
		kryo.register(Terminal.class);
	}

	@Override
	public void serialize(Terminal terminal) throws IOException {
		Output out = new Output(new FileOutputStream(getPath(terminal)));
		kryo.writeClassAndObject(out, terminal);
		out.close();
	}

	@Override
	public Terminal deserialize(String path) {
		Terminal terminal = null;
		try(Input in = new Input(new FileInputStream(path))){
			terminal = (Terminal) kryo.readClassAndObject(in);
		} catch (Exception e) {
			SerializationFactory.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return terminal;
	}

}
