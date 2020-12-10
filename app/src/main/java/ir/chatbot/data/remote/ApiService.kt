package ir.chatbot.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import ir.chatbot.data.model.VMAccount
import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.data.model.VMMessage
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/login")
    fun apiLogin(@Body jsonObject: JsonObject): Call<VMAccount>


    @Headers("Content-Type: application/json")
    @POST("api/chatroom/create")
    fun apiCreateChatRoom(@Body jsonObject: JsonObject): Call<VMChatRoom>


    @Headers("Content-Type: application/json")
    @GET("api/chatroom/list")
    fun apiListChatRoom(): Call<List<VMChatRoom>>

    @Headers("Content-Type: application/json")
    @GET("api/message/list/{chatId}")
    fun apiListMessages(@Path("chatId") chatId:String): Call<List<VMMessage>>

    @Headers("Content-Type: application/json")
    @DELETE("api/chatroom/remove/{chatId}")
    fun apiRemoveChatRoom(@Path("chatId") chatId:String): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @DELETE("api/message/remove/{chatId}")
    fun apiRemoveMessages(@Path("chatId") chatId:String): Call<JsonObject>

}
