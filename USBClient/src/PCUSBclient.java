import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class PCUSBclient{
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{
	
		Socket echoSocket = null;
		ObjectOutputStream out = null;		
        Scanner console = new Scanner(System.in);
		
		try{
			execAdb();
			echoSocket = new Socket("localhost", 38300); // Create socket connection with host address as localhost and port number with 38300			
			out = new ObjectOutputStream(echoSocket.getOutputStream());
			out.flush();
			
			ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());
			System.out.println("Connection Established");
			(new ReadThread(in)).start(); //we start a new thread for been always reading input from usb
			
			while(true){			
				out.writeObject(console.nextLine());
			}
			
		}		
		catch (IOException e){
			System.err.println("Couldn't get I/O for " + "the connection to: LocalHost:");			
		}
		/*finally{
			// Closing connection
			try{				
				out.close();
				if (echoSocket != null){
					echoSocket.close();
				}
			}
			catch (IOException ioException){
				ioException.printStackTrace();
			}
		}*/
		
	}
	
	private static void execAdb() {
		// run the adb bridge
		try {
			Process p=Runtime.getRuntime().exec("C:\\DiSCLAiMER\\android-sdk\\platform-tools\\adb.exe forward tcp:38300 tcp:38300");
			Scanner sc = new Scanner(p.getErrorStream());
			if (sc.hasNext()) {
				while (sc.hasNext()) System.out.println(sc.next());
					System.err.println("Cannot start the Android debug bridge");
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}
