package ir.chatbot.rt.service

import android.app.ActivityManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder

import android.util.Log

import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.android.AndroidInjection
import dagger.android.DaggerService

import org.json.JSONObject

import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import ir.chatbot.data.model.VMMessage
import ir.chatbot.data.remote.ApiConstants
import javax.inject.Inject


abstract class SocketService : Service() {

    var mSocket: Socket? = null
    private var isServiceBind = false
    private val TAG = "Socket"
    private val mBinder = LocalBinder()

    private val socketID: String
        get() = if (mSocket != null && mSocket!!.connected()) mSocket!!.id() else ""

    val isSocketConnected: Boolean
        get() = mSocket != null && mSocket!!.connected()


    inner class LocalBinder : Binder() {
        val service: SocketService
            get() = this@SocketService
    }

    fun setServiceBind(isServiceBind: Boolean) {
        this.isServiceBind = isServiceBind
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
        initializeSocket()
        addSocketHandlers()

    }

    override fun onDestroy() {
        super.onDestroy()
        closeSocketSession()
    }

    override fun onUnbind(intent: Intent): Boolean {
        return isServiceBind
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    fun initializeSocket() {
        try {
            mSocket = IO.socket(ApiConstants.BASE_URL)
        } catch (e: Exception) {
            Log.e(TAG, "Exception in socket creation")
            throw RuntimeException(e)
        }


    }

    private fun closeSocketSession() {
        mSocket!!.disconnect()
        mSocket!!.off()

    }

    private fun addSocketHandlers() {
        mSocket!!.on(Socket.EVENT_CONNECT) {
            val intent = Intent(Events.SocketConnectionReceiver)

            intent.putExtra("connectionStatus", true)
            intent.putExtra("socketID", socketID)
            broadcastEvent(intent)
        }
        mSocket!!.on(Socket.EVENT_DISCONNECT) {
            val intent = Intent(Events.SocketConnectionFailureReceiver)
            intent.putExtra("connectionStatus", false)
            intent.putExtra("socketID", socketID)
            broadcastEvent(intent)
        }


        mSocket!!.on(Socket.EVENT_CONNECT_ERROR) {
            val intent = Intent(Events.SocketConnectionFailureReceiver)
            broadcastEvent(intent)
        }
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT) {
            val intent = Intent(Events.SocketConnectionFailureReceiver)
            broadcastEvent(intent)
        }

        mSocket!!.on(Events.onReady) { args ->
            val intent = Intent(Events.onReadyReceiver)
            intent.putExtra("res", args[0].toString())
            broadcastEvent(intent)
        }


        mSocket!!.on(Events.onMessage) { args ->
            val intent = Intent(Events.onMessageReceiver)
            intent.putExtra("res", args[0].toString())
            broadcastEvent(intent)
        }

        mSocket!!.on(Events.onPendingMessages) { args ->
            val intent = Intent(Events.onPendingMessagesReceiver)
            intent.putExtra("res", args[0].toString())
            broadcastEvent(intent)
        }


        mSocket!!.on(Events.onAuthFailure) { args ->
            val intent = Intent(Events.onAuthFailureReceiver)
            intent.putExtra("res", args[0].toString())
            broadcastEvent(intent)
        }

        mSocket!!.on(Events.onError) { args ->
            val intent = Intent(Events.onErrorReceiver)
            intent.putExtra("res", args[0].toString())
            broadcastEvent(intent)
        }

        mSocket!!.connect()
    }


    fun removeHandler() {
        mSocket!!.off(Events.onReady)
        mSocket!!.off(Events.onMessage)
        mSocket!!.off(Events.onPendingMessages)
        mSocket!!.off(Events.onAuthFailure)
        mSocket!!.off(Events.onError)
    }

    fun emit(event: String, args: Array<Any>, ack: Ack) {
        if (mSocket != null && mSocket!!.connected()) {
            mSocket!!.emit(event, args, ack)
        }
    }

    fun emit(event: String, vararg args: Any) {
        if (mSocket != null && mSocket!!.connected()) {
            mSocket!!.emit(event, args, null)
        }


    }

    fun addOnHandler(event: String, listener: Emitter.Listener) {
        mSocket!!.on(event, listener)
    }

    fun connect() {
        if (!isSocketConnected)
            mSocket!!.connect()

    }

    fun disconnect() {
        mSocket!!.disconnect()

    }

    fun restartSocket() {
        mSocket!!.off()
        mSocket!!.disconnect()
        addSocketHandlers()
    }

    fun off(event: String) {
        mSocket!!.off(event)
    }

    private fun broadcastEvent(intent: Intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


    fun isForeground(myPackage: String): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTaskInfo = manager.getRunningTasks(1)
        val componentInfo = runningTaskInfo[0].topActivity
        return componentInfo.packageName == myPackage
    }


    abstract fun onReady()

    abstract fun onDisconnect()

    abstract fun onMessage(message: VMMessage)

    abstract fun onPendingMessages(messages: List<VMMessage>)

    abstract fun onAuthFailure(res: String)

    abstract fun onError(res: String)


}
