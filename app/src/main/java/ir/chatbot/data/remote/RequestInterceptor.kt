package ir.chatbot.data.remote

import ir.chatbot.App
import ir.chatbot.data.DataManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

import java.io.IOException
import javax.inject.Inject

class RequestInterceptor : Interceptor {





    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()
            .addHeader("Content-Type", " application/json")
            .addHeader("auth", App.token!!)
            .build()

        return chain.proceed(request)

    }
}