package ir.chatbot.data.local.db


import androidx.room.Database
import androidx.room.RoomDatabase
import ir.chatbot.data.local.db.dao.ChatRoomDao
import ir.chatbot.data.local.db.dao.MessageDao
import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.data.model.VMMessage

@Database(entities = [VMMessage::class, VMChatRoom::class], exportSchema = false, version = 9)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chatRoomDao(): ChatRoomDao

    abstract fun messageDao(): MessageDao


}
