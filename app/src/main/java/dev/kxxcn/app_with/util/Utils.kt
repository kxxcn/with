package dev.kxxcn.app_with.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.util.Constants.DAY_COLORS
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    companion object {

        private val FORMAT = DecimalFormat("#,###")

        val TYPE_DATE = 0
        val TYPE_DATE_TIME = 1

        fun getCurrentTime(): String {
            val calendar = Calendar.getInstance()
            val date = calendar.time
            return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        }

        fun getDayOfWeeks(context: Context?, _date: String?): String? {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val cal = Calendar.getInstance()
            cal.time = formatter.parse(_date)
            return when (cal.get(Calendar.DAY_OF_WEEK)) {
                1 -> context?.getString(R.string.label_sun)
                2 -> context?.getString(R.string.label_mon)
                3 -> context?.getString(R.string.label_tue)
                4 -> context?.getString(R.string.label_wed)
                5 -> context?.getString(R.string.label_thu)
                6 -> context?.getString(R.string.label_fri)
                7 -> context?.getString(R.string.label_sat)
                else -> ""
            }
        }

        fun convertTimeToDate(time: Long, type: Int): String {
            val simpleDateFormat = SimpleDateFormat(if (type == TYPE_DATE) "yyyy-MM-dd" else "yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return simpleDateFormat.format(time)
        }

        fun getDDay(context: Context, year: Int, month: Int, day: Int): String {
            val today = Calendar.getInstance()
            val decimalDay = Calendar.getInstance()
            decimalDay.set(year, month, day)
            val longDecimalDay = decimalDay.timeInMillis / (24 * 60 * 60 * 1000)
            val longToday = today.timeInMillis / (24 * 60 * 60 * 1000)

            val count = (longToday - longDecimalDay).toInt()
            if (count > 0) {
                return String.format(context.getString(R.string.text_decimal_day_plus).toString(), count)
            } else if (count == 0) {
                return context.getString(R.string.text_decimal_day).toString()
            }

            return context.getString(R.string.text_decimal_day_minus, count)
        }

        fun getDay(context: Context, year: Int, month: Int, day: Int): String {
            val today = Calendar.getInstance()
            val decimalDay = Calendar.getInstance()
            decimalDay.set(year, month, day)
            val longDecimalDay = decimalDay.timeInMillis / (24 * 60 * 60 * 1000)
            val longToday = today.timeInMillis / (24 * 60 * 60 * 1000)

            val count = (longToday - longDecimalDay).toInt() + 1

            return context.getString(R.string.text_calculate_date, count)
        }

        fun getDayBackgroundColor() = DAY_COLORS[Random().nextInt(DAY_COLORS.size)]

        fun convertSize(size: Int): String = FORMAT.format(size)
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
