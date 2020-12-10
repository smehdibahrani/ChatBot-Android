package ir.chatbot.ui.listeners

import ir.chatbot.data.model.VMMessage

interface OnNewMessageListener {
    fun onNewMessage(message: VMMessage)
}