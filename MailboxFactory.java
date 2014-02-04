import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;

import java.util.HashMap;
import java.util.Map.Entry;

public class MailboxFactory extends UnicastRemoteObject implements MailboxFactoryInterface {
	
	//Use for persistence, this is a static variable
	public static HashMap<String,Mailbox> mailboxesstore;
	
	public MailboxFactory() throws RemoteException {
		mailboxesstore = new HashMap<String,Mailbox>();
		//It restores all mailboxes
		this.restore();
	}
	
	public boolean createMailbox(String name) throws RemoteException {	
		//It should contain unique Mailbox ID
		if (mailboxesstore.containsKey(name))
			return false;
		
		try {
			Mailbox m = new Mailbox(name);
			Naming.rebind(name, m);
			System.out.println("Creating MailBox : " + name);
			//It saves the mailbox object
			mailboxesstore.put(name, m);
			return true;
		} catch(MalformedURLException e) {
			System.out.println(e);	
		}
		return false;
	}

	public boolean deleteMailbox(String name) throws RemoteException
	{
		try {
			//It remove the object from naming
			Naming.unbind(name);
			System.out.println("Deleting de la MailBox : " + name);
			//It removes the object from the available mailboxes
			mailboxesstore.remove(name);
			//It deletes the mailbox backup from the disk
			File del = new File("stored/"+name+".smbox");
			del.delete(); 
			return true;
		} catch(MalformedURLException e) {
			System.out.println(e);	
			return false;
		} catch (NotBoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Use for persitence
	public static void save()
	{
		
		//For each Mailbox it have been created
		for(Entry<String, Mailbox> entry : mailboxesstore.entrySet()) {
			System.out.println("Save Mailbox : " + (String) entry.getKey());
			try {
				//It opens a file to write
				//Here,we assume that the folder to save is stored and extension of the file is .smbox
				FileOutputStream f = new FileOutputStream("stored/" + (String) entry.getKey() + ".smbox");
				ObjectOutputStream oos = new ObjectOutputStream(f);
				oos.writeObject((Mailbox) entry.getValue());
				oos.flush();//Erase the cache
				oos.close();
				System.out.println("Successfully saved");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//Use for re-init
	private void restore() {
		try {
			
			//It parses the folder
			File storedfolder = new File("stored");
			String[] storedfile = storedfolder.list();
			//For each file in the folder
			for (int i = 0; i < storedfile.length; i++) 
			{
				//It verifies if the file have a same extension
				if(storedfile[i].endsWith(".smbox")==false)
					continue;

				FileInputStream fichier = new FileInputStream("stored/" + storedfile[i]);
				ObjectInputStream ois = new ObjectInputStream(fichier);
				Mailbox m = (Mailbox) ois.readObject();
				//It update the naming and the mailboxesstore
				Naming.rebind(m.getName(), m);
				mailboxesstore.put(m.getName(), m);
				System.out.println("Recovering Mailbox : "+m.getName());
			}
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}









