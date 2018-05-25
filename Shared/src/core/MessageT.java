package core;
import java.io.Serializable;

public class MessageT implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String topic;
	private String mess;
	private String from;
	public MessageT(String topic,String mess,String from) {
		this.topic=topic;
		this.mess=mess;
		this.from=from;
		
		
	}

	public String getTopic() {
		return topic;
	}
	public String getMess() {
		return mess;
	}

	public String getFrom() {
		return from;
	}

	public String toString() {
		return "From:"+from+"\nTopic:"+this.topic + "\nMessage:"+this.mess;
		
		
	}
	
}
