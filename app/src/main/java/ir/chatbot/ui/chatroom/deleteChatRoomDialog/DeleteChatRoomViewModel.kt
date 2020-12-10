package ir.chatbot.ui.chatroom.deleteChatRoomDialog


import com.google.gson.JsonObject
import ir.chatbot.R
import ir.chatbot.data.DataManager
import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.ui.base.BaseViewModel
import ir.chatbot.ui.chatroom.createChatRoomDialog.DeleteChatRoomCallback
import ir.chatbot.utils.rx.SchedulerProvider
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class DeleteChatRoomViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<DeleteChatRoomCallback>(dataManager, schedulerProvider) {

    fun onLaterClick() {
        navigator.dismissDialog()
    }

    fun onSubmitClick() {

        navigator.submit()
    }


    fun apiListChatRoom(name :String) {

        setIsLoading(true)
        val jsonObject = JsonObject()
        jsonObject.addProperty("name",name)
        jsonObject.addProperty("color", (0..14).random())
        dataManager.apiCreateChatRoom(jsonObject).enqueue(object : Callback<VMChatRoom> {

            override fun onResponse(
                call: Call<VMChatRoom>,
                response: Response<VMChatRoom>
            ) {
                setIsLoading(false)
                var messageRes = 0
                var messageStr = ""
                when {
                    response.isSuccessful -> {
                        val chatRoom = response.body()
                        if (chatRoom != null) {
                            compositeDisposable.add(
                                dataManager.insertChatRoom(chatRoom)
                                    .subscribeOn(schedulerProvider.io())
                                    .observeOn(schedulerProvider.ui())
                                    .subscribe({ _ ->
                                        navigator.dismissDialog()
                                    }, { _ ->

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

            override fun onFailure(call: Call<VMChatRoom>, t: Throwable) {
                setIsLoading(false)
                navigator.handleError(R.string.internet_connection_error)

            }
        })


    }
}
