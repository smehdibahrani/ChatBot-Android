package ir.chatbot.ui.login

import com.google.gson.JsonObject
import ir.chatbot.App
import ir.chatbot.R
import ir.chatbot.data.DataManager
import ir.chatbot.data.model.VMAccount
import ir.chatbot.rt.MyService
import ir.chatbot.rt.MySocket

import ir.chatbot.ui.base.BaseViewModel
import ir.chatbot.utils.rx.SchedulerProvider
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class LoginViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<LoginNavigator>(dataManager, schedulerProvider) {


    fun apiLogin(email: String, name: String, picUrl: String) {

        setIsLoading(true)
        val jsonObject = JsonObject()
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("name", name)
        jsonObject.addProperty("picUrl", picUrl)

        dataManager.apiLogin(jsonObject).enqueue(object : Callback<VMAccount> {

            override fun onResponse(call: Call<VMAccount>, response: Response<VMAccount>) {
                setIsLoading(false)
                var messageRes = 0
                var messageStr = ""
                when {
                    response.isSuccessful -> {
                        dataManager._id = response.body()!!._id
                        dataManager.email = response.body()!!.email
                        dataManager.name = response.body()!!.name
                        dataManager.picUrl = response.body()!!.picUrl
                        dataManager.token = response.body()!!.token
                        App.token = response.body()!!.token
                        dataManager.setUserLoggedIn(true)

                     //   MySocket.instance.initialize(App.context, App.token, MyService())

                        return navigator.openChatRoomsFragment()
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

            override fun onFailure(call: Call<VMAccount>, t: Throwable) {
                setIsLoading(false)
                navigator.handleError(R.string.internet_connection_error)

            }
        })


    }


}
