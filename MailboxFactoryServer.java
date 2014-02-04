import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.security.Permission;

public class MailboxFactoryServer {
	public static void main (String [ ] argv) {
		/* Run SecurityManager */
		System.setSecurityManager(new RMISecurityManager() {
			public void checkPermission(Permission perm) {}
		});
		
		try {
			Naming.rebind("Factory", new MailboxFactory()) ;
			System.out.println ("Server ready.") ;
		} catch (Exception e) { 
			System.out.println("Error : " + e) ;
		}
		
		//Used for catch the ctrl-c
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { 
		    	System.out.println("\nClosing server...\n");
		    	System.out.println("Saving mailboxes..\n");
				System.out.println("Starting saving mailbox data\n");
				MailboxFactory.save();
		    	System.out.println("Mailbox data saved\n");
		    }
		 });
	}
	
}

