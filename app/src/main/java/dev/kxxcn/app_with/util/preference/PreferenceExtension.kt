package dev.kxxcn.app_with.util.preference

abstract class PreferenceExtension {

    fun String.putString(value: String?) {
        PreferenceProvider.getInstance().edit().putString(this, value).apply()
    }

    fun String.putInt(value: Int) {
        PreferenceProvider.getInstance().edit().putInt(this, value).apply()
    }

    fun String.putLong(value: Long) {
        PreferenceProvider.getInstance().edit().putLong(this, value).apply()
    }

    fun String.putBoolean(value: Boolean) {
        PreferenceProvider.getInstance().edit().putBoolean(this, value).apply()
    }

    fun String.getString(defValue: String?): String? {
        return PreferenceProvider.getInstance().getString(this, defValue)
    }

    fun String.getInt(defValue: Int): Int {
        return PreferenceProvider.getInstance().getInt(this, defValue)
    }

    fun String.getLong(defValue: Long): Long {
        return PreferenceProvider.getInstance().getLong(this, defValue)
    }

    fun String.getBoolean(defValue: Boolean): Boolean {
        return PreferenceProvider.getInstance().getBoolean(this, defValue)
    }
}
