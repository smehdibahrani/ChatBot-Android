package ir.chatbot.ui.chatroom.createChatRoomDialog


interface DeleteChatRoomCallback {

    fun dismissDialog()

    fun  submit()

    fun handleError(message:String)
    fun handleError(message:Int)
}
