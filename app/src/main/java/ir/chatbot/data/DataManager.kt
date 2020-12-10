package ir.chatbot.data

import ir.chatbot.data.local.db.DbHelper
import ir.chatbot.data.local.prefs.PreferencesHelper
import ir.chatbot.data.remote.ApiService


interface DataManager : DbHelper, PreferencesHelper, ApiService {

    ;
}
