package menolla.co.za.itsi_test.network

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.Log



class NetworkCheck {

    companion object {
        val TAG = "NetworkCheck"
        var mobileConnected: Boolean = false
        var wifiConnected: Boolean = false
        lateinit var mobileInfo: NetworkInfo
        lateinit var wifiInfo: NetworkInfo
        lateinit var connectivity: ConnectivityManager

        @SuppressLint("MissingPermission")
        fun checkNetworkAvailability(ctx: Context) {
            connectivity = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            mobileInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        }

        //check if there is mobile network connectivity
        fun checkMobileNetwork(ctx: Context): Boolean {
            checkNetworkAvailability(ctx)
            if (mobileInfo != null) {
                if (mobileInfo.isAvailable()) {
                    Log.w(TAG, "###### MOBILE_NETWORK AVAILABLE on check")
                    if (mobileInfo.isConnected()) {
                        Log.w(TAG, "###### MOBILE_NETWORK CONNECTED on check")
                        mobileConnected = true
                    } else {
                        Log.d(TAG, "@@@ MOBILE_NETWORK NOT CONNECTED on check")
                        mobileConnected = false
                    }
                } else {
                    Log.d(TAG, "@@@ MOBILE_NETWORK NOT AVAILABLE on check")
                    mobileConnected = false
                }
            }
            return mobileConnected
        }

        //check if there is wifi connectivity
        fun checkWifi(ctx: Context): Boolean {
            checkNetworkAvailability(ctx)
            if (wifiInfo.isAvailable()) {
                Log.w(TAG, "###### WIFI AVAILABLE on check")
                if (wifiInfo.isConnected()) {
                    Log.w(TAG, "###### WIFI CONNECTED on check")
                    wifiConnected = true
                } else {
                    Log.e(TAG, "@@@ WIFI NOT CONNECTED on check")
                    wifiConnected = false
                }
            } else {
                Log.e(TAG, "###### WIFI NOT AVAILABLE on check")
                wifiConnected = false
            }
            return wifiConnected
        }
    }

    //todo latest method to check if network is available or not
    fun Context.isNetworkConnected(): Boolean {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val allNetworks = manager?.allNetworks?.let { it } ?: return false
            allNetworks.forEach { network ->
                val info = manager.getNetworkInfo(network)
                if (info.state == NetworkInfo.State.CONNECTED) return true
            }
        } else {
            val allNetworkInfo = manager?.allNetworkInfo?.let { it } ?: return false
            allNetworkInfo.forEach { info ->
                if (info.state == NetworkInfo.State.CONNECTED) return true
            }
        }
        return false
    }

    fun isNetworkAvailable(activity: Activity): Boolean {
        val connectivity = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity == null) {
            return false
        } else {
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }

}