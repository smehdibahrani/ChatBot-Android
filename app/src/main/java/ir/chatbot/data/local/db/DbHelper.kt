package ir.chatbot.data.local.db

import io.reactivex.Observable
import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.data.model.VMChatRoomMessageJoined
import ir.chatbot.data.model.VMMessage


interface DbHelper {


    fun insertChatRoom(chatRoom: VMChatRoom): Observable<Boolean>

    fun insertAllChatRooms(chatRoomList: List<VMChatRoom>): Observable<Boolean>

    fun loadAllChatRooms(): Observable<List<VMChatRoomMessageJoined>>


    fun loadByChatId(chatId: String): Observable<VMChatRoom>

    fun insertMessage(message: VMMessage): Observable<Boolean>

    fun insertAllMessage(messageList: List<VMMessage>): Observable<Boolean>

    fun loadAllMessage(): Observable<List<VMMessage>>

    fun loadAllByChatId(chatId: String): Observable<List<VMMessage>>


    fun deleteMessagesByChatId(chatId: String): Observable<Boolean>

    fun seenMessagesByChatId(chatId: String): Observable<Boolean>



    fun deleteChatRoomById(chatId: String): Observable<Boolean>



    fun clearAll(): Observable<Boolean>



}
