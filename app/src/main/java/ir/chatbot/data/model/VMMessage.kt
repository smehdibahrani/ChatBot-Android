package ir.chatbot.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "messages",indices = [Index(value = ["_id"], unique = true)])
class VMMessage {

    @PrimaryKey(autoGenerate = true)
    var numId: Long = 0
    var _id: String = ""
    var chatId: String = ""
    var type: String = ""
    var owner: String = ""
    var data: String = ""
    var createAt: Long = 0
    var seen: Int = 0
    var createAtFormated: String = ""


}
