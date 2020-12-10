package ir.chatbot.ui.chatroom

import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.data.model.VMChatRoomMessageJoined


interface ChatRoomsNavigator {


    fun handleError(message: String)
    fun handleError(message: Int)
    fun updateList(list: List<VMChatRoomMessageJoined>)
    fun deleteChatRoom()
    fun openChatFragment(chatRoom: VMChatRoom)
    fun logout()


}
