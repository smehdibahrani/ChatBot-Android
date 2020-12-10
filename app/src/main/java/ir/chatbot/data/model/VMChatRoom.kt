package ir.chatbot.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "chatrooms")
class VMChatRoom {

    @PrimaryKey
    var _id: String = ""
    var accountId: String = ""
    var name: String = ""
    var color: Int = 0
    var token: String = ""

    @Ignore
    var lastMessage:VMMessage? = null

}
