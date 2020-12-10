package ir.chatbot.data.local.db.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.data.model.VMChatRoomMessageJoined
import ir.chatbot.data.model.VMMessage

@Dao
interface ChatRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vmChatRoom: VMChatRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(questions: List<VMChatRoom>)

    @Query("select * from chatrooms " +
            "left join (SELECT type,chatId,data,owner,createAt from messages " +
            "           where createAt in ( SELECT (createAt) FROM messages order by createAt desc) " +
            "           group by chatId) AS a  on chatrooms._id = a.chatId " +
            "left join (SELECT chatId,count(seen) as unread FROM messages " +
            "           where seen = 0 " +
            "   GROUP BY chatId) " +
            "   AS b  on chatrooms._id = b.chatId  ")
    fun loadAll(): List<VMChatRoomMessageJoined>

    @Query("select * from chatrooms")
    fun loadAllQ(): List<VMChatRoom>

    @Query("SELECT * FROM chatrooms WHERE _id = :chatId")
    fun loadByChatId(chatId: String): VMChatRoom

    @Query("delete FROM chatrooms WHERE _id = :chatId")
    fun deleteChatRoomById(chatId: String)


    @Query("delete FROM chatrooms")
    fun clearChatrooms()


}
