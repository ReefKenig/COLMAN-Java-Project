package test;

import java.util.function.Consumer;

public class MyGenericMessenger extends Messenger{

	
	public MyGenericMessenger(int id, Consumer<String> logic) {
	}

	@Override
	void addMessage(String msg) {
	}

	@Override
	void close() {
	}

}
