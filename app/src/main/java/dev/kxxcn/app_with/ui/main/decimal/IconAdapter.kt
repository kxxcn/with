package dev.kxxcn.app_with.ui.main.decimal

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.util.Constants.ICON_IMAGES
import org.jetbrains.anko.sdk27.coroutines.onClick

class IconAdapter(
        private val callback: DecimalContract.SelectIconCallback
) : RecyclerView.Adapter<IconAdapter.ViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_icon, parent, false),
                callback
        )
    }

    override fun getItemCount() = ICON_IMAGES.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.icon.setBackgroundResource(getItem(position))
        holder.select.visibility = if (selectedPosition == position) View.VISIBLE else View.GONE
    }

    private fun getItem(pos: Int) = ICON_IMAGES[pos]

    fun selectIcon(pos: Int) {
        selectedPosition = pos
        notifyDataSetChanged()
    }

    fun getPosition() = selectedPosition

    class ViewHolder(
            itemView: View,
            callback: DecimalContract.SelectIconCallback
    ) : RecyclerView.ViewHolder(itemView) {
        val parent: ConstraintLayout = itemView.findViewById(R.id.cl_parent)
        val icon: ImageView = itemView.findViewById(R.id.iv_icon)
        val select: ImageView = itemView.findViewById(R.id.iv_select)

        init {
            parent.onClick { callback.selectIcon(adapterPosition) }
        }
    }
}
