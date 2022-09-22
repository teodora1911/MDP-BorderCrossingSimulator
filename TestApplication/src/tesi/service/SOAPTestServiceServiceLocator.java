/**
 * SOAPTestServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package tesi.service;

public class SOAPTestServiceServiceLocator extends org.apache.axis.client.Service implements tesi.service.SOAPTestServiceService {

    public SOAPTestServiceServiceLocator() {
    }


    public SOAPTestServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SOAPTestServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SOAPTestService
    private java.lang.String SOAPTestService_address = "http://localhost:8080/TestClientSOAP/services/SOAPTestService";

    public java.lang.String getSOAPTestServiceAddress() {
        return SOAPTestService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SOAPTestServiceWSDDServiceName = "SOAPTestService";

    public java.lang.String getSOAPTestServiceWSDDServiceName() {
        return SOAPTestServiceWSDDServiceName;
    }

    public void setSOAPTestServiceWSDDServiceName(java.lang.String name) {
        SOAPTestServiceWSDDServiceName = name;
    }

    public tesi.service.SOAPTestService getSOAPTestService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SOAPTestService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSOAPTestService(endpoint);
    }

    public tesi.service.SOAPTestService getSOAPTestService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            tesi.service.SOAPTestServiceSoapBindingStub _stub = new tesi.service.SOAPTestServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getSOAPTestServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSOAPTestServiceEndpointAddress(java.lang.String address) {
        SOAPTestService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (tesi.service.SOAPTestService.class.isAssignableFrom(serviceEndpointInterface)) {
                tesi.service.SOAPTestServiceSoapBindingStub _stub = new tesi.service.SOAPTestServiceSoapBindingStub(new java.net.URL(SOAPTestService_address), this);
                _stub.setPortName(getSOAPTestServiceWSDDServiceName());
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
        if ("SOAPTestService".equals(inputPortName)) {
            return getSOAPTestService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.tesi", "SOAPTestServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.tesi", "SOAPTestService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SOAPTestService".equals(portName)) {
            setSOAPTestServiceEndpointAddress(address);
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
