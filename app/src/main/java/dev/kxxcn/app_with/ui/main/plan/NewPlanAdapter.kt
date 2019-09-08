package dev.kxxcn.app_with.ui.main.plan

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.model.plan.Plan
import dev.kxxcn.app_with.util.Constants.DELIMITERS_DATES

class NewPlanAdapter : RecyclerView.Adapter<NewPlanAdapter.ViewHolder>() {

    private var planList: List<Plan>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_new_plan, parent, false))

    override fun getItemCount(): Int {
        return planList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dayTv.text = planList?.get(position)?.date?.split(DELIMITERS_DATES)!![2]
        holder.timeTv.text = planList?.get(position)?.time
        holder.placeTv.text = planList?.get(position)?.place
        holder.planTv.text = planList?.get(position)?.plan
    }

    fun setItems(_planList: List<Plan>) {
        planList = _planList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTv: TextView = itemView.findViewById(R.id.tv_day)
        val timeTv: TextView = itemView.findViewById(R.id.tv_time)
        val placeTv: TextView = itemView.findViewById(R.id.tv_place)
        val planTv: TextView = itemView.findViewById(R.id.tv_plan)
    }
}