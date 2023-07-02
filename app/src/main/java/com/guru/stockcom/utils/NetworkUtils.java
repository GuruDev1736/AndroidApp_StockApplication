package com.guru.stockcom.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;


public class NetworkUtils {
    public static boolean isNetworkAvailable(Context activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo!=null && networkInfo.isConnected();
        }

        Network[] networks = connectivityManager.getAllNetworks();
        boolean hasInternet = false;
        if(networks.length>0){
            for(Network network :networks){
                NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(network);
                if(nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) hasInternet = true;
            }
        }
        return hasInternet;
    }

    public static void isNetworkNotAvailable(Context activity) {
        Toast.makeText(activity, "Please check your internet Connection", Toast.LENGTH_SHORT).show();
    }
}
