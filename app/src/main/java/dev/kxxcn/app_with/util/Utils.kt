package dev.kxxcn.app_with.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import dev.kxxcn.app_with.R
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    companion object {
        fun getCurrentTime(): String {
            val calendar = Calendar.getInstance()
            val date = calendar.time
            return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        }

        fun getDayOfWeeks(context: Context?, _date: String?): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val cal = Calendar.getInstance()
            cal.time = formatter.parse(_date)
            return when (cal.get(Calendar.DAY_OF_WEEK)) {
                1 -> context?.getString(R.string.label_sun)!!
                2 -> context?.getString(R.string.label_mon)!!
                3 -> context?.getString(R.string.label_tue)!!
                4 -> context?.getString(R.string.label_wed)!!
                5 -> context?.getString(R.string.label_thu)!!
                6 -> context?.getString(R.string.label_fri)!!
                7 -> context?.getString(R.string.label_sat)!!
                else -> ""
            }
        }

        fun convertTimeToDate(time: Long): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return simpleDateFormat.format(time)
        }
    }
}

fun EditText.onChanged(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cb(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}