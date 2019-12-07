package dev.kxxcn.app_with.ui.main.write

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.util.Constants.DAY_COLORS
import dev.kxxcn.app_with.util.tint
import org.jetbrains.anko.sdk27.coroutines.onClick

class ColorPickerAdapter(
        private var selected: Int
) : RecyclerView.Adapter<ColorPickerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val ctx = parent.context
        val inflater = LayoutInflater.from(ctx)
        val v = inflater.inflate(R.layout.item_color, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = DAY_COLORS.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ctx = holder.itemView.context
        val color = ContextCompat.getColor(ctx, DAY_COLORS[position])
        holder.colorIv.tint(color)
        holder.checkIv.visibility = if (position == selected) View.VISIBLE else View.GONE
    }

    fun selectColor(pos: Int) {
        selected = pos
        notifyDataSetChanged()
    }

    fun getColor() = selected

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorIv: ImageView = itemView.findViewById(R.id.iv_color)
        val checkIv: ImageView = itemView.findViewById(R.id.iv_check)

        init {
            itemView.onClick {
                val rv = itemView.parent as? RecyclerView ?: return@onClick
                val adapter = rv.adapter as? ColorPickerAdapter ?: return@onClick
                val pos = adapterPosition.takeIf { it >= 0 } ?: return@onClick
                adapter.selectColor(pos)
            }
        }
    }
}