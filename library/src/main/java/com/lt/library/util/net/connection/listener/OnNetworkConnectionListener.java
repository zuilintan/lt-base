package com.lt.library.util.net.connection.listener;

import com.lt.library.util.net.connection.ConnectionUtil;

public interface OnNetworkConnectionListener {
    void onNetworkConnectionStatus(@ConnectionUtil.NetworkDef int networkConnectionStatus);
}