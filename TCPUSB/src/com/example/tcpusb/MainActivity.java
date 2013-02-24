package com.example.tcpusb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Handler mHandler = null;
	private ServerSocket server = null;
	public static final int TIMEOUT = 10;
	private Socket client = null;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private String message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mHandler = new Handler();
		new Thread(initializeConnection).start();
		String msg = "Attempting to connect";
		Toast.makeText(MainActivity.this, msg, msg.length()).show();
	}
	
	private final Runnable initializeConnection = new Thread(){
		
		@Override
		public void run(){
			// initialize server socket
			try{
				server = new ServerSocket(38300);
				server.setSoTimeout(MainActivity.TIMEOUT * 1000);
				
				//attempt to accept a connection
				client = server.accept();				
				
				outStream = new ObjectOutputStream(client.getOutputStream());
				inStream = new ObjectInputStream(client.getInputStream());			
				
				outStream.writeObject("Hello from the phone");			
					
				while (true){						
					message = (String) inStream.readObject();
					mHandler.post(printMessage);						
				}
			}			
			catch (Exception e)
			{
				//connectionStatus = "Connection has timed out! Please try again";
				//mHandler.post(showConnectionStatus);
			}		
		}
	};
	
	public void sendButton(View view) {
		EditText text = (EditText) this.findViewById(R.id.editText1);;
		try {			
			outStream.writeObject(text.getText().toString());
			text.setText("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private final Runnable printMessage = new Runnable(){
		@Override
		public void run(){
			TextView e = (TextView) findViewById(R.id.textView1);			
			e.setText(message.toString());
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
