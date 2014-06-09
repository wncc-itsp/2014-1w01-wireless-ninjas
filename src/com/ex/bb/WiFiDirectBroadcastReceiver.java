package com.ex.bb;

import com.ex.bb.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.util.Log;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver  {

	
	
	private WifiP2pManager mManager;
    private Channel mChannel;
    private MainActivity mActivity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
            MainActivity activity) {
        super();
        
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }
    
    
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
        	Log.d(MainActivity.TAG, "nonnull");
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
        	if (mManager == null) {
                return;
            }
        	Log.i(MainActivity.TAG,"lllll");

            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
            	Log.i(MainActivity.TAG,"connected");
                // We are connected with the other device, request connection
                // info to find group owner IP

                mManager.requestConnectionInfo(mChannel, mActivity);
            }
            if (!networkInfo.isConnected()) {
            	
            	Log.i(MainActivity.TAG,"FFFFFFFFFFFFFff");
            	mActivity.hashValue = MainActivity.getMD5EncryptedString(mActivity.read("text.txt")).substring(1,7);
        		mActivity.fileSize = Integer.toString(mActivity.read("text.txt").length());
        		mActivity.register();
        		mActivity.discoverService();
        		mActivity.serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
            }
            
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }

    

}
	


	
	



