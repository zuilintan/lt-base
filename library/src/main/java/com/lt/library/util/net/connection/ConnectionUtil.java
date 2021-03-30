package com.lt.library.util.net.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;

import com.lt.library.util.LogUtil;
import com.lt.library.util.context.ContextUtil;
import com.lt.library.util.net.connection.listener.OnCellularNetworkConnectionListener;
import com.lt.library.util.net.connection.listener.OnNetworkConnectionListener;
import com.lt.library.util.net.connection.listener.OnWifiNetworkConnectionListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionUtil {
    public static final int CONNECTION_STATUS_NOT_CONNECTED = 0;
    public static final int CONNECTION_STATUS_CONNECTED = 1;
    public static final int CONNECTION_STATUS_CONNECTED_VALIDATED = 2;
    private static final int HANDLE_WHAT_NETWORK_CONNECTION_LISTENER = 0;
    private static final int HANDLE_WHAT_CELLULAR_CONNECTION_LISTENER = 1;
    private static final int HANDLE_WHAT_WIFI_CONNECTION_LISTENER = 2;
    private OnNetworkConnectionListener mOnNetworkConnectionListener;
    private OnCellularNetworkConnectionListener mOnCellularNetworkConnectionListener;
    private OnWifiNetworkConnectionListener mOnWifiNetworkConnectionListener;
    private final Map<String, Integer> mNetworkTransportMap = new ConcurrentHashMap<>();
    private final Handler mHandler = new Handler(Looper.getMainLooper(), this::handleMessage);
    private final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            LogUtil.d("network = " + network);
            notifyNetworkAvailable(network);
        }//当网络连接成功, 可以使用时

        @Override
        public void onLosing(Network network, int maxMsToLive) {
            super.onLosing(network, maxMsToLive);
            LogUtil.d("network = " + network + ", maxMsToLive = " + maxMsToLive);
        }//当网络连接正在断开

        @Override
        public void onLost(Network network) {
            super.onLost(network);
            LogUtil.d("network = " + network);
            notifyNetworkLost(network);
        }//当网络连接已经断开

        @Override
        public void onUnavailable() {
            super.onUnavailable();
            LogUtil.d();
        }//当网络连接超时或网络请求达不到可用要求

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            LogUtil.d("network = " + network + ", networkCapabilities = " + networkCapabilities);
            notifyNetworkCapabilitiesChanged(network, networkCapabilities);
        }//当网络连接的状态变化, 但仍旧可用

        @Override
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties);
            LogUtil.d("network = " + network + ", linkProperties = " + linkProperties);
        }//当网络连接的属性变化
    };

    private void notifyNetworkAvailable(Network network) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            LogUtil.d("network = " + network + ", transport = TRANSPORT_CELLULAR");
            mNetworkTransportMap.put(network.toString(), NetworkCapabilities.TRANSPORT_CELLULAR);
            mHandler.sendMessage(mHandler.obtainMessage(HANDLE_WHAT_CELLULAR_CONNECTION_LISTENER, CONNECTION_STATUS_CONNECTED));
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            LogUtil.d("network = " + network + ", transport = TRANSPORT_WIFI");
            mNetworkTransportMap.put(network.toString(), NetworkCapabilities.TRANSPORT_WIFI);
            mHandler.sendMessage(mHandler.obtainMessage(HANDLE_WHAT_WIFI_CONNECTION_LISTENER, CONNECTION_STATUS_CONNECTED));
        } else {
            LogUtil.w("network = " + network + ", other transport, networkCapabilities = " + networkCapabilities);
            mHandler.sendMessage(mHandler.obtainMessage(HANDLE_WHAT_NETWORK_CONNECTION_LISTENER, CONNECTION_STATUS_NOT_CONNECTED));
            mHandler.sendMessage(mHandler.obtainMessage(HANDLE_WHAT_CELLULAR_CONNECTION_LISTENER, CONNECTION_STATUS_NOT_CONNECTED));
            mHandler.sendMessage(mHandler.obtainMessage(HANDLE_WHAT_WIFI_CONNECTION_LISTENER, CONNECTION_STATUS_NOT_CONNECTED));
        }
    }

    private void notifyNetworkCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        boolean validated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        LogUtil.d("network = " + network + ", validated = " + validated);
        if (validated) {
            mHandler.sendMessage(mHandler.obtainMessage(HANDLE_WHAT_NETWORK_CONNECTION_LISTENER, CONNECTION_STATUS_CONNECTED_VALIDATED));
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            LogUtil.d("network = " + network + ", validated = " + validated + ", transport = TRANSPORT_CELLULAR");
            if (validated) {
                mHandler.sendMessage(mHandler.obtainMessage(HANDLE_WHAT_CELLULAR_CONNECTION_LISTENER, CONNECTION_STATUS_CONNECTED_VALIDATED));
            }
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            LogUtil.d("network = " + network + ", validated = " + validated + ", transport = TRANSPORT_CELLULAR");
            if (validated) {
                mHandler.sendMessage(mHandler.obtainMessage(HANDLE_WHAT_WIFI_CONNECTION_LISTENER, CONNECTION_STATUS_CONNECTED_VALIDATED));
            }
        } else {
            LogUtil.w("network = " + network + ", other transport, networkCapabilities = " + networkCapabilities);
        }
    }

    private void notifyNetworkLost(Network network) {
        Integer transport = mNetworkTransportMap.get(network.toString());
        if (transport == null) {
            LogUtil.d("unknown transport");
            return;
        }
        if (transport == NetworkCapabilities.TRANSPORT_CELLULAR) {
            LogUtil.d("network = " + network + ", transport = TRANSPORT_CELLULAR");
            mHandler.sendMessage(mHandler.obtainMessage(HANDLE_WHAT_CELLULAR_CONNECTION_LISTENER, CONNECTION_STATUS_NOT_CONNECTED));
        } else if (transport == NetworkCapabilities.TRANSPORT_WIFI) {
            LogUtil.d("network = " + network + ", transport = TRANSPORT_WIFI");
            mHandler.sendMessage(mHandler.obtainMessage(HANDLE_WHAT_WIFI_CONNECTION_LISTENER, CONNECTION_STATUS_NOT_CONNECTED));
        } else {
            LogUtil.d("network = " + network + ", other transport");
        }
    }

    private void callOnNetworkConnectionListener(@NetworkDef int networkConnectionStatus) {
        if (mOnNetworkConnectionListener != null) {
            mOnNetworkConnectionListener.onNetworkConnectionStatus(networkConnectionStatus);
        }
    }

    private void callOnCellularConnectionListener(@NetworkDef int cellularConnectionStatus) {
        if (mOnCellularNetworkConnectionListener != null) {
            mOnCellularNetworkConnectionListener.onCellularConnectionStatus(cellularConnectionStatus);
        }
    }

    private void callOnWifiConnectionListener(@NetworkDef int wifiConnectionStatus) {
        if (mOnWifiNetworkConnectionListener != null) {
            mOnWifiNetworkConnectionListener.onWifiConnectionStatus(wifiConnectionStatus);
        }
    }

    private boolean handleMessage(Message msg) {
        switch (msg.what) {
            case HANDLE_WHAT_NETWORK_CONNECTION_LISTENER:
                callOnNetworkConnectionListener((Integer) msg.obj);
                break;
            case HANDLE_WHAT_CELLULAR_CONNECTION_LISTENER:
                callOnCellularConnectionListener((Integer) msg.obj);
                break;
            case HANDLE_WHAT_WIFI_CONNECTION_LISTENER:
                callOnWifiConnectionListener((Integer) msg.obj);
                break;
            default:
                break;
        }
        return true;
    }

    public boolean isActiveNetworkValidated() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    public boolean isActiveNetworkNotMetered() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
    }

    public boolean isActiveNetworkBelongTransport(int transport) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        return networkCapabilities.hasCapability(transport);
    }

    public int getActiveNetworkTransport() {
        int result = -1;
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            LogUtil.d("network = " + network + ", transport = TRANSPORT_CELLULAR");
            result = NetworkCapabilities.TRANSPORT_CELLULAR;
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            LogUtil.d("network = " + network + ", transport = TRANSPORT_WIFI");
            result = NetworkCapabilities.TRANSPORT_WIFI;
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
            LogUtil.d("network = " + network + ", transport = TRANSPORT_BLUETOOTH");
            result = NetworkCapabilities.TRANSPORT_BLUETOOTH;
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            LogUtil.d("network = " + network + ", transport = TRANSPORT_ETHERNET");
            result = NetworkCapabilities.TRANSPORT_ETHERNET;
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
            LogUtil.d("network = " + network + ", transport = TRANSPORT_VPN");
            result = NetworkCapabilities.TRANSPORT_VPN;
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
            LogUtil.d("network = " + network + ", transport = TRANSPORT_WIFI_AWARE");
            result = NetworkCapabilities.TRANSPORT_WIFI_AWARE;
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
            LogUtil.d("network = " + network + ", transport = TRANSPORT_LOWPAN");
            result = NetworkCapabilities.TRANSPORT_LOWPAN;
        } else {
            LogUtil.w("network = " + network + ", transport = unknown, networkCapabilities = " + networkCapabilities);
        }
        return result;
    }

    public void addEvent() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerDefaultNetworkCallback(mNetworkCallback);
    }

    public void delEvent() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.unregisterNetworkCallback(mNetworkCallback);
    }

    public void setOnNetworkConnectionListener(OnNetworkConnectionListener onNetworkConnectionListener) {
        mOnNetworkConnectionListener = onNetworkConnectionListener;
    }

    public void setOnCellularNetworkConnectionListener(OnCellularNetworkConnectionListener onCellularNetworkConnectionListener) {
        mOnCellularNetworkConnectionListener = onCellularNetworkConnectionListener;
    }

    public void setOnWifiNetworkConnectionListener(OnWifiNetworkConnectionListener onWifiNetworkConnectionListener) {
        mOnWifiNetworkConnectionListener = onWifiNetworkConnectionListener;
    }

    @IntDef({CONNECTION_STATUS_NOT_CONNECTED, CONNECTION_STATUS_CONNECTED, CONNECTION_STATUS_CONNECTED_VALIDATED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkDef {
    }
}
