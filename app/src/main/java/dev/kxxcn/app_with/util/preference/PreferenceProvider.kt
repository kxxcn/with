package dev.kxxcn.app_with.util.preference

import android.content.Context
import android.content.SharedPreferences
import dev.kxxcn.app_with.R

object PreferenceProvider {

    private lateinit var instance: SharedPreferences

    fun of(context: Context) {
        instance = context.getSharedPreferences(context.getString(R.string.app_name_en), Context.MODE_PRIVATE)
    }

    fun getInstance() = instance
}
