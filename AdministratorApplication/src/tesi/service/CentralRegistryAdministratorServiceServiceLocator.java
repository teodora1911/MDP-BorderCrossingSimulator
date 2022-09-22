/**
 * CentralRegistryAdministratorServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package tesi.service;

public class CentralRegistryAdministratorServiceServiceLocator extends org.apache.axis.client.Service implements tesi.service.CentralRegistryAdministratorServiceService {

    public CentralRegistryAdministratorServiceServiceLocator() {
    }


    public CentralRegistryAdministratorServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CentralRegistryAdministratorServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CentralRegistryAdministratorService
    private java.lang.String CentralRegistryAdministratorService_address = "http://localhost:8080/CentralRegistry/services/CentralRegistryAdministratorService";

    public java.lang.String getCentralRegistryAdministratorServiceAddress() {
        return CentralRegistryAdministratorService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CentralRegistryAdministratorServiceWSDDServiceName = "CentralRegistryAdministratorService";

    public java.lang.String getCentralRegistryAdministratorServiceWSDDServiceName() {
        return CentralRegistryAdministratorServiceWSDDServiceName;
    }

    public void setCentralRegistryAdministratorServiceWSDDServiceName(java.lang.String name) {
        CentralRegistryAdministratorServiceWSDDServiceName = name;
    }

    public tesi.service.CentralRegistryAdministratorService getCentralRegistryAdministratorService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CentralRegistryAdministratorService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCentralRegistryAdministratorService(endpoint);
    }

    public tesi.service.CentralRegistryAdministratorService getCentralRegistryAdministratorService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            tesi.service.CentralRegistryAdministratorServiceSoapBindingStub _stub = new tesi.service.CentralRegistryAdministratorServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCentralRegistryAdministratorServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCentralRegistryAdministratorServiceEndpointAddress(java.lang.String address) {
        CentralRegistryAdministratorService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (tesi.service.CentralRegistryAdministratorService.class.isAssignableFrom(serviceEndpointInterface)) {
                tesi.service.CentralRegistryAdministratorServiceSoapBindingStub _stub = new tesi.service.CentralRegistryAdministratorServiceSoapBindingStub(new java.net.URL(CentralRegistryAdministratorService_address), this);
                _stub.setPortName(getCentralRegistryAdministratorServiceWSDDServiceName());
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
        if ("CentralRegistryAdministratorService".equals(inputPortName)) {
            return getCentralRegistryAdministratorService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.tesi", "CentralRegistryAdministratorServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.tesi", "CentralRegistryAdministratorService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CentralRegistryAdministratorService".equals(portName)) {
            setCentralRegistryAdministratorServiceEndpointAddress(address);
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
