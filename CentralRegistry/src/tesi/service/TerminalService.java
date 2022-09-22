package tesi.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import tesi.model.Entry;
import tesi.model.Terminal;
import tesi.util.Utility;
import tesi.util.serialization.SerializationFactory;

public class TerminalService {
	
	public Terminal getTerminalByName(String name) {
		Terminal deserializedTerminal = null;
		File rootTerminalsDirectory = new File(SerializationFactory.getInstance().getSerializationRootDirectory());
		List<File> files = Arrays.asList(rootTerminalsDirectory.listFiles());
		for(File file : files) {
			String[] parts = file.getName().split("-");
			if(parts[0].equals(name)) {
				int id = Integer.parseInt(parts[1].split("\\.")[0]);
				deserializedTerminal = SerializationFactory.getInstance().getSerializationUtility(id).deserialize(file.getAbsolutePath());
				break;
			}
		}
		return deserializedTerminal;
	}
	
	private Terminal getTerminalByID(int id) {
		Terminal deserializedTerminal = null;
		File rootTerminalsDirectory = new File(SerializationFactory.getInstance().getSerializationRootDirectory());
		List<File> files = Arrays.asList(rootTerminalsDirectory.listFiles());
		for(File file : files) {
			int identifier = Integer.parseInt(file.getName().split("-")[1].split("\\.")[0]);
			if(identifier == id) {
				deserializedTerminal = SerializationFactory.getInstance().getSerializationUtility(id).deserialize(file.getAbsolutePath());
				break;
			}
		}
		return deserializedTerminal;
	}
	
	private File[] getTerminalsFiles() {
		File rootTerminalsDirectory = new File(SerializationFactory.getInstance().getSerializationRootDirectory());
		return rootTerminalsDirectory.listFiles();
	}
	
	public boolean addTerminal(Terminal terminalToAdd) {
		boolean added = false;
		if(getTerminalByName(terminalToAdd.getName()) == null) {
			try(FileInputStream input = new FileInputStream(Utility.CONFIG_FILE_PATH)){
				// citanje brojaca terminala
				Properties properties = new Properties();
				properties.load(input);
				int id = Integer.parseInt(properties.getProperty("terminal-counter"));
				properties.setProperty("terminal-counter", String.valueOf(id + 1));
				FileOutputStream output = new FileOutputStream(Utility.CONFIG_FILE_PATH);
				properties.store(output, null);
				output.close();
				
				// serijalizacija terminala
				terminalToAdd.setID(id);
				SerializationFactory.getInstance().getSerializationUtility(id).serialize(terminalToAdd);
				added = true;
			} catch(Exception e) {
				Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
		return added;
	}
	
	public boolean updateTerminal(Terminal newTerminal) {
		boolean updated = false;
		Terminal oldTerminal;
		if((oldTerminal = getTerminalByID(newTerminal.getID())) != null) {
			boolean nameCheck = true;
			// provjera jedinstvenosti imena
			if(!newTerminal.getName().equals(oldTerminal.getName())) {
				for(File file : getTerminalsFiles())
					nameCheck = nameCheck && !file.getName().split("-")[0].equals(newTerminal.getName());
			}
			
			if(nameCheck) {
				try {
					deleteTerminal(oldTerminal.getName());
					SerializationFactory.getInstance().getSerializationUtility(newTerminal.getID()).serialize(newTerminal);
					updated = true;
				} catch (IOException e) {
					Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
				}
			}
		}
		
		return updated;
	}

	public boolean deleteTerminal(String name) {
		for(File file : getTerminalsFiles()) {
			if(file.getName().split("-")[0].equals(name))
				return file.delete();
		}
		return false;
	}
	
	// (-1) - ne postoji
	// (0) - ulaz
	// (1) - izlaz
	public int login(String terminal, int entry) {
		int response = -1;
		Terminal deserializedTerminal = getTerminalByName(terminal);
		if(deserializedTerminal != null) {
			for(Entry e : deserializedTerminal.getEntries()) {
				if(e.getId() == entry) {
					response = e.isEntry() ? 0 : 1;
					break;
				}
			}
		}
		return response;
	}
}
