package ir.chatbot.data.local.prefs

import io.reactivex.Observable


interface PreferencesHelper {



    var _id: String
    var email: String
    var name: String
    var picUrl: String
    var token: String?

    val userLoggedIn: Observable<Boolean>

    fun setUserLoggedIn(userLoggedIn: Boolean)


}
