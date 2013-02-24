import java.io.ObjectInputStream;

public class ReadThread extends Thread {
	
	ObjectInputStream input;
	boolean run=false;
	
	public ReadThread(ObjectInputStream stream){
		input=stream;
		run=true;
	}
	
	public void run(){
		String message = null;
		while(run){			
			try {
				message = (String) input.readObject();
				System.out.println("@phone: "+message);
			} catch (Exception e) {
				System.err.println("Error in input reading thread (ReadThread.java)");
				run=false;
			}		
		}
	}
}
