package com.lt.library.util.net.connection.listener;

import com.lt.library.util.net.connection.ConnectionUtil;

public interface OnCellularNetworkConnectionListener {
    void onCellularConnectionStatus(@ConnectionUtil.NetworkDef int cellularConnectionStatus);
}