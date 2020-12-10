package ir.chatbot.rt


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import ir.chatbot.ui.listeners.ConnectionStatus

class NetworkReceiver(private val listener: OnNetworkStateChangedListener?) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if(listener == null)
            return

        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = conn.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            listener.onChange(ConnectionStatus.CONNECTING)
        } else {
            listener.onChange(ConnectionStatus.OFFLINE)
        }
    }

    interface OnNetworkStateChangedListener {
        fun onChange(status: ConnectionStatus)
    }
}
