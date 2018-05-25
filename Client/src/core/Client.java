package core;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import core.ServerInt;
import core.ClientInt;

public class Client implements ClientInt {
   
	private static final long serialVersionUID = 1L;
    private ClientInt stub;
    private ServerInt server;
    private Scanner sc;
    private String topic,message;
    private MessageT msg;
    private List<MessageT> messages;
    private ConcurrentHashMap<String,List<MessageT>> topicMess;
   
	public Client(String x) {
		try {
		System.setProperty("java.security.policy","file:./sec.policy");
		System.setProperty("java.rmi.server.codebase","file:${workspace_loc}/Client/");
		if (System.getSecurityManager() == null) System.setSecurityManager(new SecurityManager());
		System.setProperty("java.rmi.server.hostname","localhost");
		//Registry r = LocateRegistry.getRegistry("localhost",8000);
		Registry r = LocateRegistry.getRegistry(8000);
		server = (ServerInt) r.lookup("REG");
		stub = (ClientInt) UnicastRemoteObject.exportObject(this,0);
		server.request(x,stub);
		sc=new Scanner(System.in);
		messages=new LinkedList<MessageT>();
		topicMess=new ConcurrentHashMap<>();

		while(true) {
			menu();
			topic=sc.nextLine();
			switch(topic) {
			case("1"):
				System.out.println(server.getTopicList());
				break;
			case("2"):
				System.out.println("inserisci topic");
				topic=sc.nextLine();
				System.out.println("inserisci post");
				message=sc.nextLine();
				msg=new MessageT(topic,message,x);
				server.publish(msg);
				break;
			
			case("3"):
				System.out.println("Insert Topic to subscribe");
				topic=sc.nextLine();
				server.subscribe(topic, x);
				break;
			case("4"):
				System.out.println(messages);
				break;
			case("5"):
				topic=sc.nextLine();
				System.out.println(messages);
				break;
			default:break;
			}
			System.out.println("Press enter to continue");
			sc.nextLine();
		}
		
		
		} catch (RemoteException | NotBoundException e) {
		e.printStackTrace();
		}
	}
	private void menu() {
		System.out.println("1)Get Topics");
		System.out.println("2)Publish");
		System.out.println("3)Subscribe");
		System.out.println("4)See all posts");	
		System.out.println("5)See posts from topic");
	}
		
	public void notifyClient() throws RemoteException {
		System.out.println("hand-shake ok!");
	}
	
	public static void main(String args[]) {
		int x = (int) (Math.random() * 1000);
		String s = Integer.toString(x);
		System.out.println("Client "+s);
		new Client(s) ;
	  }


	@Override
	public void sendMessage(MessageT msg) throws RemoteException {
		// TODO Auto-generated method stub
		messages.add(msg);
		topicMess.putIfAbsent(msg.getTopic(), new LinkedList<MessageT>());
		List<MessageT> aux=topicMess.get(msg.getTopic());
		synchronized(aux) {
			aux.add(msg);
		}
	}

	@Override
	public void sendTopicList(Set<String> topics) throws RemoteException {
		// TODO Auto-generated method stub
		
		
	}





	
}

