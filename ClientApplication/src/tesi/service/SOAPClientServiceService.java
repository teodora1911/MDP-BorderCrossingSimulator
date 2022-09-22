/**
 * SOAPClientServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package tesi.service;

public interface SOAPClientServiceService extends javax.xml.rpc.Service {
    public java.lang.String getSOAPClientServiceAddress();

    public tesi.service.SOAPClientService getSOAPClientService() throws javax.xml.rpc.ServiceException;

    public tesi.service.SOAPClientService getSOAPClientService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
