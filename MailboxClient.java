import java.rmi.*;


public class MailboxClient {
	public static void usage() {
		System.out.println("Usage: java MailboxClientInnerSecurityManager");
		System.out.println("\t c id@mailboxserver ||");
		System.out.println("\t s id@mailboxserver myfriend@othermailboxserver \"message\" ||");
		System.out.println("\t s id@mailboxserver \"myfriend@othermailboxserver;myotherfriend@othermailboxserver\" \"message\" ||");
		System.out.println("\t v id@mailboxserver ||");
		System.out.println("\t d id@mailboxserver MailID ||");
		System.out.println("\t t id@mailboxserver id@mailtransfer ||");
		System.out.println("\t e id@mailboxserver");
		System.out.println(" /* c: Creating a mailbox on mailboxserver for id user */");
		System.out.println(" /* s: Sending a message to user myfriend whose mailbox is on othermailboxserver */");
		System.out.println(" /* s: Use \";\" for multiple user */");
		System.out.println(" /* v: Viewing message on mailboxserver for id user */");
		System.out.println(" /* d: Delete a mail with id MailID for id user */");
		System.out.println(" /* t: Configure a mail transfert for id user */");
		System.out.println(" /* e: Erase a Mailbox for id user */");
		
		
		System.exit(1);
	}
	
	public static void main (String [] argv) {
		String id;
		String server;
		String url;
		char op;
		
		if(argv.length < 2 || argv[0].length() != 1)
			usage();
		
		op = argv[0].charAt(0);
		
		System.setSecurityManager(new RMISecurityManager() {
			public void checkConnect(String host, int port) {}
			public void checkConnect(String host, int port, Object context) {}
			public void checkAccept(String host, int port) {}
		});
		
		try {
			// System.out.println (hello.sayHello()) ;
			switch(op) {
				case 'c':
					id = argv[1].substring(0, argv[1].indexOf("@"));
					server = argv[1].substring(argv[1].indexOf("@") + 1);
					MailboxFactoryInterface factory =
						(MailboxFactoryInterface) Naming.lookup("rmi://" + server + "/Factory");
					System.out.println(factory.createMailbox(id));
					break;
				case 's':
					if(argv.length < 4)
						usage();
					

					//Get sender ID 
					String expid = argv[1].substring(0, argv[1].indexOf("@"));
					
					//Get server name
					String expserver = argv[1].substring(argv[1].indexOf("@") + 1);
					
					//Send a request to the sender server
					MailboxInterface expMailbox = (MailboxInterface) Naming.lookup("rmi://" + expserver + "/" + expid);
					System.out.println(expMailbox.deliver(argv[1], argv[2], argv[3]));
					
					
					/*String destid = argv[2].substring(0, argv[2].indexOf("@"));
					String destserver = argv[2].substring(argv[2].indexOf("@") + 1);
					MailboxInterface destMailbox =
						(MailboxInterface) Naming.lookup("rmi://" + destserver + "/" + destid);
					System.out.println(destMailbox.deliver(argv[1], argv[3]));*/
					
					
					break;
				case 'v':
					id = argv[1].substring(0, argv[1].indexOf("@"));
					server = argv[1].substring(argv[1].indexOf("@") + 1);
					MailboxInterface mailbox =
						(MailboxInterface) Naming.lookup("rmi://" + server + "/" + id);
					System.out.println(mailbox.check());
					break;
					
				case 'd':
					id = argv[1].substring(0, argv[1].indexOf("@"));
					server = argv[1].substring(argv[1].indexOf("@") + 1);
					MailboxInterface wmailbox =
						(MailboxInterface) Naming.lookup("rmi://" + server + "/" + id);
					System.out.println(wmailbox.delete(argv[2]));
					break;
					
				case 't':
					id = argv[1].substring(0, argv[1].indexOf("@"));
					server = argv[1].substring(argv[1].indexOf("@") + 1);
					MailboxInterface tmailbox =
						(MailboxInterface) Naming.lookup("rmi://" + server + "/" + id);
					System.out.println(tmailbox.transfert(argv[2]));
					break;
				
				case 'e':
					id = argv[1].substring(0, argv[1].indexOf("@"));
					server = argv[1].substring(argv[1].indexOf("@") + 1);
					MailboxFactoryInterface dfactory =
							(MailboxFactoryInterface) Naming.lookup("rmi://" + server + "/Factory");
					System.out.println(dfactory.deleteMailbox(id));
					break;
					
				default:
					usage();
			}
			
		} catch (Exception e) {
			System.out.println ("Error client : " + e) ;
		}
	}
}
