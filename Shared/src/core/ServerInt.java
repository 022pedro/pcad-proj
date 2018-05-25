package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import java.io.Serializable;

public interface ServerInt extends Remote,Serializable,ClientInt {
	public void request(String x,ClientInt stub) throws RemoteException;
	public void subscribe(String topic, String name)throws RemoteException;
	public void publish(MessageT msg)throws RemoteException;
	public void getTopicList(String client)throws RemoteException;
	public Set<String> getTopicList()throws RemoteException;
	
	
	
	
}

