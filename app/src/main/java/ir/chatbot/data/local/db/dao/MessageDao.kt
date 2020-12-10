
package ir.chatbot.data.local.db.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.chatbot.data.model.VMMessage


@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vmMessage: VMMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<VMMessage>)

    @Query("SELECT * FROM messages")
    fun loadAll(): List<VMMessage>

    @Query("SELECT * FROM messages WHERE chatId = :chatId")
    fun loadAllByChatId(chatId: String): List<VMMessage>

    @Query("delete FROM messages WHERE chatId = :chatId")
    fun deleteMessagesByChatId(chatId: String)


    @Query("update messages set seen = 1 where chatId = :chatId")
    fun seenMessagesByChatId(chatId: String)

    @Query("delete FROM  messages")
    fun clearMessages()
}
