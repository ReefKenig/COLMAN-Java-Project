package test;

import java.util.concurrent.ConcurrentHashMap;

public abstract class Messenger {

	private static ConcurrentHashMap<Integer, Messenger> map=new ConcurrentHashMap<>();	

	public static Messenger get(int id) {
		return map.get(id);
	}
	
	
	public Messenger(int id) {
		map.put(id, this);		
	}
	
	abstract void addMessage(String msg);
	
	abstract void close();
}
