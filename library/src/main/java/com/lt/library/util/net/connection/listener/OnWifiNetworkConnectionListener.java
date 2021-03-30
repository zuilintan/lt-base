package com.lt.library.util.net.connection.listener;

import com.lt.library.util.net.connection.ConnectionUtil;

public interface OnWifiNetworkConnectionListener {
    void onWifiConnectionStatus(@ConnectionUtil.NetworkDef int wifiConnectionStatus);
}