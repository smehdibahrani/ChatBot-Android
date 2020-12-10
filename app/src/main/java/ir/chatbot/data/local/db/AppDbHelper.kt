package ir.chatbot.data.local.db


import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.data.model.VMMessage
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import java.util.concurrent.Callable

import io.reactivex.Observable
import ir.chatbot.data.model.VMChatRoomMessageJoined


@Singleton
class AppDbHelper @Inject
constructor(private val mAppDatabase: AppDatabase) : DbHelper {




    override fun deleteMessagesByChatId(chatId: String): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.messageDao().deleteMessagesByChatId(chatId)
            true
        }
    }


    override fun deleteChatRoomById(chatId: String): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.chatRoomDao().deleteChatRoomById(chatId)
            mAppDatabase.messageDao().deleteMessagesByChatId(chatId)
            true
        }
    }



    override fun clearAll(): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.chatRoomDao().clearChatrooms()
            mAppDatabase.messageDao().clearMessages()
            true
        }
    }


    override fun insertChatRoom(chatRoom: VMChatRoom): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.chatRoomDao().insert(chatRoom)
            true
        }


    }

    override fun insertAllChatRooms(chatRoomList: List<VMChatRoom>): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.chatRoomDao().insertAll(chatRoomList)
            true
        }

    }

    override fun loadAllChatRooms(): Observable<List<VMChatRoomMessageJoined>> {
        return Observable.fromCallable { mAppDatabase.chatRoomDao().loadAll() }
    }


    override fun loadByChatId(chatId: String): Observable<VMChatRoom> {
        return Observable.fromCallable { mAppDatabase.chatRoomDao().loadByChatId(chatId) }
    }

    override fun insertMessage(message: VMMessage): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.messageDao().insert(message)
            true
        }

    }

    override fun insertAllMessage(messageList: List<VMMessage>): Observable<Boolean> {
        return Observable.fromCallable {
            mAppDatabase.messageDao().insertAll(messageList)
            true
        }

    }

    override fun loadAllMessage(): Observable<List<VMMessage>> {
        return Observable.fromCallable { mAppDatabase.messageDao().loadAll() }

    }

    override fun seenMessagesByChatId(chatId: String): Observable<Boolean> {
        return Observable.fromCallable { mAppDatabase.messageDao().seenMessagesByChatId(chatId)
            true }
    }

    override fun loadAllByChatId(chatId: String): Observable<List<VMMessage>> {
        return Observable.fromCallable { mAppDatabase.messageDao().loadAllByChatId(chatId) }

    }


}
