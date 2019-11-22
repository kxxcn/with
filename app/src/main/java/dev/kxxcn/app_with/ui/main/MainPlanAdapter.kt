package dev.kxxcn.app_with.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.model.plan.Plan
import dev.kxxcn.app_with.util.ImageProcessingHelper
import dev.kxxcn.app_with.util.Utils.Companion.getDDay
import java.lang.ref.WeakReference

class MainPlanAdapter(context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: Context? = context

    private val resourcesOfMonth = intArrayOf(R.drawable.ic_january, R.drawable.ic_february, R.drawable.ic_march,
            R.drawable.ic_april, R.drawable.ic_may, R.drawable.ic_june, R.drawable.ic_july, R.drawable.ic_august,
            R.drawable.ic_september, R.drawable.ic_october, R.drawable.ic_november, R.drawable.ic_december)

    private var mPlanList = ArrayList<PlanItem>(0)

    private val option = RequestOptions()

    private var refRecyclerView: WeakReference<RecyclerView>? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        refRecyclerView = WeakReference(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        refRecyclerView = null
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val ctx = parent.context
        val inflater = LayoutInflater.from(ctx)
        return when (viewType) {
            TYPE_ITEM -> {
                val view = inflater.inflate(R.layout.item_main_plan, parent, false)
                PlanHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_view_empty, parent, false)
                EmptyHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        when (holder) {
            is PlanHolder -> {
                try {
                    val item = getItem(position).item ?: return
                    val ctx = holder.itemView.context ?: return
                    val date = item.date?.split(REGEX_DATE)
                    val month = date?.get(1)
                    if (month != null) {
                        setBackgroundMonthTextView(Integer.parseInt(month), holder.monthIv)
                    }
                    holder.planTv.text = item.plan
                    holder.dateTimeTv.text = String.format(mContext!!.getString(R.string.text_plan_date_time), item.date, item.time)
                    holder.placeTv.text = item.place
                    if (date != null) {
                        holder.decimalDayTv.text = getDDay(ctx, date[0].toInt(), date[1].toInt() - 1, date[2].toInt())
                    }
                } catch (e: ArrayIndexOutOfBoundsException) {
                    e.printStackTrace()
                }
            }
            is EmptyHolder -> {
                holder.emptyText.text = context.getString(R.string.text_not_found_plan)
            }
        }
    }

    override fun getItemCount() = mPlanList.size

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    private fun getItem(position: Int): PlanItem {
        return mPlanList[position]
    }

    fun setItems(planList: List<Plan>) {
        mPlanList.clear()
        if (planList.isEmpty()) {
            mPlanList.add(PlanItem(null, TYPE_EMPTY))
        } else {
            for (item in planList) {
                mPlanList.add(PlanItem(item, TYPE_ITEM))
            }
        }
        notifyDataSetChanged()
        val recyclerView = refRecyclerView?.get()
        recyclerView?.scheduleLayoutAnimation()
    }

    private fun setBackgroundMonthTextView(month: Int, monthIv: ImageView) {
        ImageProcessingHelper.setGlide(mContext, resourcesOfMonth[month - 1], monthIv, option)
    }

    class PlanItem(val item: Plan?, val type: Int)

    class PlanHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthIv: ImageView = itemView.findViewById(R.id.iv_month)
        val planTv: TextView = itemView.findViewById(R.id.tv_plan)
        val dateTimeTv: TextView = itemView.findViewById(R.id.tv_date_time)
        val placeTv: TextView = itemView.findViewById(R.id.tv_place)
        val decimalDayTv: TextView = itemView.findViewById(R.id.tv_decimal_day)
    }

    class EmptyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emptyText: TextView = itemView.findViewById(R.id.tv_no_data)
    }

    companion object {

        private const val REGEX_DATE = "-"

        const val TYPE_ITEM = 0

        const val TYPE_EMPTY = 1
    }
}
