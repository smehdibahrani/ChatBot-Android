package ir.chatbot.ui.main


interface MainNavigator {

    fun handleError(throwable: Throwable)

    fun openLoginFragment()

    fun openChatRoomsFragment(token:String?)
}
