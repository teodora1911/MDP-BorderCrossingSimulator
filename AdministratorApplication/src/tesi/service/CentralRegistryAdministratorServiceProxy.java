package tesi.service;

public class CentralRegistryAdministratorServiceProxy implements tesi.service.CentralRegistryAdministratorService {
  private String _endpoint = null;
  private tesi.service.CentralRegistryAdministratorService centralRegistryAdministratorService = null;
  
  public CentralRegistryAdministratorServiceProxy() {
    _initCentralRegistryAdministratorServiceProxy();
  }
  
  public CentralRegistryAdministratorServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCentralRegistryAdministratorServiceProxy();
  }
  
  private void _initCentralRegistryAdministratorServiceProxy() {
    try {
      centralRegistryAdministratorService = (new tesi.service.CentralRegistryAdministratorServiceServiceLocator()).getCentralRegistryAdministratorService();
      if (centralRegistryAdministratorService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)centralRegistryAdministratorService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)centralRegistryAdministratorService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (centralRegistryAdministratorService != null)
      ((javax.xml.rpc.Stub)centralRegistryAdministratorService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public tesi.service.CentralRegistryAdministratorService getCentralRegistryAdministratorService() {
    if (centralRegistryAdministratorService == null)
      _initCentralRegistryAdministratorServiceProxy();
    return centralRegistryAdministratorService;
  }
  
  public tesi.model.Terminal getTerminal(java.lang.String name) throws java.rmi.RemoteException{
    if (centralRegistryAdministratorService == null)
      _initCentralRegistryAdministratorServiceProxy();
    return centralRegistryAdministratorService.getTerminal(name);
  }
  
  public boolean deleteTerminal(java.lang.String name) throws java.rmi.RemoteException{
    if (centralRegistryAdministratorService == null)
      _initCentralRegistryAdministratorServiceProxy();
    return centralRegistryAdministratorService.deleteTerminal(name);
  }
  
  public boolean addTerminal(tesi.model.Terminal terminal) throws java.rmi.RemoteException{
    if (centralRegistryAdministratorService == null)
      _initCentralRegistryAdministratorServiceProxy();
    return centralRegistryAdministratorService.addTerminal(terminal);
  }
  
  public boolean updateTerminal(tesi.model.Terminal terminal) throws java.rmi.RemoteException{
    if (centralRegistryAdministratorService == null)
      _initCentralRegistryAdministratorServiceProxy();
    return centralRegistryAdministratorService.updateTerminal(terminal);
  }
  
  public java.lang.String[] getPassengers() throws java.rmi.RemoteException{
    if (centralRegistryAdministratorService == null)
      _initCentralRegistryAdministratorServiceProxy();
    return centralRegistryAdministratorService.getPassengers();
  }
  
  
}