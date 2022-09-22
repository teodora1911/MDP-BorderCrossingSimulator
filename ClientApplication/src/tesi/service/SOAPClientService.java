/**
 * SOAPClientService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package tesi.service;

public interface SOAPClientService extends java.rmi.Remote {
    public java.lang.String login(java.lang.String terminalName, int id, boolean police) throws java.rmi.RemoteException;
    public void logout(java.lang.String terminal, boolean police) throws java.rmi.RemoteException;
    public void stopTerminal(java.lang.String terminalName) throws java.rmi.RemoteException;
    public void releaseTerminal(java.lang.String terminalName) throws java.rmi.RemoteException;
    public java.lang.String getNextPassenger(java.lang.String terminal) throws java.rmi.RemoteException;
    public void updatePassenger(java.lang.String passenger, boolean passed) throws java.rmi.RemoteException;
    public tesi.model.CustomsDocument getDocument(java.lang.String terminal) throws java.rmi.RemoteException;
}
