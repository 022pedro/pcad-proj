package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import java.io.Serializable;


public interface ClientInt extends Remote,Serializable {
	public void notifyClient() throws RemoteException;
	public void sendMessage(MessageT msg)throws RemoteException;
	public void sendTopicList(Set<String> topics)throws RemoteException;
}
