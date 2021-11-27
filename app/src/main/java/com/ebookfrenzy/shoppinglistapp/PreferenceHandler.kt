package com.ebookfrenzy.shoppinglistapp

import android.content.Context
import androidx.preference.PreferenceManager

//Singleton class to handle preferences from any fragment/activity
object PreferenceHandler {
    private const val SETTINGS_NAMEKEY = "name"
    private const val SETTINGS_NOTIFICATONS = "notifications"


    fun getName(context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SETTINGS_NAMEKEY, "")!!
    }


}