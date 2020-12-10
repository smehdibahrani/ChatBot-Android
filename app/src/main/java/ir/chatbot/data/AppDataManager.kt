package ir.chatbot.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Observable
import ir.chatbot.data.local.db.DbHelper
import ir.chatbot.data.local.prefs.PreferencesHelper
import ir.chatbot.data.model.VMAccount
import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.data.model.VMChatRoomMessageJoined
import ir.chatbot.data.model.VMMessage
import ir.chatbot.data.remote.ApiService
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call


import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppDataManager @Inject
constructor(
    private val mContext: Context,
    val mDbHelper: DbHelper,
    private val mPreferencesHelper: PreferencesHelper,
    private val mApiService: ApiService,
    private val mGson: Gson
) : DataManager {


    override var token: String?
        get() = mPreferencesHelper.token
        set(accessToken) {
            mPreferencesHelper.token = accessToken
        }


    override var email: String
        get() = mPreferencesHelper.email
        set(username) {
            mPreferencesHelper.email = username
        }

    override var name: String
        get() = mPreferencesHelper.name
        set(firstName) {
            mPreferencesHelper.name = firstName
        }


    override var _id: String
        get() = mPreferencesHelper._id
        set(userId) {
            mPreferencesHelper._id = userId
        }

    override val userLoggedIn: Observable<Boolean>
        get() = mPreferencesHelper.userLoggedIn


    override var picUrl: String
        get() = mPreferencesHelper.picUrl
        set(profilePicUrl) {
            mPreferencesHelper.picUrl = profilePicUrl
        }


    override fun setUserLoggedIn(userLoggedIn: Boolean) {
        mPreferencesHelper.setUserLoggedIn(userLoggedIn)
    }


    override fun insertChatRoom(chatRoom: VMChatRoom): Observable<Boolean> {
        return mDbHelper.insertChatRoom(chatRoom)
    }

    override fun insertAllChatRooms(chatRoomList: List<VMChatRoom>): Observable<Boolean> {
        return mDbHelper.insertAllChatRooms(chatRoomList)
    }

    override fun loadAllChatRooms(): Observable<List<VMChatRoomMessageJoined>> {
        return mDbHelper.loadAllChatRooms()
    }

    override fun insertMessage(message: VMMessage): Observable<Boolean> {
        return mDbHelper.insertMessage(message)
    }

    override fun insertAllMessage(messageList: List<VMMessage>): Observable<Boolean> {
        return mDbHelper.insertAllMessage(messageList)
    }

    override fun loadAllMessage(): Observable<List<VMMessage>> {
        return mDbHelper.loadAllMessage()
    }

    override fun loadAllByChatId(chatId: String): Observable<List<VMMessage>> {
        return mDbHelper.loadAllByChatId(chatId)
    }

    override fun loadByChatId(chatId: String): Observable<VMChatRoom> {
        return mDbHelper.loadByChatId(chatId)
    }

     override fun deleteMessagesByChatId(chatId: String): Observable<Boolean> {
        return mDbHelper.deleteMessagesByChatId(chatId)
    }

    override fun deleteChatRoomById(chatId: String): Observable<Boolean> {
        return mDbHelper.deleteChatRoomById(chatId)
    }

    override fun seenMessagesByChatId(chatId: String): Observable<Boolean> {
        return mDbHelper.seenMessagesByChatId(chatId)
    }


    override fun clearAll(): Observable<Boolean> {
        return mDbHelper.clearAll()
    }

    override fun apiLogin(jsonObject: JsonObject): Call<VMAccount> {
        return mApiService.apiLogin(jsonObject)
    }


    override fun apiCreateChatRoom(jsonObject: JsonObject): Call<VMChatRoom> {
        return mApiService.apiCreateChatRoom(jsonObject)
    }

    override fun apiListChatRoom(): Call<List<VMChatRoom>> {
        return mApiService.apiListChatRoom()
    }

    override fun apiRemoveChatRoom(chatId: String): Call<JsonObject> {
        return mApiService.apiRemoveChatRoom(chatId)
    }

    override fun apiListMessages(chatId: String): Call<List<VMMessage>> {
        return mApiService.apiListMessages(chatId)
    }


    override fun apiRemoveMessages(chatId: String): Call<JsonObject> {
        return mApiService.apiRemoveMessages(chatId)
    }

}
