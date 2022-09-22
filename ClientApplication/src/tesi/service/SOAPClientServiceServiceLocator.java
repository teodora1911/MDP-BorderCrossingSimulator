/**
 * SOAPClientServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package tesi.service;

public class SOAPClientServiceServiceLocator extends org.apache.axis.client.Service implements tesi.service.SOAPClientServiceService {

    public SOAPClientServiceServiceLocator() {
    }


    public SOAPClientServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SOAPClientServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SOAPClientService
    private java.lang.String SOAPClientService_address = "http://localhost:8080/TestClientSOAP/services/SOAPClientService";

    public java.lang.String getSOAPClientServiceAddress() {
        return SOAPClientService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SOAPClientServiceWSDDServiceName = "SOAPClientService";

    public java.lang.String getSOAPClientServiceWSDDServiceName() {
        return SOAPClientServiceWSDDServiceName;
    }

    public void setSOAPClientServiceWSDDServiceName(java.lang.String name) {
        SOAPClientServiceWSDDServiceName = name;
    }

    public tesi.service.SOAPClientService getSOAPClientService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SOAPClientService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSOAPClientService(endpoint);
    }

    public tesi.service.SOAPClientService getSOAPClientService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            tesi.service.SOAPClientServiceSoapBindingStub _stub = new tesi.service.SOAPClientServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getSOAPClientServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSOAPClientServiceEndpointAddress(java.lang.String address) {
        SOAPClientService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (tesi.service.SOAPClientService.class.isAssignableFrom(serviceEndpointInterface)) {
                tesi.service.SOAPClientServiceSoapBindingStub _stub = new tesi.service.SOAPClientServiceSoapBindingStub(new java.net.URL(SOAPClientService_address), this);
                _stub.setPortName(getSOAPClientServiceWSDDServiceName());
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
        if ("SOAPClientService".equals(inputPortName)) {
            return getSOAPClientService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.tesi", "SOAPClientServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.tesi", "SOAPClientService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SOAPClientService".equals(portName)) {
            setSOAPClientServiceEndpointAddress(address);
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
