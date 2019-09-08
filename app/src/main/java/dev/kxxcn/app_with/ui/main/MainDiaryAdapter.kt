package dev.kxxcn.app_with.ui.main

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.model.diary.Diary
import dev.kxxcn.app_with.data.remote.APIPersistence.IMAGES_URL
import dev.kxxcn.app_with.data.remote.APIPersistence.THUMBS_URL
import dev.kxxcn.app_with.util.GlideApp

class MainDiaryAdapter : RecyclerView.Adapter<MainDiaryAdapter.ViewHolder>() {

    private var mDiaryList: List<Diary>? = null

    private val mBackgroundColors = arrayListOf(R.color.color_1, R.color.color_2, R.color.color_3, R.color.color_4,
            R.color.color_5, R.color.color_6, R.color.color_7, R.color.color_8, R.color.color_9, R.color.color_10,
            R.color.color_11, R.color.color_12, R.color.color_13, R.color.color_14, R.color.color_15, R.color.color_16,
            R.color.color_17, R.color.color_18, R.color.color_19, R.color.color_20, R.color.color_21)

    private val options = RequestOptions()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main_diary, parent, false))

    override fun getItemCount() = if (mDiaryList == null) 0 else mDiaryList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val item = mDiaryList?.get(position) ?: return
        if (item.primaryPosition != -1) {
            holder.backgroundIv.setBackgroundColor(ContextCompat.getColor(context, mBackgroundColors[item.primaryPosition]))
        } else if (item.galleryName.isNotEmpty()) {
            GlideApp
                    .with(context)
                    .load(String.format(context.getString(R.string.param_download_image_url),
                            THUMBS_URL, item.galleryName))
                    .error(
                            GlideApp.with(context)
                                    .load(String.format(context.getString(R.string.param_download_image_url),
                                            IMAGES_URL, item.galleryName))
                    )
                    .apply(options)
                    .into(holder.backgroundIv)
        }
    }

    fun setItems(planList: List<Diary>) {
        mDiaryList = planList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val backgroundIv: ImageView = itemView.findViewById(R.id.iv_background)
    }
}