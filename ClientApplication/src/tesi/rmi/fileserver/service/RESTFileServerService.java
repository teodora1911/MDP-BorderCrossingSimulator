package tesi.rmi.fileserver.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.MissingFormatArgumentException;
import java.util.Properties;
import java.util.logging.Level;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tesi.util.Utility;
import tesi.util.exceptions.LoadingConfigurationFileException;

@Path("/dokumenti")
public class RESTFileServerService {
	
	public static final String CONFIG_FILEPATH = Utility.getResourcesPath(true) + File.separator + "rmi-config.properties";
	private static String StorageDirectoryPath;
	
	static {
		try {
			loadConfigFile();
		} catch (LoadingConfigurationFileException e) {
			FileServerService.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public RESTFileServerService() { }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response example() {
		return Response.status(Response.Status.OK).entity("HELLO WORLD").build();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDocumentsFrom(@PathParam("id") String id) {
		ArrayList<byte[]> files = new ArrayList<>();
		File root = new File(StorageDirectoryPath + File.separator + id);
		if(!root.exists())
			return Response.status(Response.Status.NOT_FOUND).build();
		getDocuments(root, files);
		return Response.status(Response.Status.OK).entity(files).build();
	}
	
	private void getDocuments(File root, ArrayList<byte[]> files) {
		for(File file : root.listFiles()) {
			if(file.isFile())
				readDocument(file, files);
			else if(file.isDirectory()) {
				getDocuments(file, files);
			}
		}
	}
	
	private void readDocument(File file, ArrayList<byte[]> files) {
		try(FileInputStream in = new FileInputStream(file)) {
			files.add(Utility.readAllBytes(in));
		} catch (IOException e) {
			FileServerService.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	private static void loadConfigFile() throws LoadingConfigurationFileException {
		try(FileInputStream input = new FileInputStream(CONFIG_FILEPATH)){
			Properties properties = new Properties();
			properties.load(input);
			
			StorageDirectoryPath = properties.getProperty("file-server-storage");
			if(StorageDirectoryPath == null)
				throw new MissingFormatArgumentException("File server storage is not defined.");
			StorageDirectoryPath = Utility.getProjectRootPath(true) + StorageDirectoryPath;
			File storageDirectory = new File(StorageDirectoryPath);
			if(!storageDirectory.exists())
				storageDirectory.mkdir();
		} catch (Exception e) {
			FileServerService.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			throw new LoadingConfigurationFileException(e);
		}
	}
}
