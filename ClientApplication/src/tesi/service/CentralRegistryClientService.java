/**
 * CentralRegistryClientService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package tesi.service;

public interface CentralRegistryClientService extends java.rmi.Remote {
    public int login(java.lang.String name, int entry) throws java.rmi.RemoteException;
    public void logPassengerCrossed(java.lang.String passenger) throws java.rmi.RemoteException;
    public void logPassengerOnWarrant(java.lang.String passenger) throws java.rmi.RemoteException;
    public java.lang.String[] getPassengers() throws java.rmi.RemoteException;
}
