/**
 * CentralRegistryClientServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package tesi.service;

public class CentralRegistryClientServiceServiceLocator extends org.apache.axis.client.Service implements tesi.service.CentralRegistryClientServiceService {

    public CentralRegistryClientServiceServiceLocator() {
    }


    public CentralRegistryClientServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CentralRegistryClientServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CentralRegistryClientService
    private java.lang.String CentralRegistryClientService_address = "http://localhost:8080/CentralRegistry/services/CentralRegistryClientService";

    public java.lang.String getCentralRegistryClientServiceAddress() {
        return CentralRegistryClientService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CentralRegistryClientServiceWSDDServiceName = "CentralRegistryClientService";

    public java.lang.String getCentralRegistryClientServiceWSDDServiceName() {
        return CentralRegistryClientServiceWSDDServiceName;
    }

    public void setCentralRegistryClientServiceWSDDServiceName(java.lang.String name) {
        CentralRegistryClientServiceWSDDServiceName = name;
    }

    public tesi.service.CentralRegistryClientService getCentralRegistryClientService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CentralRegistryClientService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCentralRegistryClientService(endpoint);
    }

    public tesi.service.CentralRegistryClientService getCentralRegistryClientService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            tesi.service.CentralRegistryClientServiceSoapBindingStub _stub = new tesi.service.CentralRegistryClientServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCentralRegistryClientServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCentralRegistryClientServiceEndpointAddress(java.lang.String address) {
        CentralRegistryClientService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (tesi.service.CentralRegistryClientService.class.isAssignableFrom(serviceEndpointInterface)) {
                tesi.service.CentralRegistryClientServiceSoapBindingStub _stub = new tesi.service.CentralRegistryClientServiceSoapBindingStub(new java.net.URL(CentralRegistryClientService_address), this);
                _stub.setPortName(getCentralRegistryClientServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CentralRegistryClientService".equals(inputPortName)) {
            return getCentralRegistryClientService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.tesi", "CentralRegistryClientServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.tesi", "CentralRegistryClientService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CentralRegistryClientService".equals(portName)) {
            setCentralRegistryClientServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
