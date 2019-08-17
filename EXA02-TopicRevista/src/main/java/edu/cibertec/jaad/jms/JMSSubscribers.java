package edu.cibertec.jaad.jms;

public class JMSSubscribers {

	public static void main(String[] args) {
		int size = 3;
		for(int i = 0; i < size ; i++){
			JMSSubscriberPrestValMonto subs = new JMSSubscriberPrestValMonto("PrestValMonto-"+i);
			subs.start();
		}
		
		for(int i = 0; i < size ; i++){
			JMSSubscriberPrestValSbs subs = new JMSSubscriberPrestValSbs("PrestValSbs-"+i);
			subs.start();
		}
	}

}
