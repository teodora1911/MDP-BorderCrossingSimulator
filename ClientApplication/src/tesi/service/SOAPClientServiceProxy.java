package tesi.service;

public class SOAPClientServiceProxy implements tesi.service.SOAPClientService {
  private String _endpoint = null;
  private tesi.service.SOAPClientService sOAPClientService = null;
  
  public SOAPClientServiceProxy() {
    _initSOAPClientServiceProxy();
  }
  
  public SOAPClientServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initSOAPClientServiceProxy();
  }
  
  private void _initSOAPClientServiceProxy() {
    try {
      sOAPClientService = (new tesi.service.SOAPClientServiceServiceLocator()).getSOAPClientService();
      if (sOAPClientService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sOAPClientService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sOAPClientService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sOAPClientService != null)
      ((javax.xml.rpc.Stub)sOAPClientService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public tesi.service.SOAPClientService getSOAPClientService() {
    if (sOAPClientService == null)
      _initSOAPClientServiceProxy();
    return sOAPClientService;
  }
  
  public java.lang.String login(java.lang.String terminalName, int id, boolean police) throws java.rmi.RemoteException{
    if (sOAPClientService == null)
      _initSOAPClientServiceProxy();
    return sOAPClientService.login(terminalName, id, police);
  }
  
  public void logout(java.lang.String terminal, boolean police) throws java.rmi.RemoteException{
    if (sOAPClientService == null)
      _initSOAPClientServiceProxy();
    sOAPClientService.logout(terminal, police);
  }
  
  public void stopTerminal(java.lang.String terminalName) throws java.rmi.RemoteException{
    if (sOAPClientService == null)
      _initSOAPClientServiceProxy();
    sOAPClientService.stopTerminal(terminalName);
  }
  
  public void releaseTerminal(java.lang.String terminalName) throws java.rmi.RemoteException{
    if (sOAPClientService == null)
      _initSOAPClientServiceProxy();
    sOAPClientService.releaseTerminal(terminalName);
  }
  
  public java.lang.String getNextPassenger(java.lang.String terminal) throws java.rmi.RemoteException{
    if (sOAPClientService == null)
      _initSOAPClientServiceProxy();
    return sOAPClientService.getNextPassenger(terminal);
  }
  
  public void updatePassenger(java.lang.String passenger, boolean passed) throws java.rmi.RemoteException{
    if (sOAPClientService == null)
      _initSOAPClientServiceProxy();
    sOAPClientService.updatePassenger(passenger, passed);
  }
  
  public tesi.model.CustomsDocument getDocument(java.lang.String terminal) throws java.rmi.RemoteException{
    if (sOAPClientService == null)
      _initSOAPClientServiceProxy();
    return sOAPClientService.getDocument(terminal);
  }
  
  
}