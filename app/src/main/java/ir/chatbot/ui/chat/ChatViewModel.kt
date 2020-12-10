package ir.chatbot.ui.chat


import com.google.gson.JsonObject
import ir.chatbot.R
import ir.chatbot.data.DataManager
import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.data.model.VMMessage
import ir.chatbot.data.remote.ApiConstants

import ir.chatbot.ui.base.BaseViewModel
import ir.chatbot.utils.Helper
import ir.chatbot.utils.rx.SchedulerProvider
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class ChatViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<ChatNavigator>(dataManager, schedulerProvider) {


    fun getListMessage(chatId: String) {
        compositeDisposable.add(
            dataManager.loadAllByChatId(chatId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ list ->
                    if (list != null && list.isNotEmpty() && list.size > 1)
                        navigator.updateList(list)
                    else
                        apiListMessages(chatId)
                }, { _ ->

                })
        )
    }


    fun insertMessage(message: VMMessage) {
        compositeDisposable.add(
            dataManager.insertMessage(message)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ _ ->

                }, { _ ->

                })
        )
    }

    fun seenMessagesByChatId(chatId: String) {
        compositeDisposable.add(
            dataManager.seenMessagesByChatId(chatId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ _ ->

                }, { _ ->

                })
        )
    }


    fun apiDeleteMessages(chatId: String) {

        setIsLoading(true)

        dataManager.apiRemoveMessages(chatId).enqueue(object : Callback<JsonObject> {

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
                            dataManager.deleteMessagesByChatId(chatId)
                                .subscribeOn(schedulerProvider.io())
                                .observeOn(schedulerProvider.ui())
                                .subscribe({ _ ->
                                    navigator.deletedMessages()
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
                                    navigator.deletedChatRoom()
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


    fun insertListMessage(messages: List<VMMessage>) {
        try {
            if (messages.isNullOrEmpty())
                return
            for (msg in messages) {
                msg.createAtFormated = Helper.longToDateFormat(msg.createAt)
                if (msg.type == "photo")
                    msg.data = ApiConstants.BASE_URL + msg.data
            }
            compositeDisposable.add(
                dataManager.insertAllMessage(messages)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ _ ->
                        navigator.updateList(messages)
                    }, { _ ->

                    })
            )
        }catch (ex:Exception){

        }
    }

    private fun apiListMessages(chatId: String) {

        setIsLoading(true)

        dataManager.apiListMessages(chatId).enqueue(object : Callback<List<VMMessage>> {

            override fun onResponse(
                call: Call<List<VMMessage>>,
                response: Response<List<VMMessage>>
            ) {
                setIsLoading(false)
                var messageRes = 0
                var messageStr = ""
                when {
                    response.isSuccessful -> {
                        val list = response.body()
                        if (list != null && list.isNotEmpty()) {
                            insertListMessage(list)
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

            override fun onFailure(call: Call<List<VMMessage>>, t: Throwable) {
                setIsLoading(false)
//                navigator.handleError(R.string.internet_connection_error)

            }
        })


    }

}
