import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import java.util.Date;

public class Mailbox extends UnicastRemoteObject implements MailboxInterface {
	String name;
	//String mails;
	//List of received mails
	List<Mail> inbox;
	//List of sent mails
	List<Mail> sent;
	//Used for permanent transfer
	String transfer;
	
	public Mailbox(String n) throws RemoteException {
		name = n;
		inbox = new Vector<Mail>();
		sent = new Vector<Mail>();
		transfer = null;
	}
	
	public boolean deliver(String from, String message) throws RemoteException {
		System.out.println ("Mailbox: "+ this.name + ": Reception d'un nouveau mail de "+ from);
		/*if(mails == null)
			mails = from + ":" + message;
		else
			mails = mails + "\n" + from + ":" + message;*/
		/*Mise en place du transfert permanent avec copie du message*/
		if(transfer != null)
		{
			try
			{
				System.out.println ("Mailbox: "+ this.name + ": Transfert du mail vers "+ transfer) ;
				MailboxInterface transferMailbox;
				String transid;
				String transerver;
				
				//Transfer to this.transfer mail
				transid = transfer.substring(0, transfer.indexOf("@"));
				transerver = transfer.substring(transfer.indexOf("@") + 1);
				transferMailbox = (MailboxInterface) Naming.lookup("rmi://" + transerver + "/" + transid);
				transferMailbox.deliver(from, message);
			}
			catch (Exception e) {
				System.out.println ("Mailbox: "+ this.name + ": Erreur client : " + e) ;
			}
				//Used for Transfer mail flag
				message = "TR:" + message;
		}
		
		//Copy of the transfered mail
		this.inbox.add(new Mail(new Date(), from, message));
		return true;	
	}
	
	
	
	/**********Ajout du code ***************/
	/*Here, deliver function is used for mail transfer between the mail server of the sender
	 * and the mail server of the recipient.
	 * This function is called by the Sender Client et runned by the server of the sender
	 */
	public boolean deliver(String from, String to, String message) throws RemoteException {
		String destid;
		String destserver;
		MailboxInterface destMailbox;
		
		try
		{
			System.out.println ("Mailbox: "+ this.name + ": Envoi d'un mail de "+ from) ;
			//Used for multiple recipient, can be changed to modify the separator
			String [] destinataires = to.split(";");
			
			//For each recipient in the args
			for (int i=0; i<destinataires.length; i++)
			{
				System.out.println ("Mailbox: "+ this.name + ": Tentative d'envoi de mail vers "+ destinataires[i]);
				
				//It collects the recipient
				destid = destinataires[i].substring(0, destinataires[i].indexOf("@"));
				destserver = destinataires[i].substring(destinataires[i].indexOf("@") + 1);
				//It try to get the recipient server
				destMailbox = (MailboxInterface) Naming.lookup("rmi://" + destserver + "/" + destid);
				
				//It deliver the mail on the recipient server
				if (destMailbox.deliver(from, message))
				{
					System.out.println("Mailbox: "+ this.name + ": Envoi de mail vers " + destinataires[i] + ": Reussi");
				}
				else
				{
					System.out.println("Mailbox: "+ this.name + ": Envoi de mail vers " + destinataires[i] + ": Echoue");
				}
				//Finally, it save the mail in the sentbox
				this.sent.add(new Mail(new Date(), destinataires[i], message));
			}
			return true;
			//Ex code
			/*System.out.println ("Mailbox: "+ this.name + ": Tentative de transfert de mail vers "+ to) ;
			String destid = to.substring(0, to.indexOf("@"));
			String destserver = to.substring(to.indexOf("@") + 1);
			MailboxInterface destMailbox =
				(MailboxInterface) Naming.lookup("rmi://" + destserver + "/" + destid);
			
			if (destMailbox.deliver(from, message))
			{
				System.out.println("Mailbox: "+ this.name + ": Transfert de mail vers " + to + ": Reussi");
				return true;
			}
			System.out.println("Mailbox: "+ this.name + ": Transfert de mail vers " + to + ": Echoue");
			return false;*/
			
		}
		catch (Exception e) {
			System.out.println ("Mailbox: "+ this.name + ": Erreur client : " + e) ;
			return false;
		}
	}
	
	public String check() throws RemoteException {
		/*
		 * This function is used for display user id mails
		 * Here, we assume that a mail id is : 's+index of the mail' for a sent mail
		 * and 'i+index of the mail for a inbox mail
		 */
		//return mails;
		System.out.println("Ouverture de la MailBox "+ this.name);
		String mails = "########################## MailBox de "+ this.name + " ##########################\n";
		mails = mails + "------------------------------------ Inbox -------------------------------------";
		Mail temp;
		//Only used for display a specific date format
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		//It parses the inbox and displays the contained mails
		for (int i=0; i<this.inbox.size(); i++)
		{
			temp = (Mail)((Vector<Mail>) this.inbox).elementAt(i);
			mails=mails + "\n#idMail: i"+ i + " >>> The "+ dateFormat.format(temp.getDate()) 
					+ " From " + temp.getCorrespondant()
					+ ": " + temp.getMessage();
		}
		mails = mails + "\n------------------------------------ Sent -------------------------------------";
		
		//It parses the sentbox and displays the contained mails
		for (int i=0; i<this.sent.size(); i++)
		{
			temp = (Mail)((Vector<Mail>) this.sent).elementAt(i);
			mails=mails + "\n#idMail: s"+ i + " >>> The "+ dateFormat.format(temp.getDate()) 
					+ " To " + temp.getCorrespondant()
					+ ": " + temp.getMessage();
		}
		return mails;
	}
	
	public boolean delete(String id) throws RemoteException
	{
		System.out.println("Suppression du mail "+id+" de la MailBox "+ this.name);
		if(id.charAt(0) == 'i')
		{
			try {
				inbox.remove(Integer.parseInt(id.substring(1)));
				return true;
			}
			catch (Exception e) {
				System.out.println ("Mailbox: "+ this.name + ": Erreur suppression : " + e) ;
				return false;
			} 
		}
		else if (id.charAt(0) == 's')
		{
			try {
				sent.remove(Integer.parseInt(id.substring(1)));
				return true;
			}
			catch (Exception e) {
				System.out.println ("Mailbox: "+ this.name + ": Erreur suppression : " + e) ;
				return false;
			} 	
		}
		else
		return false;	
	}
	
	public boolean transfert(String mail) throws RemoteException
	{
		this.transfer = mail;
		return true;
	}
	
	public String getName()
	{
		return this.name;
	}
}









