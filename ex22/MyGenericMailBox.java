package test;

import java.util.function.Consumer;

public class MyGenericMailBox extends MailBox{

	
	public MyGenericMailBox(int id, Consumer<String> logic) {
	}

	@Override
	void addMessage(String msg) {
	}

	@Override
	void close() {
	}

}
