package ir.chatbot.ui.chat

import ir.chatbot.data.model.VMMessage


interface ChatNavigator {

    fun handleError(message: String)

    fun handleError(message: Int)

    fun deletedChatRoom()
    fun deletedMessages()

    fun updateList(list: List<VMMessage>)




}
