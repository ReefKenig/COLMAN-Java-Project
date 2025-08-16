package test;

import java.util.concurrent.ConcurrentHashMap;

public abstract class MailBox {

	private static ConcurrentHashMap<Integer, MailBox> map=new ConcurrentHashMap<>();	

	public static MailBox get(int id) {
		return map.get(id);
	}
	
	
	public MailBox(int id) {
		map.put(id, this);		
	}
	
	abstract void addMessage(String msg);
	
	abstract void close();
}
