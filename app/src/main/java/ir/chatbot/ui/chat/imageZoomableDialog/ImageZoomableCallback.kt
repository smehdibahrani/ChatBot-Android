package ir.chatbot.ui.chat.imageZoomableDialog


interface ImageZoomableCallback {

    fun dismissDialog()

    fun  submit()

    fun handleError(message:String)
    fun handleError(message:Int)
}
