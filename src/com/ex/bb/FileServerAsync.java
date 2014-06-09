package com.ex.bb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
//import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
//import android.os.Environment;
import android.util.Log;

public  class FileServerAsync extends AsyncTask<Wrapper, Object, Wrapper> {

    private Context context;
    private MainActivity m;

    public FileServerAsync(Context context,MainActivity m) {
        this.context = context;
        this.m=m;
        
    }
public void copyFile(InputStream in,FileOutputStream out){
    byte[] buffer = new byte[1024];
    int len;
    try {
		while ((len = in.read(buffer)) != -1) {
		    out.write(buffer, 0, len);
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
    @Override
    protected Wrapper doInBackground(Wrapper... w) {
    	Log.i(MainActivity.TAG, "t");
        try {
        	Log.i(MainActivity.TAG, "do in b");
            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
        	
            ServerSocket serverSocket = new ServerSocket(w[0].port);	           
            Log.i(MainActivity.TAG, "serversocket created");
            Socket client = serverSocket.accept();
           
            Log.i(MainActivity.TAG, "serversocket.accept()");
            /**
             * If this code is reached, a client has connected and transferred data
             * Save the input stream from the client as a JPEG file
             */
            Log.i(MainActivity.TAG, "f");
           // final File f = new File("filename1");
           // final File f = new File("filename");
            final File f = new File(Environment.getExternalStorageDirectory() + "/" +System.currentTimeMillis()
                    + ".jpg");
            //File f = new File("file://"+"/storage/sdcard0"+"/"+System.currentTimeMillis()+".jpg");
            Log.i(MainActivity.TAG, "f");
            //Log.i(MainActivity.TAG, "file created"+"file://    "+Environment.getExternalStorageDirectory().toString()+"/text1.txt");
            File dirs = new File(f.getParent());
            Log.i(MainActivity.TAG, "f");
            if (!dirs.exists())
                dirs.mkdirs();
            Log.i(MainActivity.TAG, "f");
            f.createNewFile();
            Log.i(MainActivity.TAG, "f");
            InputStream inputstream = client.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(inputstream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            String dss = total.toString();
            Log.i(MainActivity.TAG, dss);
            Log.i(MainActivity.TAG, "add")
            ;
            Log.i(MainActivity.TAG,"dss "+ dss);
            Log.i(MainActivity.TAG, "streams");
            serverSocket.close();
            
           // Wrapper ww=new Wrapper(w[0].listener,w[0].host,8889);
            //if(w[0].port == 8888){ new SocketAsync(context).execute(ww);}
            //Wrapper ww= new Wrapper(w[0].listener,dss,w[0].port);
            w[0].host = dss;
            return w[0];
            
        } catch (IOException e) {
           // Log.e(WiFiDirectActivity.TAG, e.getMessage());
            return null;
        }
    }
    protected void onPostExecute(Wrapper w) {
    	w.listener.onTaskCompleted(true,w.host);
     }
}
