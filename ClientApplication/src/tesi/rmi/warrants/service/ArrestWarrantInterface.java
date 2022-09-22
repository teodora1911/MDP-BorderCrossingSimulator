package tesi.rmi.warrants.service;

import java.io.IOException;
import java.rmi.Remote;

public interface ArrestWarrantInterface extends Remote {

    public boolean hasArrestWarrant(String passenger) throws IOException;
}
