package com.suda.jzapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

	@SuppressWarnings("static-access")
	public static boolean checkWifi(Context ctx) {
	    boolean isWifiConnect = true;  
	    ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    //check the networkInfos numbers  
	    NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
	    for (int i = 0; i<networkInfos.length; i++) {  
	        if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
	           if(networkInfos[i].getType() == cm.TYPE_MOBILE) {  
	               isWifiConnect = false;  
	           }  
	           if(networkInfos[i].getType() == cm.TYPE_WIFI) {  
	               isWifiConnect = true;  
	           }  
	        }  
	    }  
	    return isWifiConnect;  
	}
	
	public static boolean checkNetwork(Context ctx){
	    ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
	    for (int i = 0; i<networkInfos.length; i++) {  
	        if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
	           return true;
	        }  
	    }  
	    
	    return false; 
	}
}
