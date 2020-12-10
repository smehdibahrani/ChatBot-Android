package ir.chatbot.data.model

class VMChatRoomMessageJoined {


    var _id: String = ""
    var accountId: String = ""
    var name: String = ""
    var color: Int = 0
    var token: String = ""

    ///calculate unread messages
    var unread: Int = 0

    //last message
    var type: String? = ""
    var chatId: String? = ""
    var owner: String? = ""
    var data: String? = ""
    var createAt: Long?= 0


}
