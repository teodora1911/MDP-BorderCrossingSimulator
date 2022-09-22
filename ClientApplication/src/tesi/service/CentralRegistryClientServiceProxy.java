package tesi.service;

public class CentralRegistryClientServiceProxy implements tesi.service.CentralRegistryClientService {
  private String _endpoint = null;
  private tesi.service.CentralRegistryClientService centralRegistryClientService = null;
  
  public CentralRegistryClientServiceProxy() {
    _initCentralRegistryClientServiceProxy();
  }
  
  public CentralRegistryClientServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCentralRegistryClientServiceProxy();
  }
  
  private void _initCentralRegistryClientServiceProxy() {
    try {
      centralRegistryClientService = (new tesi.service.CentralRegistryClientServiceServiceLocator()).getCentralRegistryClientService();
      if (centralRegistryClientService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)centralRegistryClientService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)centralRegistryClientService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (centralRegistryClientService != null)
      ((javax.xml.rpc.Stub)centralRegistryClientService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public tesi.service.CentralRegistryClientService getCentralRegistryClientService() {
    if (centralRegistryClientService == null)
      _initCentralRegistryClientServiceProxy();
    return centralRegistryClientService;
  }
  
  public void logPassengerCrossed(java.lang.String passenger) throws java.rmi.RemoteException{
    if (centralRegistryClientService == null)
      _initCentralRegistryClientServiceProxy();
    centralRegistryClientService.logPassengerCrossed(passenger);
  }
  
  public void logPassengerOnWarrant(java.lang.String passenger) throws java.rmi.RemoteException{
    if (centralRegistryClientService == null)
      _initCentralRegistryClientServiceProxy();
    centralRegistryClientService.logPassengerOnWarrant(passenger);
  }
  
  public java.lang.String[] getPassengers() throws java.rmi.RemoteException{
    if (centralRegistryClientService == null)
      _initCentralRegistryClientServiceProxy();
    return centralRegistryClientService.getPassengers();
  }
  
  public int login(java.lang.String name, int entry) throws java.rmi.RemoteException{
    if (centralRegistryClientService == null)
      _initCentralRegistryClientServiceProxy();
    return centralRegistryClientService.login(name, entry);
  }
  
  
}