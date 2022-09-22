package tesi.rmi.fileserver.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileServerServiceInterface extends Remote {
	
	public boolean upload(String passenger, byte[] bytes) throws RemoteException;
}
