package core;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import core.ServerInt;
import core.ClientInt;

public class Server implements ServerInt {
  private static final long serialVersionUID = 1L;
  private static Registry r;
  private AbstractMap<String,ClientInt> connClients =new ConcurrentHashMap<>();
  private AbstractMap<String, List<ClientInt>> topC = new ConcurrentHashMap<>();
  
  public Server() {
	  System.out.println("ciao");
  }
  
  public synchronized void request(String x,ClientInt stub) throws RemoteException {
	  System.out.println("Request da "+x);
	  try {
		r.bind(x, stub);
	} catch (AlreadyBoundException e) {
	}
	  try {
		connClients.putIfAbsent(x,(ClientInt)r.lookup(x));
	} catch (NotBoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  stub.notifyClient();
  }
  
  public static void main(String args[]) {
		try {
			System.setProperty("java.security.policy","file:./sec.policy");
			System.setProperty("java.rmi.server.codebase","file:${workspace_loc}/Server/");
			if(System.getSecurityManager() == null) System.setSecurityManager(new SecurityManager());
			System.setProperty("java.rmi.server.hostname","localhost");
			r = null;
			try {
				r = LocateRegistry.createRegistry(8000);
			} catch (RemoteException e) {
				r = LocateRegistry.getRegistry(8000);
			}
			System.out.println("Registro trovato");
			Server server = (Server) new Server();
			ServerInt stubRequest = (ServerInt) UnicastRemoteObject.exportObject(server,0);
			r.rebind("REG", stubRequest);
		    System.out.println("Tutto ok");
		    
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	 }

@Override
public void subscribe(String topic, String name) throws RemoteException {
	// TODO Auto-generated method stub
	try {/*
		synchronized(topC) {
			if (true) //fai riga dopo solo se topic e name ci sono già
				topC.get(topic).add(new ClientDetails(name, (ClientInt)r.lookup(name)));
			else //inserisci new topic, name
		}*/
		ClientInt cd =(ClientInt)r.lookup(name);
		List<ClientInt> clients = topC.get(topic);
		synchronized(clients) {
			if (clients != null && !clients.contains(cd))
				clients.add(cd);
		}
	} catch (NotBoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

@Override
public void sendMessage(MessageT msg) throws RemoteException {
	// TODO Auto-generated method stub
	
}

@Override
public void publish(MessageT msg) throws RemoteException {
	// TODO Auto-generated method stub
	//check se msg.topic in topC.getkeys()
	if (!topC.containsKey(msg.getTopic())) {
		//create topic
		topC.put(msg.getTopic(), new LinkedList<ClientInt>());
	}
	for(ClientInt cl:topC.get(msg.getTopic())) {
		cl.sendMessage(msg);
		
		
	}
	
}

@Override
public void notifyClient() throws RemoteException {
	// TODO Auto-generated method stub
	System.out.println("hand-shake ok!");
}

@Override
public void sendTopicList(Set<String> topics) throws RemoteException {
	// TODO Auto-generated method stub
	
}

@Override
public void getTopicList(String client) throws RemoteException {
	// TODO Auto-generated method stub
	connClients.get(client).sendTopicList(topC.keySet());
}

@Override
public Set<String> getTopicList() throws RemoteException {
	// TODO Auto-generated method stub
	return topC.keySet();
}


  
}