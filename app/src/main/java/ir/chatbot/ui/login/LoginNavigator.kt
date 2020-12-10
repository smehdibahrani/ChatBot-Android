package ir.chatbot.ui.login





interface LoginNavigator {

    fun handleError(message:String)
    fun handleError(message:Int)
    fun openChatRoomsFragment()


}
