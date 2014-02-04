import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.security.Permission;

public class MailboxFactoryServer {
	public static void main (String [ ] argv) {
		/* lancer SecurityManager */
		System.setSecurityManager(new RMISecurityManager() {
			public void checkPermission(Permission perm) {}
		});
		
		try {
			Naming.rebind("Factory", new MailboxFactory()) ;
			System.out.println ("Serveur pret.") ;
		} catch (Exception e) { 
			System.out.println("Erreur serveur : " + e) ;
		}
		
		//Used for catch the ctrl-c
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { 
		    	System.out.println("\nFermeture du serveur en cours...\n");
		    	System.out.println("Préparation de la sauvegarde en cours...\n");
				System.out.println("Début de la sauvegarde des donnée\n");
				MailboxFactory.save();
		    	System.out.println("Fin de la sauvegarde des données\n");
		    }
		 });
	}
	
}

