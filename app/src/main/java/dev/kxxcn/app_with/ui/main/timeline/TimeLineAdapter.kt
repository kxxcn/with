package dev.kxxcn.app_with.ui.main.timeline

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ablanco.zoomy.Zoomy
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.model.diary.Diary
import dev.kxxcn.app_with.data.remote.APIPersistence.IMAGES_URL
import dev.kxxcn.app_with.util.Constants.*
import dev.kxxcn.app_with.util.ImageProcessingHelper
import dev.kxxcn.app_with.util.Utils
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.lang.ref.WeakReference

class TimeLineAdapter(
        private val activity: Activity,
        private val identifier: String,
        private val callback: TimeLineContract.ItemClick
) : RecyclerView.Adapter<TimeLineAdapter.ViewHolder>() {

    private var diaryList: List<Diary>? = null

    private var filteredList: List<Diary>? = null

    private var refRecyclerView: WeakReference<RecyclerView>? = null

    private val options = RequestOptions
            .fitCenterTransform()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        refRecyclerView = WeakReference(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        refRecyclerView = null
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_timeline, parent, false), callback)

    override fun getItemCount() = filteredList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList?.get(holder.adapterPosition) ?: return
        val context = holder.itemView.context
//        val anim = AnimationUtils.loadAnimation(context, R.anim.slide_bottom_top2)
//        holder.itemView.startAnimation(anim)
        val dates = item.letterDate?.split("-") ?: return
        holder.contentTv.text = item.letter
        holder.dayOfWeeksTv.text = Utils.getDayOfWeeks(context, item.letterDate)
        holder.dayTv.text = String.format(context?.getString(R.string.text_timeline_date)!!, dates[1].toInt(), dates[2].toInt())

        if (TextUtils.isEmpty(item.letterTime)) {
            holder.timeTv.text = context.getString(R.string.text_not_exist_time_information)
        } else {
            holder.timeTv.text = item.letterTime
        }

        holder.line.visibility = if (position == 0) {
            View.GONE
        } else {
            View.VISIBLE
        }

        holder.line2.visibility = if (itemCount == 1) {
            View.GONE
        } else {
            View.VISIBLE
        }

        if (item.writer == identifier) {
            holder.editIv.visibility = View.VISIBLE
            holder.timeBackgroundIv.background = ContextCompat.getDrawable(context, R.drawable.drawable_rectangle_timeline_mine)
            holder.circleIv.background = ContextCompat.getDrawable(context, R.drawable.drawable_circle_timeline_mine)
        } else {
            holder.editIv.visibility = View.GONE
            holder.timeBackgroundIv.background = ContextCompat.getDrawable(context, R.drawable.drawable_rectangle_timeline_yours)
            holder.circleIv.background = ContextCompat.getDrawable(context, R.drawable.drawable_circle_timeline_yours)
        }

        when {
            item.primaryPosition != -1 -> {
                Glide.with(context).clear(holder.backgroundIv)
                holder.backgroundIv.setBackgroundColor(ContextCompat.getColor(context, COLORS[item.primaryPosition]))
            }
            item.galleryName.isNotEmpty() -> {
                val galleryName = item.galleryName
                holder.backgroundIv.layout(0, 0, 0, 0)
                ImageProcessingHelper.setGlide(
                        context,
                        String.format(context.getString(R.string.param_download_image_url), IMAGES_URL, galleryName),
                        holder.backgroundIv,
                        options)
                Zoomy.Builder(activity).target(holder.backgroundIv).register()
            }
            else -> {
                Glide.with(context).clear(holder.backgroundIv)
                holder.backgroundIv.setBackgroundResource(COLOR_DEFAULT[0])
            }
        }

        holder.weatherIv.background = when {
            item.letterWeather == WEATHER_SUN -> ContextCompat.getDrawable(context, R.drawable.ic_sun)
            item.letterWeather == WEATHER_CLOUD -> ContextCompat.getDrawable(context, R.drawable.ic_cloud)
            item.letterWeather == WEATHER_RAIN -> ContextCompat.getDrawable(context, R.drawable.ic_rain)
            item.letterWeather == WEATHER_SNOW -> ContextCompat.getDrawable(context, R.drawable.ic_snow)
            else -> null
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    fun setItems(_diaryList: List<Diary>) {
        diaryList = _diaryList
        filteredList = _diaryList.filter { it.writer == identifier }
        notifyDataSetChanged()
        val recyclerView = refRecyclerView?.get()
        recyclerView?.scheduleLayoutAnimation()
    }

    fun changeDiary(isMine: Boolean) {
        filteredList = if (isMine) {
            diaryList?.filter { it.writer == identifier }
        } else {
            diaryList?.filter { it.writer != identifier }
        }
        notifyDataSetChanged()
        val recyclerView = refRecyclerView?.get()
        recyclerView?.scheduleLayoutAnimation()
    }

    fun getItem(position: Int): Diary? = filteredList?.get(position)

    class ViewHolder(itemView: View, callback: TimeLineContract.ItemClick) : RecyclerView.ViewHolder(itemView) {
        val circleIv: ImageView = itemView.findViewById(R.id.iv_circle)
        val line: View = itemView.findViewById(R.id.view_line)
        val line2: View = itemView.findViewById(R.id.view_line2)
        val backgroundIv: ImageView = itemView.findViewById(R.id.iv_background)
        val timeBackgroundIv: ImageView = itemView.findViewById(R.id.iv_time_background)
        val contentTv: TextView = itemView.findViewById(R.id.tv_content)
        val detailTv: TextView = itemView.findViewById(R.id.tv_detail)
        val dayTv: TextView = itemView.findViewById(R.id.tv_day)
        val dayOfWeeksTv: TextView = itemView.findViewById(R.id.tv_day_of_weeks)
        val weatherIv: ImageView = itemView.findViewById(R.id.iv_weather)
        val timeTv: TextView = itemView.findViewById(R.id.tv_time)
        val editIv: ImageView = itemView.find(R.id.iv_edit)

        init {
            contentTv.onClick {
                callback.clickItem(adapterPosition.takeIf { it > -1 }
                        ?: return@onClick, TimeLineFragment.TYPE_DETAIL)
            }
            detailTv.onClick {
                callback.clickItem(adapterPosition.takeIf { it > -1 }
                        ?: return@onClick, TimeLineFragment.TYPE_DETAIL)
            }
            editIv.onClick {
                callback.clickItem(adapterPosition.takeIf { it > -1 }
                        ?: return@onClick, TimeLineFragment.TYPE_EDIT)
            }
        }
    }
}