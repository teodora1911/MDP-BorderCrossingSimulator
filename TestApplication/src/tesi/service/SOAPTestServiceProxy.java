package tesi.service;

public class SOAPTestServiceProxy implements tesi.service.SOAPTestService {
  private String _endpoint = null;
  private tesi.service.SOAPTestService sOAPTestService = null;
  
  public SOAPTestServiceProxy() {
    _initSOAPTestServiceProxy();
  }
  
  public SOAPTestServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initSOAPTestServiceProxy();
  }
  
  private void _initSOAPTestServiceProxy() {
    try {
      sOAPTestService = (new tesi.service.SOAPTestServiceServiceLocator()).getSOAPTestService();
      if (sOAPTestService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sOAPTestService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sOAPTestService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sOAPTestService != null)
      ((javax.xml.rpc.Stub)sOAPTestService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public tesi.service.SOAPTestService getSOAPTestService() {
    if (sOAPTestService == null)
      _initSOAPTestServiceProxy();
    return sOAPTestService;
  }
  
  public int passed(java.lang.String passenger) throws java.rmi.RemoteException{
    if (sOAPTestService == null)
      _initSOAPTestServiceProxy();
    return sOAPTestService.passed(passenger);
  }
  
  public java.lang.String checkAvailability(java.lang.String terminalName, int id) throws java.rmi.RemoteException{
    if (sOAPTestService == null)
      _initSOAPTestServiceProxy();
    return sOAPTestService.checkAvailability(terminalName, id);
  }
  
  public void policeStop(java.lang.String terminal, java.lang.String passenger) throws java.rmi.RemoteException{
    if (sOAPTestService == null)
      _initSOAPTestServiceProxy();
    sOAPTestService.policeStop(terminal, passenger);
  }
  
  public void customsStop(java.lang.String terminal, tesi.model.CustomsDocument documents) throws java.rmi.RemoteException{
    if (sOAPTestService == null)
      _initSOAPTestServiceProxy();
    sOAPTestService.customsStop(terminal, documents);
  }
  
  
}