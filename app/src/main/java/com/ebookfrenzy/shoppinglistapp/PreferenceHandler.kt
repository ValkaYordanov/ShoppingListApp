package com.ebookfrenzy.shoppinglistapp

import android.content.Context
import androidx.preference.PreferenceManager


object PreferenceHandler {
    private const val SETTINGS_NAMEKEY = "name"


    fun getName(context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SETTINGS_NAMEKEY, "")!!
    }


}