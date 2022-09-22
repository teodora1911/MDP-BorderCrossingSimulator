/**
 * CentralRegistryAdministratorService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package tesi.service;

public interface CentralRegistryAdministratorService extends java.rmi.Remote {
    public tesi.model.Terminal getTerminal(java.lang.String name) throws java.rmi.RemoteException;
    public boolean deleteTerminal(java.lang.String name) throws java.rmi.RemoteException;
    public boolean addTerminal(tesi.model.Terminal terminal) throws java.rmi.RemoteException;
    public boolean updateTerminal(tesi.model.Terminal terminal) throws java.rmi.RemoteException;
    public java.lang.String[] getPassengers() throws java.rmi.RemoteException;
}
