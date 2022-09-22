package tesi.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.rpc.ServiceException;

import tesi.application.TestApplication;
import tesi.model.CustomsDocument;
import tesi.service.SOAPTestService;
import tesi.service.SOAPTestServiceServiceLocator;

public class SOAPUtility {

	private static SOAPUtility instance;
	
	private SOAPTestService service;
	
	public SOAPUtility() throws ServiceException {
		SOAPTestServiceServiceLocator locator = new SOAPTestServiceServiceLocator();
		service = locator.getSOAPTestService();
	}

	public static SOAPUtility getInstance() {
		if(instance == null) {
			try {
				instance = new SOAPUtility();
			} catch (ServiceException e) {
				TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
		
		return instance;
	}
	
	public String checkAvailability(String terminalName, int id) {
		String key = null;
		try {
			key = service.checkAvailability(terminalName, id);
		} catch (RemoteException e) {
			TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return key;
	}
	
	public void policeStop(String terminal, String passenger) {
		try {
			service.policeStop(terminal, passenger);
		} catch (RemoteException e) {
			TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public void customsStop(String terminal, String passenger, Set<File> files) {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ZipOutputStream zout = new ZipOutputStream(bout);
			for(File file : files) {
				ZipEntry entry = new ZipEntry(file.getName());
				zout.putNextEntry(entry);
				
				FileInputStream in = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int length;
				while((length = in.read(buffer)) > 0)
					zout.write(buffer, 0, length);
				zout.closeEntry();
				in.close();
			}
			zout.close();
			bout.close();
			CustomsDocument zipDocuments = new CustomsDocument(bout.toByteArray(), passenger);
			service.customsStop(terminal, zipDocuments);
		} catch (Exception e) {
			TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public int passed(String passenger) {
		int value = 0;
		try {
			value = service.passed(passenger);
		} catch (Exception e) {
			TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return value;
	}
}
