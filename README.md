proto_mailbox
=============

Java Mailbox Emulation.

#Main goals and Topic

Main goal:
* Learn to use RMI
* Learn distributed environment

Topic
Make a software using Java to show how a RMI architecture works. Make a server and a client version to view, send, receive micro e-mails.

#Functions
* Send a mail between client using servers
* Allow multiple recipient
* Follow mals with timestamp
* Allow to delete and store mails and accounts
* Allow transfe between accounts

#Usage
##Installation
First compile MailboxFactoryServer:
`
$ javac MailboxFactoryServer.java
`
Then, run the server:
`
$ java -Djava.rmi.server.codebase=file://$pwd/ MailboxFactoryServer
`

Make sure RMI is installed and do:
`
$ rmic -keep Mailbox MailboxFactory
$ rmiregistry
`

Once it's done, let's compile the client class like this:
`
$ javac MailboxClient.java
`

##Typical client usage
Create a mailbox:
`
$ java MailboxClient c "mailboxname@servername"
or
$ java MailboxClient c "mailboxname@serverIP"
`
Send a message to user myfriend whose mailbox is on othermailboxserver:
`
$ java MailboxClient s "sender@serverIP" "recipient@servername" "hello"
`
Send a message to multiple user:
`
$ java MailboxClient s "sender@server" "rec1@servername;rec2@servername" "hello people"
`
View message on mailboxserver for id user:
`
$ java MailboxClient v mailboxname@serverIP
`
Delete a mail with id MailID for id user:
`
$ java MailboxClient d mailboxname@serverIP idmail
`
Configure a mail transfert for id user:
`
$ java MailboxClient t initialmailbox@servername finalmailbox@serverIP
`
Erase a Mailbox for id user:
`
$ java MailboxClient e mailboxname@serverIP
`
