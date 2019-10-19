package dev.kxxcn.app_with.ui.main.decimal

import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.model.decimal.DecimalDay
import dev.kxxcn.app_with.util.Constants.ICON_IMAGES
import dev.kxxcn.app_with.util.Utils
import dev.kxxcn.app_with.util.Utils.Companion.getDDay
import dev.kxxcn.app_with.util.Utils.Companion.getDay
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onLongClick

class DecimalAdapter(
        private var identifier: String,
        private val callback: DecimalContract.DayCallback
) : RecyclerView.Adapter<DecimalAdapter.ViewHolder>() {

    private val dayList = ArrayList<DecimalDay>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_day, parent, false), identifier, callback)
    }

    override fun getItemCount() = dayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        val ctx = holder.itemView.context ?: return
        holder.content.text = item.content
        holder.date.text = item.date
        holder.icon.setBackgroundResource(ICON_IMAGES[item.icon])
        holder.parent.setCardBackgroundColor(ContextCompat.getColor(ctx, Utils.getDayBackgroundColor()))

        try {
            val date = item.date?.split("-") ?: return
            holder.remain.text = if (item.type == 0) {
                getDay(ctx, date[0].toInt(), date[1].toInt() - 1, date[2].toInt())
            } else {
                getDDay(ctx, date[0].toInt(), date[1].toInt() - 1, date[2].toInt())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getItem(pos: Int): DecimalDay? {
        return dayList[pos]
    }

    fun setItems(_dayList: List<DecimalDay>) {
        dayList.clear()
        dayList.addAll(_dayList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        dayList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dayList.size)
    }

    class ViewHolder(
            itemView: View,
            identifier: String,
            callback: DecimalContract.DayCallback
    ) : RecyclerView.ViewHolder(itemView) {

        val parent: CardView = itemView.findViewById(R.id.cv_parent)
        var icon: ImageView = itemView.findViewById(R.id.iv_icon)
        val content: TextView = itemView.findViewById(R.id.tv_content)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val remain: TextView = itemView.findViewById(R.id.tv_remain)

        init {
            fun clickItem(type: Int) {
                val parent = itemView.parent as? RecyclerView ?: return
                val adapter = parent.adapter as? DecimalAdapter ?: return
                val item = adapter.getItem(adapterPosition)
                if (item?.writer == identifier) {
                    when (type) {
                        0 -> {
                            callback.editDay(adapterPosition)
                        }
                        1 -> {
                            callback.removeDay(adapterPosition)
                        }
                    }
                }
            }
            parent.onClick {
                clickItem(0)
            }
            parent.onLongClick {
                clickItem(1)
            }
        }
    }
}
