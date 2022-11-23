package com.thoughtworks.androidtrain.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtils {
    fun writeBoolean(activity: Activity, key: String, value: Boolean) {
        val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun readBoolean(activity: Activity, key: String, defaultValue: Boolean): Boolean {
        val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(key, defaultValue)
    }
}