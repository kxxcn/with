package dev.kxxcn.app_with.ui.main.diary

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.joooonho.SelectableRoundedImageView
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.model.diary.Diary
import dev.kxxcn.app_with.data.remote.APIPersistence.IMAGES_URL
import dev.kxxcn.app_with.util.ImageProcessingHelper

class DiaryAdapter(
        private val deviceWidth: Int
) : RecyclerView.Adapter<DiaryAdapter.Companion.ViewHolder>() {

    private var diaryList: List<Diary>? = null

    private val options = RequestOptions.fitCenterTransform().override(Target.SIZE_ORIGINAL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_diary, parent, false))

    override fun getItemCount() = diaryList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = diaryList?.get(position) ?: return
        val ctx = holder.itemView.context ?: return
        val params = holder.diaryIv.layoutParams
        params.width = deviceWidth / 2 - 50
        holder.diaryIv.layoutParams = params
        holder.diaryIv.layout(0, 0, 0, 0)
        ImageProcessingHelper.setThumbnail(
                ctx,
                ctx.getString(R.string.param_download_image_url, IMAGES_URL, item.galleryName),
                holder.diaryIv,
                options)
    }

    fun setItems(_diaryList: List<Diary>?) {
        diaryList = _diaryList
        notifyDataSetChanged()
    }

    companion object {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val diaryIv: SelectableRoundedImageView = itemView.findViewById(R.id.iv_diary)
        }
    }
}