package com.example.evil;

import android.util.Log;

import java.net.*;
import java.util.Enumeration;

public class VpnCheck {
    public static boolean vpncheck() {
        boolean hasVpn = false;
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            if (nis != null) {
                while (nis.hasMoreElements()) {
                    //String ni = String.valueOf(nis.nextElement()); // 但是我这样写，你就hook干掉不了vpn检测了!
                    String ni = nis.nextElement().getName();
                    if (ni.contains("ppp") || ni.contains("tun")) {
                        hasVpn = true;
                        break;
                    }
                }
            }
        } catch (SocketException e) {
        }
        Log.d("Che", "App is running on vpn: " + hasVpn);
        return hasVpn;
    }
}
