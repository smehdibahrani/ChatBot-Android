package ir.chatbot.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.Observable
import ir.chatbot.data.DataManager
import ir.chatbot.di.PreferenceInfo
import ir.chatbot.utils.AppConstants


import javax.inject.Inject
import java.util.concurrent.Callable


class AppPreferencesHelper @Inject
constructor(context: Context, @PreferenceInfo prefFileName: String) : PreferencesHelper {


    private val mPrefs: SharedPreferences =
        context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    override var token: String?
        get() = mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null)
        set(accessToken) = mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply()

    override var email: String
        get() = mPrefs.getString(PREF_KEY_USER_NAME, null)!!
        set(userName) = mPrefs.edit().putString(PREF_KEY_USER_NAME, userName).apply()


    override var _id: String
        get() {
            return mPrefs.getString(PREF_KEY_USER_ID, "")!!

        }
        set(userId) {
            mPrefs.edit().putString(PREF_KEY_USER_ID, userId).apply()
        }


    override var name: String
        get() = mPrefs.getString(PREF_KEY_FIRST_NAME, null)!!
        set(firstName) = mPrefs.edit().putString(PREF_KEY_FIRST_NAME, firstName).apply()


    override var picUrl: String
        get() = mPrefs.getString(PREF_KEY_USER_PROFILE_PIC_URL, null)!!
        set(profilePicUrl) = mPrefs.edit().putString(
            PREF_KEY_USER_PROFILE_PIC_URL,
            profilePicUrl
        ).apply()


    override val userLoggedIn: Observable<Boolean>
        get() = Observable.fromCallable {
            mPrefs.getBoolean(PREF_KEY_USER_LOGGED_IN, false)
        }

    override fun setUserLoggedIn(userLoggedIn: Boolean) {
        mPrefs.edit().putBoolean(PREF_KEY_USER_LOGGED_IN, userLoggedIn).apply()
    }

    companion object {

        private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        private const val PREF_KEY_USER_ID = "PREF_KEY_USER_ID"
        private const val PREF_KEY_USER_NAME = "PREF_KEY_USER_NAME"
        private const val PREF_KEY_FIRST_NAME = "PREF_KEY_FIRST_NAME"
        private const val PREF_KEY_USER_PROFILE_PIC_URL = "PREF_KEY_USER_PROFILE_PIC_URL"
        private const val PREF_KEY_USER_LOGGED_IN = "PREF_KEY_USER_LOGGED_IN"


    }
}
