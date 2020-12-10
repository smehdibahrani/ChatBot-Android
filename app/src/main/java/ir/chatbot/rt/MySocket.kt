package ir.chatbot.rt

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.IBinder
import android.provider.Settings

import android.util.Log

import androidx.localbroadcastmanager.content.LocalBroadcastManager

import com.google.gson.Gson
import com.google.gson.JsonObject

import org.json.JSONException
import org.json.JSONObject

import java.util.Locale

import io.socket.client.Ack
import io.socket.emitter.Emitter
import ir.chatbot.data.model.VMMessage
import ir.chatbot.rt.service.Events
import ir.chatbot.rt.service.SocketService
import org.json.JSONArray


class MySocket {
    private var context: Context? = null
    private var socketServiceInterface: SocketService? = null
    private val connectRetry = 10
    private var token = ""
    private val TAG = "Socket"
    private val deviceID: String = Settings.Secure.ANDROID_ID


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            socketServiceInterface = (service as SocketService.LocalBinder).service
            socketServiceInterface!!.setServiceBind(true)

        }

        override fun onServiceDisconnected(name: ComponentName) {
            socketServiceInterface!!.setServiceBind(false)
            socketServiceInterface = null
        }
    }

     val isSocketConnected: Boolean
        get() = if (socketServiceInterface == null) {
            false
        } else socketServiceInterface!!.isSocketConnected


    //// receiver

    private val socketConnectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val connected = intent.getBooleanExtra("connectionStatus", false)
            if (connected) {
                Log.i(TAG, "socket connected")
                authenticateUser()
            } else {
                Log.i(TAG, "socket disconnected")
            }
        }
    }

    private val connectionFailureReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.i(TAG, "socket connection failure")
            if (socketServiceInterface != null)
                socketServiceInterface!!.onDisconnect()
        }
    }

    private val onReadyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (socketServiceInterface != null)
                socketServiceInterface!!.onReady()
        }
    }


    private val onMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val res = intent.getStringExtra("res")
            val message = Gson().fromJson(res, VMMessage::class.java)
            sendMessageDelivered(message._id)
            if (socketServiceInterface != null)
                socketServiceInterface!!.onMessage(message)
        }
    }

    private val onPendingMessagesReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val res = intent.getStringExtra("res")
            val jsonArray = JSONArray(res)
            val len = jsonArray.length() - 1
            val list = ArrayList<VMMessage>()
            // val listIds = ArrayList<String>()

            for (i in 0..len) {
                val message =
                    Gson().fromJson(jsonArray.getJSONObject(i).toString(), VMMessage::class.java)
                list.add(message)
                sendMessageDelivered(message._id)
            }
//            sendPendingMessagesDelivered(listIds)
            if (socketServiceInterface != null)
                socketServiceInterface!!.onPendingMessages(list)
        }
    }

    private val onAuthFailureReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val res = intent.getStringExtra("res")

            Log.e(TAG, res)
            if (socketServiceInterface != null)
                socketServiceInterface!!.onAuthFailure(res)


        }
    }

    private val onErrorReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val res = intent.getStringExtra("res")
            if (socketServiceInterface != null)
                socketServiceInterface!!.onError(res)
            Log.e(TAG, res)
        }
    }


    fun initialize(context: Context, token: String, service: SocketService) {
        this.context = context
        this.token = token
        val intent = Intent(context, service.javaClass)
        try {
            context.startService(intent)
        } catch (ex: Exception) {

            Log.i(TAG, "android not support background service")
            Log.i(TAG, ex.toString())
        }

        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        ////// socket events
        LocalBroadcastManager.getInstance(context).registerReceiver(
            socketConnectionReceiver,
            IntentFilter(Events.SocketConnectionReceiver)
        )
        LocalBroadcastManager.getInstance(context).registerReceiver(
            connectionFailureReceiver,
            IntentFilter(Events.SocketConnectionFailureReceiver)
        )


        LocalBroadcastManager.getInstance(context)
            .registerReceiver(onReadyReceiver, IntentFilter(Events.onReadyReceiver))

        LocalBroadcastManager.getInstance(context)
            .registerReceiver(onMessageReceiver, IntentFilter(Events.onMessageReceiver))

        LocalBroadcastManager.getInstance(context)
            .registerReceiver(
                onPendingMessagesReceiver,
                IntentFilter(Events.onPendingMessagesReceiver)
            )

        LocalBroadcastManager.getInstance(context)
            .registerReceiver(onAuthFailureReceiver, IntentFilter(Events.onAuthFailureReceiver))

        LocalBroadcastManager.getInstance(context)
            .registerReceiver(onErrorReceiver, IntentFilter(Events.onErrorReceiver))


    }

    private fun destroy() {
        socketServiceInterface!!.setServiceBind(false)
        context!!.unbindService(serviceConnection)
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(socketConnectionReceiver)
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(onReadyReceiver)
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(onMessageReceiver)
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(onPendingMessagesReceiver)
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(onAuthFailureReceiver)
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(onErrorReceiver)

    }

    private fun addOnHandler(event: String, listener: Emitter.Listener) {
        socketServiceInterface!!.addOnHandler(event, listener)
    }

    private fun emit(event: String, args: Array<Any>, ack: Ack) {
        socketServiceInterface!!.emit(event, args, ack)
    }

    private fun emit(event: String, vararg args: Any) {
        if (socketServiceInterface != null)
            socketServiceInterface!!.emit(event, *args)
        else
            Log.i(TAG, "socketServiceInterface is null")
    }

    private fun connect() {
        socketServiceInterface!!.connect()
    }

    private fun disconnect() {
        if (socketServiceInterface != null)
            socketServiceInterface!!.disconnect()
    }

    private fun off(event: String) {
        if (socketServiceInterface != null) {
            socketServiceInterface!!.off(event)
        }
    }


    private fun restartSocket() {
        if (socketServiceInterface != null) {
            socketServiceInterface!!.restartSocket()
        }
    }

    //// sender
    fun sendMessage(type: String, data: String, chatRoomToken: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("token", chatRoomToken)
        jsonObject.addProperty("type", type)
        jsonObject.addProperty("data", data)
        emit(Events.eventAppNewMessage, jsonObject.toString())
    }

    fun sendMessageDelivered(messageId: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("messageId", messageId)
        emit(Events.eventMessageReceived, jsonObject.toString())
    }

    fun sendRequestJoin(token: String) {
        emit(Events.eventRequestJoin, token)
    }

    fun sendPendingMessagesDelivered(list: List<String>) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("pendingMessageIds", list.toTypedArray().toString())
        emit(Events.eventPendingMessagesReceived, jsonObject.toString())
    }


    private fun authenticateUser() {
        emit(Events.eventAppAuth, token)
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var sharedInstance: MySocket? = null

        val instance: MySocket
            get() {

                if (sharedInstance == null) {
                    sharedInstance = MySocket()
                }
                return sharedInstance as MySocket


            }
    }
}
