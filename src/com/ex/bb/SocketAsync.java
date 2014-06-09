package com.ex.bb;

//import java.io.File;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
//import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class SocketAsync extends AsyncTask<Wrapper,Object,Wrapper> {
	
	private Context context;
	public MainActivity m;
	 //   private TextView statusText;

	    public SocketAsync(Context context,MainActivity m) {
	        this.context = context;
	        this.m=m;
	        
	    }

	
    protected Wrapper doInBackground(Wrapper...w) {
    	
    	int port=w[0].port;
    	int len;
    	Socket socket = new Socket();
    	byte buf[]  = new byte[2048];
    	Log.i(MainActivity.TAG, "try");
    	try {
    	    /**
    	     * Create a client socket with the host,
    	     * port, and timeout information.
    	     */
    	    socket.bind(null);
    	    
    	    Log.i(MainActivity.TAG, "bind"+w[0].host+port);
    	//	InetSocketAddress i=new InetSocketAddress(host, port);
    		Log.i(MainActivity.TAG, "connect");
    	    socket.connect(new InetSocketAddress(w[0].host, port), 500000000);}
    	catch (IOException e) {
    		Log.i(MainActivity.TAG, "IO Fucking Exceptionnew");
    		e.printStackTrace();
    	    //catch logic
    	}try{
    	    /**
    	     * Create a byte stream from a JPEG file and pipe it to the output stream
    	     * of the socket. This data will be retrieved by the server device.
    	     */
    	    Log.i(MainActivity.TAG, "file");
    	    OutputStream outputStream = socket.getOutputStream();
    	    Log.i(MainActivity.TAG, "file1");
    	    ContentResolver cr = context.getContentResolver();
    	    Log.i(MainActivity.TAG, "file2");
    	    InputStream inputStream = null;
    	    Log.i(MainActivity.TAG, "+file3");
    	   // File file=new File("test");
    	    Log.i(MainActivity.TAG, "file4"+Environment.getExternalStorageDirectory().toString());
    	    //File f=new File("file:///"+Environment.getExternalStorageDirectory().toString()+"/text1.txt");
    	    Log.i(MainActivity.TAG, "file55");
    	   // m.write(m.read("text.txt"),"file:///"+Environment.getExternalStorageDirectory().toString()+"/text1.txt");
    	    Log.i(MainActivity.TAG, "file56");
    	    Uri uri = Uri.parse("file:///storage/sdcard0/A.jpg");
    	    //Uri uri = Uri.parse("filename");
    	    Log.i(MainActivity.TAG, "file5"+"file://"+Environment.getExternalStorageDirectory().toString()+m.getApplicationInfo().dataDir + "/text.txt");
    	    inputStream = cr.openInputStream(uri);
    	    //InputStream i = null;
    	    //i = new FileInputStream("file://"+Environment.getExternalStorageDirectory().toString()+m.getApplicationInfo().dataDir + "/text.txt");
    	    Log.i(MainActivity.TAG, "file6");
    	    
    	    InputStream stream = new ByteArrayInputStream(m.read("text.txt").getBytes(Charset.forName("UTF-8")));
    	    while ((len = stream.read(buf)) != -1) {
    	        outputStream.write(buf, 0, len);
    	    }
    	    //i.close();
    	    outputStream.close();
    	    inputStream.close();
    	    
    	} catch (FileNotFoundException e) {
    		
    		Log.i(MainActivity.TAG, "File Fucking Exception");
    	    //catch logic
    	} catch (IOException e) {
    		Log.i(MainActivity.TAG, "IO Fucking Exception");
    	    //catch logic
    	} catch (IllegalArgumentException e){
    		Log.i(MainActivity.TAG, "Fuck Fuck Fuck");
    	}
    	  //catch (SocketException a){
    		//  Log.i(MainActivity.TAG, "Socket Fucking Exception");
    	//  }
    	  

    	/**
    	 * Clean up any open sockets when done
    	 * transferring or if an exception occurred.
    	 */
    	finally {Log.i(MainActivity.TAG, "Final");
    	    if (socket != null) {
    	    	Log.i(MainActivity.TAG, "null");
    	        if (socket.isConnected()) {
    	            try {
    	            	
    	                socket.close();
    	                Log.i(MainActivity.TAG, "trys");
    	                
    	            } catch (IOException e) {
    	            	Log.i(MainActivity.TAG, "Fuck Fuck Fuck2");
    	                //catch logic
    	            }
    	        }
    	    }
    	}
    	
    	  

   return w[0];
    }
    protected void onPostExecute(Wrapper w) {
        w.listener.onTaskCompleted(false,"");
     }}
    
    

