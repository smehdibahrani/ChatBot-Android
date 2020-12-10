package ir.chatbot.rt

import android.content.Intent
import android.util.Log
import ir.chatbot.App
import ir.chatbot.App.Companion.context
import ir.chatbot.data.AppDataManager
import ir.chatbot.data.DataManager

import org.json.JSONObject

import ir.chatbot.data.model.VMMessage
import ir.chatbot.data.remote.ApiConstants
import ir.chatbot.rt.service.SocketService
import ir.chatbot.ui.chat.ChatFragment
import ir.chatbot.ui.chatroom.ChatRoomsFragment
import ir.chatbot.ui.listeners.ConnectionStatus
import ir.chatbot.ui.main.MainActivity
import ir.chatbot.utils.Helper
import ir.chatbot.utils.NotificationUtil
import javax.inject.Inject
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import io.reactivex.disposables.CompositeDisposable
import ir.chatbot.utils.Notify
import ir.chatbot.utils.rx.SchedulerProvider
import kotlin.math.log


class MyService : SocketService() {


    @Inject
    lateinit var dataManager: DataManager

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    override fun onDisconnect() {
        ChatRoomsFragment.conStatus = ConnectionStatus.OFFLINE
        if (ChatRoomsFragment.connectionListener != null)
            ChatRoomsFragment.connectionListener!!.onOnConnectionChanged(ConnectionStatus.OFFLINE)
    }

    private val TAG = "Socket"

    override fun onReady() {
        Log.i(TAG, "onReady")
        ChatRoomsFragment.conStatus = ConnectionStatus.CONNECTED
        if (ChatRoomsFragment.connectionListener != null)
            ChatRoomsFragment.connectionListener!!.onOnConnectionChanged(ConnectionStatus.CONNECTED)

    }

    override fun onMessage(message: VMMessage) {
        Log.i("rt","got a message");
        handleMessage(message)
    }

    override fun onPendingMessages(messages: List<VMMessage>) {
        for (message in messages) {
            handleMessage(message)
        }
    }

    override fun onAuthFailure(res: String) {
        Log.i(TAG, res)
        if (ChatRoomsFragment.connectionListener != null)
            ChatRoomsFragment.connectionListener!!.onOnConnectionChanged(ConnectionStatus.AUTHENTICATION_FAILURE)
    }

    override fun onError(res: String) {
        Log.i(TAG, res)
    }


    private fun handleMessage(message: VMMessage) {

        message.createAtFormated = Helper.longToDateFormat(message.createAt)
        if (message.type == "photo")
            message.data = ApiConstants.BASE_URL + message.data

        /// save message
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            dataManager.insertMessage(message)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ _ ->

                    if (ChatFragment.listener != null && ChatFragment.chatId == message.chatId)
                        ChatFragment.listener!!.onNewMessage(message)
                    else {
                        if (ChatRoomsFragment.listener != null)
                            ChatRoomsFragment.listener!!.onNewMessage(message)

                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("fromNotification", true)
                        intent.putExtra("chatId", message.chatId)
                        Notify.showNotification(context, message.data, intent)
                    }

                }, { _ ->

                })
        )

    }


}
