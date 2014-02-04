import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MailboxInterface extends Remote {
	public boolean deliver(String from, String message) throws RemoteException;
	public boolean deliver(String from, String to, String message) throws RemoteException;
	public boolean delete(String id) throws RemoteException;
	public boolean transfert(String mail) throws RemoteException;
	public String check() throws RemoteException;
}
