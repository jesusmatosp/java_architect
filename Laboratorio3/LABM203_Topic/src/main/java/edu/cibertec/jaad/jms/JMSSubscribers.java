package edu.cibertec.jaad.jms;

public class JMSSubscribers {

	public static void main(String[] args) {
		int size = 3;
		for(int i = 0; i < size ; i++){
			JMSSubscriber subs = new JMSSubscriber("SUBS-"+i);
			subs.start();
		}
	}

}
