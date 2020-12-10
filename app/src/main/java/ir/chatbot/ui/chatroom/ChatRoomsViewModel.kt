package ir.chatbot.ui.chatroom


import android.util.Log
import com.google.gson.JsonObject
import ir.chatbot.R
import ir.chatbot.data.DataManager
import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.data.model.VMChatRoomMessageJoined
import ir.chatbot.data.remote.ApiConstants
import ir.chatbot.ui.base.BaseViewModel
import ir.chatbot.utils.rx.SchedulerProvider
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class ChatRoomsViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<ChatRoomsNavigator>(dataManager, schedulerProvider) {


    fun getListChatRoom() {
        compositeDisposable.add(
            dataManager.loadAllChatRooms()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ list ->
                    if (list != null && list.isNotEmpty() )
                        navigator.updateList(list)
                    else
                        apiListChatRoom()
                }, { t ->
                    Log.i("error", t.message)
                    apiListChatRoom()
                })
        )


    }


    fun getChatRoom(chatId: String) {

        compositeDisposable.add(
            dataManager.loadByChatId(chatId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ chatRoom ->
                    navigator.openChatFragment(chatRoom)
                }, { _ ->

                })
        )
    }

    private fun apiListChatRoom() {

        setIsLoading(true)

        dataManager.apiListChatRoom().enqueue(object : Callback<List<VMChatRoom>> {

            override fun onResponse(
                call: Call<List<VMChatRoom>>,
                response: Response<List<VMChatRoom>>
            ) {
                setIsLoading(false)
                var messageRes = 0
                var messageStr = ""
                when {
                    response.isSuccessful -> {
                        val list = response.body()

                        if (list != null && list.isNotEmpty()) {

                            compositeDisposable.add(
                                dataManager.insertAllChatRooms(list)
                                    .subscribeOn(schedulerProvider.io())
                                    .observeOn(schedulerProvider.ui())
                                    .subscribe({ _ ->
                                        val _list = ArrayList<VMChatRoomMessageJoined>()
                                        for (vmChatRoom in list) {
                                            val item = VMChatRoomMessageJoined()
                                            item._id = vmChatRoom._id
                                            item.accountId = vmChatRoom.accountId
                                            item.name = vmChatRoom.name
                                            item.color = vmChatRoom.color
                                            item.token = vmChatRoom.token
                                            if(vmChatRoom.lastMessage != null) {
                                                item.chatId = vmChatRoom.lastMessage!!.chatId
                                                item.type = vmChatRoom.lastMessage!!.type
                                                item.data = vmChatRoom.lastMessage!!.data
                                                item.createAt = vmChatRoom.lastMessage!!.createAt

                                                if (item.type == "photo") {
                                                    item.data = ApiConstants.BASE_URL + item.data
                                                    vmChatRoom.lastMessage!!.data = ApiConstants.BASE_URL + item.data
                                                }

                                                vmChatRoom.lastMessage!!.seen = 1

                                                compositeDisposable.add(
                                                    dataManager.insertMessage(vmChatRoom.lastMessage!!)
                                                        .subscribeOn(schedulerProvider.io())
                                                        .observeOn(schedulerProvider.ui())
                                                        .subscribe({ _ ->
                                                        }, { _ ->

                                                        }))
                                            }
                                            _list.add(item)
                                        }
                                        navigator.updateList(_list)
                                    }, { t ->
                                        Log.i("error", t.message)
                                    })
                            )


                        }
                        return
                    }
                    response.code() == 404 -> messageRes = R.string.server_connect_error
                    response.code() >= 500 -> messageRes = R.string.server_error
                    else -> try {
                        val jsonObject1 = JSONObject(response.errorBody()!!.string())
                        messageStr = jsonObject1.getString("error")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                if (messageRes == 0)
                    navigator.handleError(messageStr)
                else
                    navigator.handleError(messageRes)
            }

            override fun onFailure(call: Call<List<VMChatRoom>>, t: Throwable) {
                setIsLoading(false)
                navigator.handleError(R.string.internet_connection_error)

            }
        })


    }


    fun apiDeleteChatRoom(chatId: String) {

        setIsLoading(true)

        dataManager.apiRemoveChatRoom(chatId).enqueue(object : Callback<JsonObject> {

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                setIsLoading(false)
                var messageRes = 0
                var messageStr = ""
                when {
                    response.isSuccessful -> {
                        compositeDisposable.add(
                            dataManager.deleteChatRoomById(chatId)
                                .subscribeOn(schedulerProvider.io())
                                .observeOn(schedulerProvider.ui())
                                .subscribe({ _ ->
                                    navigator.deleteChatRoom()
                                }, { _ ->

                                })
                        )
                        return
                    }
                    response.code() == 404 -> messageRes = R.string.server_connect_error
                    response.code() >= 500 -> messageRes = R.string.server_error
                    else -> try {
                        val jsonObject1 = JSONObject(response.errorBody()!!.string())
                        messageStr = jsonObject1.getString("error")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                if (messageRes == 0)
                    navigator.handleError(messageStr)
                else
                    navigator.handleError(messageRes)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                setIsLoading(false)
                navigator.handleError(R.string.internet_connection_error)

            }
        })


    }


    fun logout() {

        compositeDisposable.add(
            dataManager.clearAll()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ _ ->
                    dataManager.setUserLoggedIn(false)
                    navigator.logout()
                }, { _ ->


                })
        )
    }


}
