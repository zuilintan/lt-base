package com.lt.library.util.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.IntDef;

import com.lt.library.util.LogUtil;
import com.lt.library.util.context.ContextUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionUtil {
    public static final int CONNECTION_STATUS_NOT_CONNECTED = 10;
    public static final int CONNECTION_STATUS_CONNECTED = 20;
    public static final int CONNECTION_STATUS_CONNECTED_VALIDATED = 21;
    private static final int CONNECTION_TYPE_NETWORK = 0;
    private static final int CONNECTION_TYPE_CELLULAR_NETWORK = 1;
    private static final int CONNECTION_TYPE_WIFI_NETWORK = 2;
    private static final String TRANSPORT_KEY_UNKNOWN = "UNKNOWN";
    private static final int TRANSPORT_VALUE_UNKNOWN = -1;
    private final Handler mHandler;
    private final ConnectivityManager.NetworkCallback mNetworkCallback;
    private final Map<Network, NetworkCapabilities> mNetworkMap = new ConcurrentHashMap<>();
    private final Map<Network, NetworkCapabilities> mExcludedTransportsNetworkMap = new ConcurrentHashMap<>();
    private final int[] mNeedExcludeTransports;
    private final AtomicInteger mNetworkConnectionStatus = new AtomicInteger();
    private final AtomicInteger mCellularNetworkConnectionStatus = new AtomicInteger();
    private final AtomicInteger mWifiNetworkConnectionStatus = new AtomicInteger();
    private OnConnectionListener mOnNetworkListener;
    private OnConnectionListener mOnCellularNetworkListener;
    private OnConnectionListener mOnWifiNetworkListener;

    {
        mHandler = new Handler(Looper.getMainLooper(), msg -> {
            switch (msg.what) {
                case CONNECTION_TYPE_NETWORK:
                    callOnNetworkListener((Integer) msg.obj);
                    break;
                case CONNECTION_TYPE_CELLULAR_NETWORK:
                    callOnCellularNetworkListener((Integer) msg.obj);
                    break;
                case CONNECTION_TYPE_WIFI_NETWORK:
                    callOnWifiNetworkListener((Integer) msg.obj);
                    break;
                default:
                    return false;
            }
            return true;
        });
        mNetworkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                LogUtil.d("network = " + network + ", networkCapabilities = " + networkCapabilities);
                if (networkCapabilities == null) {
                    return;
                }
                NetworkCapabilities oldValue = mNetworkMap.put(network, networkCapabilities);
                if (oldValue != null) {
                    LogUtil.d("networkMap value updated, old value = " + oldValue);
                }
                boolean hasInternetCapability = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                if (!hasInternetCapability) {
                    return;
                }
                int connectionStatus = CONNECTION_STATUS_CONNECTED;
                if (!isNeedExcludeTransport(networkCapabilities, mNeedExcludeTransports)) {
                    checkAndCallNetworkListener(connectionStatus);
                }
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    checkAndCallCellularNetworkListener(connectionStatus);
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    checkAndCallWifiNetworkListener(connectionStatus);
                }
            }//当网络连接成功, 可以使用时

            @Override
            public void onLosing(Network network, int maxMsToLive) {
                super.onLosing(network, maxMsToLive);
                LogUtil.v("network = " + network + ", maxMsToLive = " + maxMsToLive);
            }//当网络连接正在断开

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                NetworkCapabilities networkCapabilities = mNetworkMap.get(network);
                LogUtil.d("network = " + network + ", networkCapabilities = " + networkCapabilities);
                if (networkCapabilities == null) {
                    return;
                }
                mNetworkMap.remove(network);
                mExcludedTransportsNetworkMap.putAll(mNetworkMap);
                LogUtil.d("networkMap before exclusion = " + mNetworkMap);
                Iterator<Map.Entry<Network, NetworkCapabilities>> iterator = mExcludedTransportsNetworkMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Network, NetworkCapabilities> entry = iterator.next();
                    if (isNeedExcludeTransport(entry.getValue(), mNeedExcludeTransports)) {
                        iterator.remove();
                    }
                }
                LogUtil.d("networkMap after exclusion = " + mExcludedTransportsNetworkMap);
                int connectionStatus = CONNECTION_STATUS_NOT_CONNECTED;
                if (mExcludedTransportsNetworkMap.isEmpty()) {
                    checkAndCallNetworkListener(connectionStatus);
                } else {
                    mExcludedTransportsNetworkMap.clear();
                }
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    checkAndCallCellularNetworkListener(connectionStatus);
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    checkAndCallWifiNetworkListener(connectionStatus);
                }
            }//当网络连接已经断开

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                LogUtil.v();
            }//当网络连接超时或网络请求达不到可用要求

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                LogUtil.d("network = " + network + ", networkCapabilities = " + networkCapabilities);
                boolean hasInternetCapability = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                if (!hasInternetCapability) {
                    return;
                }
                boolean hasValidatedCapability = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                int connectionStatus = !hasValidatedCapability ? CONNECTION_STATUS_CONNECTED : CONNECTION_STATUS_CONNECTED_VALIDATED;
                if (!isNeedExcludeTransport(networkCapabilities, mNeedExcludeTransports)) {
                    checkAndCallNetworkListener(connectionStatus);
                }
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    checkAndCallCellularNetworkListener(connectionStatus);
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    checkAndCallWifiNetworkListener(connectionStatus);
                }
            }//当网络连接的状态变化, 但仍旧可用

            @Override
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties);
                LogUtil.v("network = " + network + ", linkProperties = " + linkProperties);
            }//当网络连接的属性变化
        };
    }

    private ConnectionUtil(Builder builder) {
        mNeedExcludeTransports = builder.mNeedExcludeTransports;
        mOnNetworkListener = builder.mOnNetworkListener;
        mOnCellularNetworkListener = builder.mOnCellularNetworkListener;
        mOnWifiNetworkListener = builder.mOnWifiNetworkListener;
    }

    public static boolean isConnected(int... needExcludeTransports) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        LogUtil.d("network = " + network);
        if (network == null) {
            LogUtil.d("result = " + false);
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        if (networkCapabilities == null) {
            LogUtil.d("result = " + false);
            return false;
        }
        boolean hasInternetCapability = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        LogUtil.d("hasInternetCapability = " + hasInternetCapability);
        if (!hasInternetCapability) {
            LogUtil.d("result = " + false);
            return false;
        }
        boolean result = !isNeedExcludeTransport(networkCapabilities, needExcludeTransports);
        LogUtil.d("result = " + result);
        return result;
    }

    public static boolean isConnectedAndValidated(int... needExcludeTransports) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        LogUtil.d("network = " + network);
        if (network == null) {
            LogUtil.d("result = " + false);
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        if (networkCapabilities == null) {
            LogUtil.d("result = " + false);
            return false;
        }
        boolean hasInternetAndValidatedCapability = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        LogUtil.d("hasInternetAndValidatedCapability = " + hasInternetAndValidatedCapability);
        if (!hasInternetAndValidatedCapability) {
            LogUtil.d("result = " + false);
            return false;
        }
        boolean result = !isNeedExcludeTransport(networkCapabilities, needExcludeTransports);
        LogUtil.d("result = " + result);
        return result;
    }

    public static boolean isNotMetered(int... needExcludeTransports) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        LogUtil.d("network = " + network);
        if (network == null) {
            LogUtil.d("result = " + false);
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        if (networkCapabilities == null) {
            LogUtil.d("result = " + false);
            return false;
        }
        boolean hasNotMeteredCapability = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
        LogUtil.d("hasNotMeteredCapability = " + hasNotMeteredCapability);
        if (!hasNotMeteredCapability) {
            LogUtil.d("result = " + false);
            return false;
        }
        boolean result = !isNeedExcludeTransport(networkCapabilities, needExcludeTransports);
        LogUtil.d("result = " + result);
        return result;
    }

    public static boolean isBelongTransport(int transport) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        LogUtil.d("network = " + network);
        if (network == null) {
            LogUtil.d("result = " + false);
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        if (networkCapabilities == null) {
            LogUtil.d("result = " + false);
            return false;
        }
        boolean result = networkCapabilities.hasTransport(transport);
        LogUtil.d("result = " + result);
        return result;
    }// TODO: 2021/6/8

    public static int getTransport() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        LogUtil.d("network = " + network);
        if (network == null) {
            LogUtil.w("result = " + TRANSPORT_KEY_UNKNOWN);
            return TRANSPORT_VALUE_UNKNOWN;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        if (networkCapabilities == null) {
            LogUtil.w("result = " + TRANSPORT_KEY_UNKNOWN);
            return TRANSPORT_VALUE_UNKNOWN;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            LogUtil.d("result = TRANSPORT_CELLULAR");
            return NetworkCapabilities.TRANSPORT_CELLULAR;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            LogUtil.d("result = TRANSPORT_WIFI");
            return NetworkCapabilities.TRANSPORT_WIFI;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
            LogUtil.d("result = TRANSPORT_BLUETOOTH");
            return NetworkCapabilities.TRANSPORT_BLUETOOTH;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            LogUtil.d("result = TRANSPORT_ETHERNET");
            return NetworkCapabilities.TRANSPORT_ETHERNET;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
            LogUtil.d("result = TRANSPORT_VPN");
            return NetworkCapabilities.TRANSPORT_VPN;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
            LogUtil.d("result = TRANSPORT_WIFI_AWARE");
            return NetworkCapabilities.TRANSPORT_WIFI_AWARE;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
            LogUtil.d("result = TRANSPORT_LOWPAN");
            return NetworkCapabilities.TRANSPORT_LOWPAN;
        }
        LogUtil.w("result = " + TRANSPORT_KEY_UNKNOWN);
        return TRANSPORT_VALUE_UNKNOWN;
    }// TODO: 2021/6/8

    public static String getRawTransport() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        LogUtil.d("network = " + network);
        if (network == null) {
            LogUtil.w("result = " + TRANSPORT_KEY_UNKNOWN);
            return TRANSPORT_KEY_UNKNOWN;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        LogUtil.d("networkCapabilities = " + networkCapabilities);
        if (networkCapabilities == null) {
            LogUtil.w("result = " + TRANSPORT_KEY_UNKNOWN);
            return TRANSPORT_KEY_UNKNOWN;
        }
        String ncStr = networkCapabilities.toString();
        String strStart = "Transports: ";
        String strEnd = " Capabilities:";
        int indexStart = ncStr.indexOf(strStart);
        int indexEnd = ncStr.indexOf(strEnd);
        String result;
        try {
            result = ncStr.substring(indexStart + strStart.length(), indexEnd);
            LogUtil.d("result = " + result);
        } catch (IndexOutOfBoundsException e) {
            result = TRANSPORT_KEY_UNKNOWN;
            LogUtil.w("result = " + result, "e = " + e.toString());
        }
        return result;
    }

    private static boolean isNeedExcludeTransport(NetworkCapabilities networkCapabilities, int[] excludeTransports) {
        if (excludeTransports == null || excludeTransports.length == 0) {
            return false;
        }
        for (int excludeTransport : excludeTransports) {
            if (networkCapabilities.hasTransport(excludeTransport)) {
                LogUtil.d("transport = " + excludeTransport + ", excludeTransports = " + Arrays.toString(excludeTransports) + ", need to be excluded");
                return true;
            }
        }
        return false;
    }

    private synchronized void checkAndCallNetworkListener(int newConnectionStatus) {
        int oldConnectionStatus = mNetworkConnectionStatus.get();
        LogUtil.d("oldConnectionStatus= " + oldConnectionStatus + ", newConnectionStatus = " + newConnectionStatus);
        if (oldConnectionStatus == newConnectionStatus) {
            return;
        }
        mHandler.sendMessage(mHandler.obtainMessage(CONNECTION_TYPE_NETWORK, newConnectionStatus));
        mNetworkConnectionStatus.set(newConnectionStatus);
    }

    private synchronized void checkAndCallCellularNetworkListener(int newConnectionStatus) {
        int oldConnectionStatus = mCellularNetworkConnectionStatus.get();
        LogUtil.d("oldConnectionStatus= " + oldConnectionStatus + ", newConnectionStatus = " + newConnectionStatus);
        if (oldConnectionStatus == newConnectionStatus) {
            return;
        }
        mHandler.sendMessage(mHandler.obtainMessage(CONNECTION_TYPE_CELLULAR_NETWORK, newConnectionStatus));
        mCellularNetworkConnectionStatus.set(newConnectionStatus);
    }

    private synchronized void checkAndCallWifiNetworkListener(int newConnectionStatus) {
        int oldConnectionStatus = mWifiNetworkConnectionStatus.get();
        LogUtil.d("oldConnectionStatus= " + oldConnectionStatus + ", newConnectionStatus = " + newConnectionStatus);
        if (oldConnectionStatus == newConnectionStatus) {
            return;
        }
        mHandler.sendMessage(mHandler.obtainMessage(CONNECTION_TYPE_WIFI_NETWORK, newConnectionStatus));
        mWifiNetworkConnectionStatus.set(newConnectionStatus);
    }

    private void callOnNetworkListener(@NetworkDef int connectionStatus) {
        if (mOnNetworkListener != null) {
            LogUtil.d("call start");
            mOnNetworkListener.onConnectionStatusChanged(connectionStatus);
            LogUtil.d("call end");
        }
    }

    private void callOnCellularNetworkListener(@NetworkDef int connectionStatus) {
        if (mOnCellularNetworkListener != null) {
            LogUtil.d("call start");
            mOnCellularNetworkListener.onConnectionStatusChanged(connectionStatus);
            LogUtil.d("call end");
        }
    }

    private void callOnWifiNetworkListener(@NetworkDef int connectionStatus) {
        if (mOnWifiNetworkListener != null) {
            LogUtil.d("call start");
            mOnWifiNetworkListener.onConnectionStatusChanged(connectionStatus);
            LogUtil.d("call end");
        }
    }

    private void addEvent() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), mNetworkCallback);
    }

    private void delEvent() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.unregisterNetworkCallback(mNetworkCallback);
    }

    public void release() {
        delEvent();
        mNetworkMap.clear();
        mExcludedTransportsNetworkMap.clear();
        mHandler.removeCallbacksAndMessages(null);
        mOnNetworkListener = null;
        mOnWifiNetworkListener = null;
        mOnCellularNetworkListener = null;
    }

    public interface OnConnectionListener {
        void onConnectionStatusChanged(@NetworkDef int connectionStatus);
    }

    @IntDef({CONNECTION_STATUS_NOT_CONNECTED, CONNECTION_STATUS_CONNECTED, CONNECTION_STATUS_CONNECTED_VALIDATED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkDef {
    }

    public static class Builder {
        private int[] mNeedExcludeTransports;
        private OnConnectionListener mOnNetworkListener;//optional (required or optional)
        private OnConnectionListener mOnCellularNetworkListener;//optional
        private OnConnectionListener mOnWifiNetworkListener;//optional

        public Builder setOnNetworkListener(OnConnectionListener onConnectionListener, int... needExcludeTransports) {
            mOnNetworkListener = onConnectionListener;
            mNeedExcludeTransports = needExcludeTransports;
            return this;
        }

        public Builder setOnCellularNetworkListener(OnConnectionListener onConnectionListener) {
            mOnCellularNetworkListener = onConnectionListener;
            return this;
        }

        public Builder setOnWifiNetworkListener(OnConnectionListener onConnectionListener) {
            mOnWifiNetworkListener = onConnectionListener;
            return this;
        }

        public ConnectionUtil build() {
            ConnectionUtil connectionUtil;
            if (mOnNetworkListener != null || mOnCellularNetworkListener != null || mOnWifiNetworkListener != null) {
                connectionUtil = new ConnectionUtil(this);
                connectionUtil.addEvent();
            } else {
                throw new RuntimeException("call at least one method 'setOnXxxListener'");
            }
            return connectionUtil;
        }
    }
}
