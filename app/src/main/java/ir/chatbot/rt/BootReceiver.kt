package ir.chatbot.rt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import ir.chatbot.rt.service.SocketService


class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(SocketService::class.java.name)
        context.startService(serviceIntent)
    }
}
