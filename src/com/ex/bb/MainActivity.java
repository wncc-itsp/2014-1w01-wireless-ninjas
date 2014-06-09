package com.ex.bb;

//import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
//import java.util.List;
import java.util.Map;

import android.app.Activity;
//import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
//import android.os.Build;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class MainActivity extends Activity implements ConnectionInfoListener,OnTaskCompleted ,OnClickListener {
	public final static String EXTRA_MESSAGE = "com.ex.bb.MESSAGE";
	public static final String TAG = "wifidirectdemo";
	public static final String TAG1 = "file";
	
    private WifiP2pManager manager;
    public boolean connected=true;
    public DnsSdTxtRecordListener txtListener;
    public DnsSdServiceResponseListener servListener;
    //private boolean isWifiP2pEnabled = false;
    final public HashMap<String, String> buddies = new HashMap<String, String>();
    private final IntentFilter intentFilter = new IntentFilter();
    WifiP2pDnsSdServiceRequest serviceRequest;
    WifiManager wifimanager;
    private Channel channel;
    private BroadcastReceiver receiver = null;
    ProgressDialog progressDialog = null;
    View mContentView = null;
    public String name;
    WifiP2pDnsSdServiceInfo serviceInfo;
    public String fileSize;
    public String hashValue;
    //private WifiP2pDevice device;
    final int FOR_NAME = 1;
    //*****
	 // final static String TAG="MAINACTIVITY";
	  EditText ET1;
	  Button B1;
	  TextView TV1;
	  char c=255;
	  String end=""+c;
	  String s="";
	  MenuItem new_game,help;
	  ScrollView SV;
	  String username;
    TextView[] tx = new TextView[100];
	public int q=0;
	public LinearLayout  LL;
//	File f=new File("text1.txt");
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	//	delete();
	 clear();
		Log.i("QWERTY",read("text.txt"));
        //LL = (LinearLayout) findViewById(R.id.LL);

		Log.i(MainActivity.TAG, "Disconnected from device"+correctread(read("text.txt")));
		//SV=(ScrollView)findViewById(R.id.SV1);
	    addtext(correctread(read("text.txt")));
	    Log.i(MainActivity.TAG, "D");
	    // find the elements
	    ET1 = (EditText) findViewById(R.id.ET1);
	    B1 = (Button) findViewById(R.id.B1);
	    Log.i(MainActivity.TAG, "Di");
	    // set a listenerLog.i(MainActivity.TAG, "Disconnected from device");
	    Log.i(MainActivity.TAG, "Disconnected from device");
	    B1.setOnClickListener((OnClickListener) this);
	    Log.i(MainActivity.TAG, "Disconnected from device1");
	 /*   B1.setOnLongClickListener(new View.OnLongClickListener() {
		    @Override
		    public boolean onLongClick(View v){
			    delete();
			    addtext("");
		        return true;
		    }
		});*/
	    connected=false;
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		wifimanager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE); 
		wifimanager.setWifiEnabled(true);
		Log.i(MainActivity.TAG,"cre2");
		channel = manager.initialize(this, getMainLooper(), null);
        Log.i(MainActivity.TAG,"on");
        hashValue = getMD5EncryptedString(read("text.txt")).substring(1,7);
		fileSize = Integer.toString(read("text.txt").length());
		Log.i(MainActivity.TAG,fileSize+hashValue);
        //stopService();
        //stopDiscovery();
		register();
		discoverService();
		serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(!sharedPrefs.contains("name")){
			giveName();
			sharedPrefs.edit().putString("name", name).commit();
		}
		name = sharedPrefs.getString("name", "");
	}
	 @Override
	    public void onResume() {
	        super.onResume();
	        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
	        registerReceiver(receiver, intentFilter);
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        unregisterReceiver(receiver);
	    }
	    
	 public void register(){
		 Map<String, String> record = new HashMap<String, String>();
	        record.put("buddyname", "Stud Sourabh9");
	        record.put("hashValue", hashValue);
	        record.put("fileSize", fileSize);
	        Log.i(MainActivity.TAG,"Broadcasted");
	        Log.i(MainActivity.TAG,"123"+hashValue+fileSize);
	    //    Log.i(MainActivity.TAG,fileSize);
	        
	        WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance("WIFI_CHAT", "GROUP_NAME", record);
	        manager.addLocalService(channel, service, new ActionListener() {
	            @Override
	            public void onSuccess() {
	                //appendStatus("Added Local Service");
	            	Log.i(TAG, "Registered");
	            }
	            @Override
	            public void onFailure(int error) {
	            	Log.i(TAG, "Fail(Register)");
	            	register();
	                //appendStatus("Failed to add a service");
	            }
	        });
	        
	    }
	 public void stopService(){
		 manager.removeServiceRequest(channel, serviceRequest,
	                new ActionListener() {
	                    @Override
	                    public void onSuccess() {
	                    	Log.i(TAG, "Removed Discovery");
	                        //appendStatus("Added service discovery request");
	                    }
	                    @Override
	                    public void onFailure(int arg0) {
	                    	Log.i(TAG, "Unable to remove Discovery");
	                        //appendStatus("Failed adding service discovery request");
	                    }
	                });
	 }
	 public void stopDiscovery(){
		 manager.clearLocalServices(channel,
         		new ActionListener() {
             @Override
             public void onSuccess() {
             	Log.i(TAG, "Removed Service");
                 //appendStatus("Added service discovery request");
             }
             @Override
             public void onFailure(int arg0) {
             	Log.i(TAG, "Unable to remove service");
                 //appendStatus("Failed adding service discovery request");
             }
         });
	 }
	 public void onTaskCompleted(boolean i,String dss){
		 disconnect(i,dss);
	 }
	 
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == FOR_NAME) {

		     if(resultCode == RESULT_OK){ 
		         name=data.getStringExtra("result");          
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
	 }

	 
	 public void giveName(){
		 Intent intent = new Intent(this, YourName.class);
		 startActivityForResult(intent,FOR_NAME);
	 }
	 
	    public void discoverService() {
	        /*
	         * Register listeners for DNS-SD services. These are callbacks invoked
	         * by the system when a service is actually discovered.
	         */
	        manager.setDnsSdResponseListeners(channel,
	                new DnsSdServiceResponseListener() {
	                    @Override
	                    public void onDnsSdServiceAvailable(String instanceName,
	                            String registrationType, WifiP2pDevice srcDevice) {
	                        // A service has` been discovered. Is this our app?
	                    	Log.i(TAG, "Wh1"+hashValue+		getMD5EncryptedString(read("text.txt")));
	                    }
	                }, new DnsSdTxtRecordListener() {
	                    /**
	                     * A new TXT record is available. Pick up the advertised
	                     * buddy name.
	                     */
	                	
	                    @Override
	                    public void onDnsSdTxtRecordAvailable(
	                            String fullDomainName, Map<String, String> record,
	                            WifiP2pDevice device) {Log.i(TAG, "AAA"+hashValue+record.get("hashValue")+connected);
	                    	if(!connected && !hashValue.equals(record.get("hashValue"))){
	                    		connected = true;
	                    		Log.i(TAG, "Whoooooooooo");
	                    		Log.i(TAG,record.get("buddyname"));	
	                    		
	                    	
	                        WifiP2pConfig config = new WifiP2pConfig();
	                        Log.i(TAG,"1");
	                        config.deviceAddress = device.deviceAddress;
	                        Log.i(TAG,"2");
	                        config.wps.setup = WpsInfo.PBC;
	                        Log.i(TAG,"3");
	                        config.groupOwnerIntent=0;
	                        Log.i(TAG,"4");
	                        int ourFileSize = Integer.parseInt(fileSize);
	                        Log.i(TAG,"lllll");
	                        int receivedFileSize = Integer.parseInt(record.get("fileSize"));
	                        Log.i(TAG,"lllll"+ourFileSize+"kk"+receivedFileSize);
	                        if(ourFileSize < receivedFileSize){
	                        	Log.i(TAG,"mmmmmmmmmmmmm");
	                        	config.groupOwnerIntent = 15;
	                        }
	                        //netId = manager.getConnectionInfo();
	                        Log.i(TAG,"lllll");
	                        manager.connect(channel, config, new ActionListener() {

	                            @Override
	                            public void onSuccess() {
	                            	Log.i(TAG,"lllll");
	                                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
	                            }

	                            @Override
	                            public void onFailure(int reason) {
	                            }
	                        });
	                      }
	                    } });
	        // After attaching listeners, create a service request and initiate
	        // discovery.
	        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
	        manager.addServiceRequest(channel, serviceRequest,
	                new ActionListener() {
	                    @Override
	                    public void onSuccess() {
	                    	Log.i(TAG, "Service request added");
	                        //appendStatus("Added service discovery request");
	                    }
	                    @Override
	                    public void onFailure(int arg0) {
	                    	Log.i(TAG, "fail(service request)");
	                        //appendStatus("Failed adding service discovery request");
	                    }
	                });
	        manager.discoverServices(channel, new ActionListener() {
	            @Override
	            public void onSuccess() {
	            	Log.i(TAG, "Discover(man)(succ)");
	                //appendStatus("Service discovery initiated");
	            }
	            @Override
	            public void onFailure(int arg0) {
	            	c();
	            	Log.i(TAG, "Discover(man)(Fail)");
	                //appendStatus("Service discovery failed");
	            }
	        });
	    }
	    public void c(){
	    	manager.discoverServices(channel, new ActionListener() {
	            @Override
	            public void onSuccess() {
	            	Log.i(TAG, "Discover(man)(succ)");
	                //appendStatus("Service discovery initiated");
	            }
	            @Override
	            public void onFailure(int arg0) {
	            	c();
	            	Log.i(TAG, "Discover(man)(Fail)");
	                //appendStatus("Service discovery failed");
	            }
	        });
	    }
	 @Override
	    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
	        String host = info.groupOwnerAddress.getHostAddress();
	        connected = true;
	        stopService();
	        stopDiscovery();
	        if (info.groupFormed && info.isGroupOwner) {
	        	Log.i(MainActivity.TAG, "Group owner");
	        	Wrapper w=new Wrapper(this,host,8888);
	        	new FileServerAsync(this,this).execute(w);
	        	Log.i(MainActivity.TAG, "FSA");
	            // Do whatever tasks are specific
	        	//po,to the group owner.
	            // One common case is creating a server thread and accepting
	            //+ incoming connections.
	        } else if (info.groupFormed) {
	        	Log.i(MainActivity.TAG, "slave");
	        	Wrapper ww=new Wrapper(this,host,8888);
	        	new SocketAsync(this,this).execute(ww);
	            // The other device acts as the client. In this case,
	            // you'll want to create a client thread that connects to the group
	            // owner.
	        }
	        
	    }
	 public static String getMD5EncryptedString(String encTarget){
	        MessageDigest mdEnc = null;
	        try {
	            mdEnc = MessageDigest.getInstance("MD5");
	        } catch (NoSuchAlgorithmException e) {
	            System.out.println("Exception while encrypting to md5");
	            e.printStackTrace();
	        } // Encryption algorithm
	        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
	        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
	        while ( md5.length() < 32 ) {
	            md5 = "0"+md5;
	        }
	        return md5;
	    }
	 
	public void disconnect(boolean i,String dss){
		Log.i(MainActivity.TAG, "d");
		//int gg = wifimanager.getConnectionInfo().getNetworkId();
		if(i){
			
		manager.removeGroup(channel,new ActionListener(){
        	@Override 
			public void onSuccess() { 
				Log.i(MainActivity.TAG, "Disconnected from device");
			} 
			 
			@Override 
			public void onFailure(int reason) {
				Log.i(MainActivity.TAG, "Couldn't disconnect from device");
				//disconnect();
			}
			
		});
		Log.i(MainActivity.TAG, "dd");
		//+wifimanager.removeNetwork(gg);
		Log.i(MainActivity.TAG, "ddd");
		write(dss,"text1.txt");
        Log.i(MainActivity.TAG, "write"+dss);
        
        String s=union("text.txt", "text1.txt");
        Log.i(MainActivity.TAG, "write"+dss);
        clear();
        Log.i(MainActivity.TAG, "write"+dss);
        write(s,"text.txt");
        Log.i(MainActivity.TAG,read("text.txt"));
        Log.i(MainActivity.TAG, "union");
        addtext(correctread(read("text.txt")));
        Log.i(MainActivity.TAG, "at");
		}
		connected = false;
		Log.i(MainActivity.TAG,getMD5EncryptedString(read("text.txt")));
		hashValue = getMD5EncryptedString(read("text.txt")).substring(1,7);
		fileSize = Integer.toString(read("text.txt").length());
		//register();
		//discoverService();
		
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	public void addtext(String s){
		Log.i(MainActivity.TAG, "clear");
		LL = (LinearLayout) findViewById(R.id.LL);
		SV=(ScrollView)findViewById(R.id.SV1);
		Log.i(MainActivity.TAG, "clear");
       LL.removeAllViewsInLayout();
		Log.i(MainActivity.TAG, "clear1");
          for (int i = 0; i < 100;i++) {
        	  if(s.length()==0)break;
        	  int k;
        	  for(k=0;k<s.length();k++){
        		  if(s.charAt(k)==255)
        		  break;
        	  }
	    		Log.i(MainActivity.TAG, "clear2");
        	  String sub=s.substring(0,k);
              tx[i] = new TextView(this);
            tx[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
          tx[i].setText(sub);
       tx[i].setBackgroundResource(R.drawable.images);
         tx[i].getCompoundPaddingRight();
         tx[i].getCompoundPaddingTop();
       //  tx[i].setTextSize(getResources().getDimension(R.dimen.textsize));
         tx[i].setId(i);
         tx[i].setOnLongClickListener(new TextView.OnLongClickListener() {
 		    @Override
 		    public boolean onLongClick(View v){
	    		Log.i(MainActivity.TAG, "clear3");

 		    	removetx(v);
 		        return true;
 		    }
 		});
 		Log.i(MainActivity.TAG, "clear4");
 		Log.d("wifidirect", Log.getStackTraceString(new Exception()));
 		try{
         LL.addView(tx[i]);
 		}
 		catch(Exception e){
 			Log.d("wifidirect", Log.getStackTraceString(e.getCause()));
 		}
   		Log.i(MainActivity.TAG, "clear41");

    	  if(k==s.length()-1)break;
   		Log.i(MainActivity.TAG, "clear42");

  		Log.i(MainActivity.TAG, "clear5");

    	  String s1=s.substring(k+1,s.length());
    	  s=s1;
          }
  		Log.i(MainActivity.TAG, "clear6");

          SV.fullScroll(View.FOCUS_DOWN);
	}
	public String union(String f1,String f2)
	{
		Log.i("Q","read");

		String s1=read(f1),s2=read(f2);String f=""; 
		Log.i("Q","read"+s1+"   "+s2);
		int i1=0,j1=0,i2=0,j2=0;
		while(true){
			Log.i("Q","while true");
			if(j1>=s1.length()){
				f+=s2.substring(j2);
				Log.i("Q","BB"+j2);
				break;}
			if(j2>=s2.length()){
				f+=s1.substring(j1);
				Log.i("Q","AA"+j1+" "+j2+" "+f);
				break;}	
			
			i1+=13;
			Log.i("Q",Integer.toString(i1)+s1.substring(j1+4,j1+i1)+" "+j1+" "+i1+" "+s1);
			//Log.i("Q",s1.substring(j1, i1));
			int a=Integer.parseInt(s1.substring(j1+4,j1+i1));
		//Log.i("Q",s1.substring(j1, i1));
			i2+=13;
			Log.i("Q",Integer.toString(i2));
			int b=Integer.parseInt(s2.substring(j2+4,j2+i2));
			if(a<b)
			{
				
				while(s1.charAt(j1+i1)!=255)
				{
					i1++;
				}
				f+=s1.substring(j1,j1+i1+1);
				Log.i("Q","b>a");
				j1+=i1+1;
				Log.i("Q","b>a"+j1+"   "+s1.length()+"  "+j2+"  "+s2.length());
				i1=0;i2=0;
				
			}
			if(a>b)
			{
				
				while(s2.charAt(j2+i2)!=255)
				{
					i2++;
				}
				f+=s2.substring(j2,j2+i2+1);
				Log.i("Q","a>b");
				j2+=i2+1;
				i1=0;i2=0;
				
			}
			if(a==b)
			{
				
				while(s2.charAt(j2+i2)!=255)
				{
					i2++;i1++;
				}
				f+=s2.substring(j2,j2+i2+1);
				Log.i("Q","a==b "+f);
				j2+=i2+1;
				j1+=i1+1;
				i1=0;i2=0;
				
			}
				
				
			}
		
				
	
		return f;
			
		}
	public void removetx(View v){
		int id=v.getId();
		String in=(read("text.txt"));
		String rem=tx[id].getText().toString();
		char c=254;
		String ch=""+c;
		String tmp=rem.replaceAll("\n", ch);
		rem=tmp;
		Log.i("ABC",rem);
		tmp=in.replaceFirst(rem+end,"");
		Log.i("ABC",tmp);
		int i=0,j=0;
		in=tmp;
		//char c=255;
		while(i<in.length()){
			if(in.charAt(i)==255)j++;
			if(j==id)break;
			i++;
		}
		String first=in.substring(0,i+1);
		Log.i("ABC",first);
		String second=in.substring(i+14);
		Log.i("ABC",second);
		in=first+second;
		//Log.i("ABC",tmp);
		delete();
		write(in,"text.txt");
		addtext(correctread(read("text.txt")));
	}
	public String correctwrite(String s){
		String k="";
		int j=0;
		char c=254;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='\n'){
				k=k+s.substring(j,i)+c;
				j=i+1;
				if(j==s.length())break;
			}
		}
		
		if(j!=s.length())k=k+s.substring(j,s.length());
		return k;
	}
	public String correctread(String s){
		String k="";
		int j=0;
		if(s.length()!=0)j=13;
		char c=254,ch=35,c2=255;
		Log.i(MainActivity.TAG,"length "+s);
		for(int i=0;i<s.length();i++){
			//Log.i(TAG1,Integer.toString(i));
			//if(s.charAt(i)==ch){Log.i("P","b "+i+" "+j);j=i+1;}
			if(s.charAt(i)==c2){k+=s.substring(j,i+1);if(j+14>=s.length()){j=s.length();break;}         j=i+14;}
			if(s.charAt(i)==c){
				Log.i(TAG1,"b");
				k=k+s.substring(j,i)+'\n';
				j=i+1;
				if(j==s.length())break;
			}
			
		}
		
		if(j!=s.length())k=k+s.substring(j,s.length());
		Log.i(TAG,"c");
		return k;
	}
	public void write(String s,String f){
		    FileOutputStream fos;
		    try {
			    Log.i(TAG,f);
		        fos = openFileOutput(f,MODE_APPEND);
		        fos.write((s).getBytes());
		        fos.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();

		    } catch (IOException e) {
		        e.printStackTrace();

		    }

	  }
	  public String read(String s){
		  StringBuffer datax = new StringBuffer("");
	        try {
	            FileInputStream fIn = openFileInput ( s ) ;
	            InputStreamReader isr = new InputStreamReader ( fIn ) ;
	            BufferedReader buffreader = new BufferedReader ( isr ) ;
	            String readString = buffreader.readLine ( ) ;
	            while (readString != null ) {
	            	System.out.println(readString);
	                datax.append(readString);
	                readString = buffreader.readLine ( ) ;
	                
	            }
	            System.out.println();
	            isr.close ( ) ;
	        } catch ( IOException ioe ) {
	            ioe.printStackTrace ( ) ;
	        }
	        return datax.toString();
	  }
	  public void onClick(View v) {
		    Calendar cal = Calendar.getInstance(); 
		 //   Log.i(TAG1,Boolean.toString(q));
		int sec = cal.get(Calendar.SECOND);
		int min = cal.get(Calendar.MINUTE);
		int hou = cal.get(Calendar.HOUR);
		int dat = cal.get(Calendar.DATE);
		int mon = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		String senton=""+"sent on"+Integer.toString(hou)+":"+Integer.toString(min)+":"+Integer.toString(sec)+" "+Integer.toString(dat)+"/"+Integer.toString(mon)+"/"+Integer.toString(year)+"\n";
		
	//	long  senton =mon*100000000+dat*1000000+hou*10000+min*100+sec  ;
		String s1="";
	    s1=ET1.getText().toString();
	    if(s1.length()==0)return;
	    char ch=35;
	    String tmp=""+ch;
	    
	    s=System.currentTimeMillis()+senton+s1+end;
	    Log.i("R",s+ch);
	     write(correctwrite(s),"text.txt");
	     /*
	     Log.i("PP",read("text.txt")+"  "+read("text1.txt"));
	    q++;
	    String f=union("text.txt","text1.txt");
	    clear();
	    write(f,"text.txt");
	    Log.i(TAG,read("text.txt"));
	    Log.i("llll",read("text.txt"));*/
	    
	    
	//  String f=union("text.txt","text1.txt");
	  Log.i("Q","Cl");
	//  clear();
	  Log.i("Q","Cl--wr");
		//write(f,"text.txt");
		String s3=read("text.txt");String s2=read("text1.txt"); 
		Log.i("Q","read"+s3+" @@  ");
		Log.i("Q","read"+s2);
		
	    switch (v.getId()) {
	    case R.id.B1:
	    	addtext(correctread(read("text.txt")));
	    	stopService();
	    	stopDiscovery();
	    	hashValue = getMD5EncryptedString(read("text.txt"));
	    	Log.i("kkk","yyy"+hashValue);
	    	fileSize = Integer.toString((read("text.txt").length()));
	    	register();
	    	discoverService();
		    ET1.setText("");
	      break;
	    default:
	      break;
	   }
	 }
	  public void delete(){
	        try {
	    		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("text.txt", Context.MODE_PRIVATE));
	    		//OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(openFileOutput("text1.txt", Context.MODE_PRIVATE));

	    		Log.i(MainActivity.TAG, "clear");

				outputStreamWriter.append("");
				//outputStreamWriter1.append("");
				outputStreamWriter.close();
				//outputStreamWriter1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
	        
	  }
	  public int clear(){
	        try {
	    		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("text.txt", Context.MODE_PRIVATE));
	    		OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(openFileOutput("text1.txt", Context.MODE_PRIVATE));

	    		Log.i(MainActivity.TAG, "clear");

				outputStreamWriter.append("");
				outputStreamWriter1.append("");
				outputStreamWriter.close();
				outputStreamWriter1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
	        
		}


}
