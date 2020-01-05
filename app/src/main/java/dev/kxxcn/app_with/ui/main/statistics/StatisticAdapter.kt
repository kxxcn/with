package dev.kxxcn.app_with.ui.main.statistics

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import at.grabner.circleprogress.CircleProgressView
import com.app.progresviews.ProgressLine
import com.app.progresviews.ProgressWheel
import com.crashlytics.android.Crashlytics
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.model.diary.Diary
import dev.kxxcn.app_with.util.Constants
import dev.kxxcn.app_with.util.RoundedBarChartRender
import dev.kxxcn.app_with.util.Utils
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList

class StatisticAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<StatisticItem>()
    private val diaryList = ArrayList<Diary>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_TOTAL -> {
                TotalHolder(inflater.inflate(R.layout.item_statistic_total, parent, false))
            }
            TYPE_INSIGHT -> {
                InsightHolder(inflater.inflate(R.layout.item_statistic_insight, parent, false))
            }
            TYPE_ANALYSIS -> {
                AnalysisHolder(inflater.inflate(R.layout.item_statistic_analysis, parent, false))
            }
            TYPE_WEATHER -> {
                WeatherHolder(inflater.inflate(R.layout.item_statistic_weather, parent, false))
            }
            else -> {
                TotalHolder(inflater.inflate(R.layout.item_statistic_total, parent, false))
            }
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (h) {
            is TotalHolder -> {
                h.totalSize.text = Utils.convertSize(item.diary.size)
            }
            is InsightHolder -> {
                val ctx = h.itemView.context
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1
                val yearList = item.diary.filter {
                    try {
                        val date = it.letterDate.split("-")
                        year == date[0].toInt()
                    } catch (e: Exception) {
                        Crashlytics.logException(RuntimeException(ctx.getString(R.string.exception_message_casting, "YEAR", it.id)))
                        return
                    }
                }
                val monthList = item.diary.filter {
                    try {
                        val date = it.letterDate.split("-")
                        month == date[1].toInt()
                    } catch (e: Exception) {
                        Crashlytics.logException(RuntimeException(ctx.getString(R.string.exception_message_casting, "MONTH", it.id)))
                        return
                    }
                }

                val weekStartDate = Calendar.getInstance()
                weekStartDate.set(Calendar.HOUR_OF_DAY, 0)
                weekStartDate.set(Calendar.MINUTE, 0)
                weekStartDate.set(Calendar.SECOND, 0)
                weekStartDate.add(Calendar.DATE, 1 - weekStartDate.get(Calendar.DAY_OF_WEEK))

                val weekEndDate = Calendar.getInstance()
                weekEndDate.set(Calendar.HOUR_OF_DAY, 0)
                weekEndDate.set(Calendar.MINUTE, 0)
                weekEndDate.set(Calendar.SECOND, 0)
                weekEndDate.add(Calendar.DATE, 7 - weekEndDate.get(Calendar.DAY_OF_WEEK))

                val weekList = item.diary.filter {
                    try {
                        val date = it.letterDate.split("-")
                        val compareDate = Calendar.getInstance()
                        compareDate.set(Calendar.YEAR, date[0].toInt())
                        compareDate.set(Calendar.MONTH, date[1].toInt() - 1)
                        compareDate.set(Calendar.DAY_OF_MONTH, date[2].toInt())
                        compareDate.set(Calendar.HOUR_OF_DAY, 0)
                        compareDate.set(Calendar.MINUTE, 0)
                        compareDate.set(Calendar.SECOND, 0)
                        compareDate.timeInMillis >= weekStartDate.timeInMillis && compareDate.timeInMillis <= weekEndDate.timeInMillis
                    } catch (e: Exception) {
                        Crashlytics.logException(RuntimeException(ctx.getString(R.string.exception_message_casting, "WEEK", it.id)))
                        return
                    }
                }
                val yearPercent = if (item.diary.isNotEmpty()) {
                    (yearList.size.toDouble() / item.diary.size.toDouble() * 100).toFloat()
                } else {
                    0f
                }
                val monthPercent = if (yearList.isNotEmpty()) {
                    (monthList.size.toDouble() / yearList.size.toDouble() * 100).toFloat()
                } else {
                    0f
                }
                val weekPercent = if (monthList.isNotEmpty()) {
                    (weekList.size.toDouble() / monthList.size.toDouble() * 100).toFloat()
                } else {
                    0f
                }
                h.yearProgress.setValueAnimated(yearPercent)
                h.monthProgress.setValueAnimated(monthPercent)
                h.weekProgress.setValueAnimated(weekPercent)
            }
            is AnalysisHolder -> {
                val map = mutableMapOf(
                        Calendar.SUNDAY to 0f,
                        Calendar.MONDAY to 0f,
                        Calendar.TUESDAY to 0f,
                        Calendar.WEDNESDAY to 0f,
                        Calendar.THURSDAY to 0f,
                        Calendar.FRIDAY to 0f,
                        Calendar.SATURDAY to 0f)

                item.diary.forEach {
                    val date = it.letterDate.split("-")
                    val compareDate = Calendar.getInstance()
                    compareDate.set(Calendar.YEAR, date[0].toInt())
                    compareDate.set(Calendar.MONTH, date[1].toInt() - 1)
                    compareDate.set(Calendar.DAY_OF_MONTH, date[2].toInt())
                    compareDate.set(Calendar.HOUR_OF_DAY, 0)
                    compareDate.set(Calendar.MINUTE, 0)
                    compareDate.set(Calendar.SECOND, 0)

                    val key = compareDate.get(Calendar.DAY_OF_WEEK)
                    val count = map[key] ?: 0f
                    map[key] = count + 1
                }
                val entries = arrayListOf<BarEntry>()
                map.forEach {
                    val percent = if (item.diary.isEmpty()) 0f else it.value / item.diary.size * 100
                    entries.add(BarEntry(it.key.toFloat(), percent))
                }
                val analysisSet = BarDataSet(entries, null)
                val ctx = h.itemView.context
                analysisSet.color = ContextCompat.getColor(ctx, R.color.bar_background)
                val barData = BarData(analysisSet)
                barData.barWidth = 0.3f
                barData.setDrawValues(false)
                h.barChart.data = barData
                h.barChart.animateY(1000)
                h.barChart.invalidate()

                val attendanceList = item.diary.filter {
                    val date = it.letterDate.split("-")
                    date[1].toInt() == Calendar.getInstance().get(Calendar.MONTH) + 1
                }.distinctBy {
                    val date = it.letterDate.split("-")
                    date[2]
                }

                val calendar = Calendar.getInstance()
                val dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH).toDouble()
                val attendance = attendanceList.size.toDouble()
                val tmp = attendance / dayOfMonth * 100
                val percent = 360 * tmp / 100
                h.attendance.setPercentage(percent.toInt())
                h.attendance.setStepCountText(ctx.getString(R.string.text_attendance_days, attendance.toInt()))
                h.attendance.setDefText(ctx.getString(R.string.text_attendance_month, dayOfMonth.toInt()))
            }
            is WeatherHolder -> {
                val total = item.diary.size
                val sizeOfSun = item.diary.filter { it.letterWeather == Constants.WEATHER_SUN }.size
                val sizeOfCloud = item.diary.filter { it.letterWeather == Constants.WEATHER_CLOUD }.size
                val sizeOfRain = item.diary.filter { it.letterWeather == Constants.WEATHER_RAIN }.size
                val sizeOfSnow = item.diary.filter { it.letterWeather == Constants.WEATHER_SNOW }.size
                h.sunPl.setmValueText(sizeOfSun)
                h.cloudPl.setmValueText(sizeOfCloud)
                h.rainPl.setmValueText(sizeOfRain)
                h.snowPl.setmValueText(sizeOfSnow)

                if (total == 0) {
                    h.sunPl.setmPercentage(0)
                    h.cloudPl.setmPercentage(0)
                    h.rainPl.setmPercentage(0)
                    h.snowPl.setmPercentage(0)
                } else {
                    h.sunPl.setmPercentage((sizeOfSun.toDouble() / total.toDouble() * 100).toInt())
                    h.cloudPl.setmPercentage((sizeOfCloud.toDouble() / total.toDouble() * 100).toInt())
                    h.rainPl.setmPercentage((sizeOfRain.toDouble() / total.toDouble() * 100).toInt())
                    h.snowPl.setmPercentage((sizeOfSnow.toDouble() / total.toDouble() * 100).toInt())
                }
            }
        }
    }

    override fun getItemViewType(position: Int) = items[position].type

    private fun makeItems(diary: List<Diary>, identifier: String?, isMine: Boolean): List<StatisticItem> {
        val items = ArrayList<StatisticItem>()

        val filteredList =
                diary.filter {
                    if (isMine) {
                        it.writer == identifier
                    } else {
                        it.writer != identifier
                    }
                }

        items.add(StatisticItem(TYPE_TOTAL, filteredList))
        items.add(StatisticItem(TYPE_INSIGHT, filteredList))
        items.add(StatisticItem(TYPE_ANALYSIS, filteredList))
        items.add(StatisticItem(TYPE_WEATHER, filteredList))
        return items
    }

    private fun notifyDataSetChanged(identifier: String?, isMine: Boolean) {
        items.clear()
        items.addAll(makeItems(diaryList, identifier, isMine))
        notifyDataSetChanged()
    }

    fun setItems(_diaryList: List<Diary>, identifier: String?, isMine: Boolean) {
        diaryList.addAll(_diaryList)
        notifyDataSetChanged(identifier, isMine)
    }

    fun getItem(pos: Int) = items[pos]

    fun changeItems(identifier: String?, isMine: Boolean) {
        notifyDataSetChanged(identifier, isMine)
    }

    class TotalHolder(
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val totalSize: TextView = itemView.findViewById(R.id.tv_total)
    }

    class InsightHolder(
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val yearProgress: CircleProgressView = itemView.findViewById(R.id.cpv_year)
        val monthProgress: CircleProgressView = itemView.findViewById(R.id.cpv_month)
        val weekProgress: CircleProgressView = itemView.findViewById(R.id.cpv_week)

        init {
            yearProgress.onClick { showToast(it) }
            monthProgress.onClick { showToast(it) }
            weekProgress.onClick { showToast(it) }
        }

        private fun showToast(v: View?) {
            v ?: return
            val context = itemView.context ?: return
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val text = when (v.id) {
                R.id.cpv_year -> context.getString(R.string.toast_insight_year, year)
                R.id.cpv_month -> context.getString(R.string.toast_insight_month, year, month)
                R.id.cpv_week -> context.getString(R.string.toast_insight_week, month)
                else -> context.getString(R.string.toast_insight_year, year)
            }
            context.toast(text)
        }
    }

    class AnalysisHolder(
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val barChart: BarChart = itemView.findViewById(R.id.bc_analysis)
        val attendance: ProgressWheel = itemView.findViewById(R.id.pw_attendance)

        init {
            barChart.setPinchZoom(false)
            barChart.description = null
            barChart.renderer = RoundedBarChartRender(barChart, barChart.animator, barChart.viewPortHandler, 15f)

            val legend = barChart.legend
            legend.isEnabled = false

            val xAxis = barChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.valueFormatter = DayValueFormatter(itemView.context)

            barChart.axisRight.apply {
                setDrawLabels(false)
                setDrawAxisLine(false)
                setDrawGridLines(false)
                setLabelCount(2, true)
                axisMinimum = 0f
                axisMaximum = 100f
            }

            barChart.axisLeft.apply {
                setDrawLabels(false)
                setDrawAxisLine(false)
                setDrawGridLines(false)
                setLabelCount(2, true)
                axisMinimum = 0f
                axisMaximum = 100f
            }
        }
    }

    class WeatherHolder(
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val sunPl: ProgressLine = itemView.findViewById(R.id.pl_sun)
        val cloudPl: ProgressLine = itemView.findViewById(R.id.pl_cloud)
        val rainPl: ProgressLine = itemView.findViewById(R.id.pl_rain)
        val snowPl: ProgressLine = itemView.findViewById(R.id.pl_snow)
    }

    class DayValueFormatter(
            private val context: Context
    ) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return when (value.toInt()) {
                Calendar.SUNDAY -> context.getString(R.string.analysis_sun)
                Calendar.MONDAY -> context.getString(R.string.analysis_mon)
                Calendar.TUESDAY -> context.getString(R.string.analysis_tue)
                Calendar.WEDNESDAY -> context.getString(R.string.analysis_wed)
                Calendar.THURSDAY -> context.getString(R.string.analysis_thu)
                Calendar.FRIDAY -> context.getString(R.string.analysis_fri)
                Calendar.SATURDAY -> context.getString(R.string.analysis_sat)
                else -> context.getString(R.string.analysis_sun)
            }
        }
    }

    class StatisticItem(val type: Int, val diary: List<Diary>)

    companion object {

        private const val TYPE_TOTAL = 0

        private const val TYPE_INSIGHT = 1

        private const val TYPE_ANALYSIS = 2

        private const val TYPE_WEATHER = 3
    }
}
