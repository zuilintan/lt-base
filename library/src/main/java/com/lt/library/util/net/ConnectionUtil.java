package com.lt.library.util.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;

import com.lt.library.util.LogUtil;
import com.lt.library.util.context.ContextUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionUtil {
    public static final int CONNECTION_STATUS_NOT_CONNECTED = 10;
    public static final int CONNECTION_STATUS_CONNECTED = 20;
    public static final int CONNECTION_STATUS_CONNECTED_VALIDATED = 21;
    private static final int TRANSPORT_UNKNOWN = -1;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Map<Network, NetworkCapabilities> mActiveNetworkMap = new ConcurrentHashMap<>();
    private final ConnectivityManager.NetworkCallback mNetworkCallback;
    private int[] mExcludeTransports;
    private int mConnectionStatus = -1;
    private OnActiveConnectionListener mOnActiveConnectionListener;
    private OnActiveConnectionListener mOnActiveCellularNetworkListener;
    private OnActiveConnectionListener mOnActiveWifiNetworkListener;

    {
        mNetworkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                LogUtil.v("network = " + network);
                notifyAvailable(network);
            }//当网络连接成功, 可以使用时

            @Override
            public void onLosing(Network network, int maxMsToLive) {
                super.onLosing(network, maxMsToLive);
                LogUtil.v("network = " + network + ", maxMsToLive = " + maxMsToLive);
            }//当网络连接正在断开

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                LogUtil.v("network = " + network);
                notifyLost(network);
            }//当网络连接已经断开

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                LogUtil.v();
            }//当网络连接超时或网络请求达不到可用要求

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                LogUtil.v("network = " + network + ", networkCapabilities = " + networkCapabilities);
                notifyCapabilitiesChanged(network, networkCapabilities);
            }//当网络连接的状态变化, 但仍旧可用

            @Override
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties);
                LogUtil.v("network = " + network + ", linkProperties = " + linkProperties);
            }//当网络连接的属性变化
        };
    }

    private ConnectionUtil() {
    }

    public static boolean isConnected(int... excludeTransports) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = connectivityManager.getActiveNetwork();
        LogUtil.d("activeNetwork = " + activeNetwork);
        if (activeNetwork == null) {
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        if (networkCapabilities == null) {
            return false;
        }
        boolean hasInternetCapability = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        LogUtil.d("hasInternetCapability = " + hasInternetCapability);
        if (!hasInternetCapability) {
            return false;
        }
        boolean result = !isNeedExcludeTransport(networkCapabilities, excludeTransports);
        LogUtil.d("result = " + result);
        return result;
    }

    public static boolean isConnectedAndValidated() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = connectivityManager.getActiveNetwork();
        LogUtil.d("activeNetwork = " + activeNetwork);
        if (activeNetwork == null) {
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        if (networkCapabilities == null) {
            return false;
        }
        boolean result = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        LogUtil.d("result = " + result);
        return result;
    }

    public static boolean isNotMetered() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = connectivityManager.getActiveNetwork();
        LogUtil.d("activeNetwork = " + activeNetwork);
        if (activeNetwork == null) {
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        if (networkCapabilities == null) {
            return false;
        }
        boolean result = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
        LogUtil.d("result = " + result);
        return result;
    }

    public static boolean isBelongTransport(int transport) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = connectivityManager.getActiveNetwork();
        LogUtil.d("activeNetwork = " + activeNetwork);
        if (activeNetwork == null) {
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        if (networkCapabilities == null) {
            return false;
        }
        boolean result = networkCapabilities.hasCapability(transport);
        LogUtil.d("result = " + result);
        return result;
    }

    public static int getTransport() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = connectivityManager.getActiveNetwork();
        LogUtil.d("activeNetwork = " + activeNetwork);
        if (activeNetwork == null) {
            LogUtil.w("transport = TRANSPORT_UNKNOWN");
            return TRANSPORT_UNKNOWN;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        if (networkCapabilities == null) {
            LogUtil.w("transport = TRANSPORT_UNKNOWN");
            return TRANSPORT_UNKNOWN;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            LogUtil.d("transport = TRANSPORT_CELLULAR");
            return NetworkCapabilities.TRANSPORT_CELLULAR;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            LogUtil.d("transport = TRANSPORT_WIFI");
            return NetworkCapabilities.TRANSPORT_WIFI;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
            LogUtil.d("transport = TRANSPORT_BLUETOOTH");
            return NetworkCapabilities.TRANSPORT_BLUETOOTH;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            LogUtil.d("transport = TRANSPORT_ETHERNET");
            return NetworkCapabilities.TRANSPORT_ETHERNET;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
            LogUtil.d("transport = TRANSPORT_VPN");
            return NetworkCapabilities.TRANSPORT_VPN;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
            LogUtil.d("transport = TRANSPORT_WIFI_AWARE");
            return NetworkCapabilities.TRANSPORT_WIFI_AWARE;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
            LogUtil.d("transport = TRANSPORT_LOWPAN");
            return NetworkCapabilities.TRANSPORT_LOWPAN;
        }
        LogUtil.w("transport = TRANSPORT_UNKNOWN");
        return TRANSPORT_UNKNOWN;
    }

    private static boolean isNeedExcludeTransport(NetworkCapabilities networkCapabilities, int[] excludeTransports) {
        if (excludeTransports == null || excludeTransports.length == 0) {
            return false;
        }
        for (int excludeTransport : excludeTransports) {
            if (networkCapabilities.hasTransport(excludeTransport)) {
                LogUtil.d("excludeTransports = " + Arrays.toString(excludeTransports) + ", need to be excluded");
                return true;
            }
        }
        return false;
    }

    private void notifyAvailable(Network activeNetwork) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        LogUtil.d("activeNetwork = " + activeNetwork + ", networkCapabilities = " + networkCapabilities);
        LogUtil.d("mActiveNetworkMap = " + mActiveNetworkMap);// TODO: 2021/4/28
        if (networkCapabilities == null) {
            return;
        }
        NetworkCapabilities oldNetworkCapabilities = mActiveNetworkMap.put(activeNetwork, networkCapabilities);
        if (oldNetworkCapabilities != null) {
            LogUtil.d("networkCapabilities updated");
        }// TODO: 2021/4/27
        if (isNeedExcludeTransport(networkCapabilities, mExcludeTransports)) {
            return;
        }
        boolean hasInternetCapability = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        if (!hasInternetCapability) {
            return;
        }
        LogUtil.d("sendMessage = " + CONNECTION_STATUS_CONNECTED);
        callListener(CONNECTION_STATUS_CONNECTED);
    }

    private void notifyCapabilitiesChanged(Network activeNetwork, NetworkCapabilities networkCapabilities) {
        LogUtil.d("activeNetwork = " + activeNetwork + ", networkCapabilities = " + networkCapabilities);
        boolean hasInternetCapability = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        if (!hasInternetCapability) {
            callListener(CONNECTION_STATUS_NOT_CONNECTED);
            return;
        }
        if (isNeedExcludeTransport(networkCapabilities, mExcludeTransports)) {
            return;
        }
        boolean hasValidatedCapability = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        if (!hasValidatedCapability) {
            return;
        }
        LogUtil.d("sendMessage = " + CONNECTION_STATUS_CONNECTED_VALIDATED);
        callListener(CONNECTION_STATUS_CONNECTED_VALIDATED);
    }

    private void notifyLost(Network activeNetwork) {
        NetworkCapabilities networkCapabilities = mActiveNetworkMap.get(activeNetwork);
        LogUtil.d("activeNetwork = " + activeNetwork + ", networkCapabilities = " + networkCapabilities);
        LogUtil.i("mActiveNetworkMap = " + mActiveNetworkMap);// TODO: 2021/4/28
        if (networkCapabilities == null) {
            return;
        }
        mActiveNetworkMap.remove(activeNetwork);
        Map<Network, NetworkCapabilities> activeNetworkMap = new ConcurrentHashMap<>(mActiveNetworkMap);
        Set<Map.Entry<Network, NetworkCapabilities>> entries = activeNetworkMap.entrySet();
        Iterator<Map.Entry<Network, NetworkCapabilities>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Network, NetworkCapabilities> entry = iterator.next();
            if (isNeedExcludeTransport(entry.getValue(), mExcludeTransports)) {
                iterator.remove();
            }
        }
        if (activeNetworkMap.isEmpty()) {
            LogUtil.i("sendMessage = " + CONNECTION_STATUS_NOT_CONNECTED);
            callListener(CONNECTION_STATUS_NOT_CONNECTED);
        }
    }

    private void callListener(int newStatus) {
        LogUtil.d("oldStatus = " + mConnectionStatus + ", newStatus = " + newStatus);
        if (mConnectionStatus == newStatus) {
            LogUtil.d("call exclude");
            return;
        }
        LogUtil.d("call execute");
        callOnConnectionListener(newStatus);
        mConnectionStatus = newStatus;
    }

    private void callOnConnectionListener(@NetworkDef int connectionStatus) {
        if (mOnActiveConnectionListener != null) {
            mOnActiveConnectionListener.onConnectionStatusChanged(connectionStatus);
        }
    }

    private void callOnCellularConnectionListener(@NetworkDef int connectionStatus) {
        if (mOnActiveCellularNetworkListener != null) {
            mOnActiveCellularNetworkListener.onConnectionStatusChanged(connectionStatus);
        }
    }

    private void callOnWifiConnectionListener(@NetworkDef int connectionStatus) {
        if (mOnActiveWifiNetworkListener != null) {
            mOnActiveWifiNetworkListener.onConnectionStatusChanged(connectionStatus);
        }
    }

    private void addEvent() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest networkRequest = new NetworkRequest.Builder().build();
        connectivityManager.registerNetworkCallback(networkRequest, mNetworkCallback, mHandler);
    }

    private void delEvent() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.unregisterNetworkCallback(mNetworkCallback);
    }

    public void release() {
        delEvent();
        mOnActiveConnectionListener = null;
        mOnActiveCellularNetworkListener = null;
        mOnActiveWifiNetworkListener = null;
        mActiveNetworkMap.clear();
    }

    public interface OnActiveConnectionListener {
        void onConnectionStatusChanged(@NetworkDef int connectionStatus);
    }

    @IntDef({CONNECTION_STATUS_NOT_CONNECTED, CONNECTION_STATUS_CONNECTED, CONNECTION_STATUS_CONNECTED_VALIDATED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkDef {
    }

    public static class Builder {
        private final ConnectionUtil mConnectionUtil;

        public Builder() {
            mConnectionUtil = new ConnectionUtil();
        }

        public Builder setOnConnectionListener(OnActiveConnectionListener onActiveConnectionListener, int... excludeTransports) {
            mConnectionUtil.mOnActiveConnectionListener = onActiveConnectionListener;
            mConnectionUtil.mExcludeTransports = excludeTransports;
            return this;
        }

        public Builder setOnCellularConnectionListener(OnActiveConnectionListener onActiveConnectionListener) {
            mConnectionUtil.mOnActiveCellularNetworkListener = onActiveConnectionListener;
            return this;
        }

        public Builder setOnWifiConnectionListener(OnActiveConnectionListener onActiveConnectionListener) {
            mConnectionUtil.mOnActiveWifiNetworkListener = onActiveConnectionListener;
            return this;
        }

        public ConnectionUtil build() {
            if (mConnectionUtil.mOnActiveConnectionListener != null || mConnectionUtil.mOnActiveCellularNetworkListener != null || mConnectionUtil.mOnActiveWifiNetworkListener != null) {
                mConnectionUtil.addEvent();
            } else {
                throw new NullPointerException("call at least one setOnXxxConnectionListener");
            }
            return mConnectionUtil;
        }
    }
}
