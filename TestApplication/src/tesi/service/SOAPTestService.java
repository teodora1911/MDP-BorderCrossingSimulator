/**
 * SOAPTestService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package tesi.service;

public interface SOAPTestService extends java.rmi.Remote {
    public int passed(java.lang.String passenger) throws java.rmi.RemoteException;
    public java.lang.String checkAvailability(java.lang.String terminalName, int id) throws java.rmi.RemoteException;
    public void policeStop(java.lang.String terminal, java.lang.String passenger) throws java.rmi.RemoteException;
    public void customsStop(java.lang.String terminal, tesi.model.CustomsDocument documents) throws java.rmi.RemoteException;
}
