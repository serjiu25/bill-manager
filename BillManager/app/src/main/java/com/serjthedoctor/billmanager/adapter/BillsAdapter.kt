package com.serjthedoctor.billmanager.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.serjthedoctor.billmanager.R
import com.serjthedoctor.billmanager.domain.Bill
import com.serjthedoctor.billmanager.domain.BillStatus
import com.serjthedoctor.billmanager.lib.limit
import java.time.format.DateTimeFormatter
import java.util.*


class BillsAdapter internal constructor(
    private val onClickItemListener: OnClickItemListener,
    private val onLongClickItemListener: OnLongClickItemListener
): RecyclerView.Adapter<BillsAdapter.BillsViewHolder>() {
    private var items = mutableListOf<Bill>()

    inner class BillsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.bill_name)
        val price: TextView = view.findViewById(R.id.bill_price)
        val merchant: TextView = view.findViewById(R.id.bill_merchant)
        val date: TextView = view.findViewById(R.id.bill_date)
        val status: TextView = view.findViewById(R.id.bill_status)
        val imageView: ImageView = view.findViewById(R.id.bill_image)

        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onClickItemListener.onClickItem(items[adapterPosition])
                }
            }
            view.setOnLongClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onLongClickItemListener.onLongClickItem(items[adapterPosition])
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bills_list_item, parent, false)
        return BillsViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat", "NewApi")
    override fun onBindViewHolder(holder: BillsViewHolder, position: Int) {
        val currentItem = items[position]

        if (currentItem.name == null || currentItem.name!!.isBlank()) {
            if (currentItem.date == null || currentItem.merchant == null) {
                holder.name.text = "-"
            } else {
                val date: String = currentItem.date!!.format(DateTimeFormatter.ofPattern("EEEE"))
                holder.name.text = ("$date at ${currentItem.merchant}".limit(25))
            }
        } else {
            holder.name.text = currentItem.name?.limit(27)
        }

        if (currentItem.price == null) {
            holder.price.text = "N/A"
        } else {
            holder.price.text = currentItem.price.toString()
        }

        if (currentItem.merchant == null) {
            holder.merchant.text = "-"
        } else {
            holder.merchant.text = currentItem.merchant?.limit(32)
        }

        if (currentItem.date == null) {
            holder.date.text = "-"
        } else {
            val pattern = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                if (currentItem.date!!.year == Calendar.getInstance().get(Calendar.YEAR)) {
                    "dd MMMM"
                } else {
                    "dd MMMM yyyy"
                }
            } else {
                "dd MMMM yyyy"
            }
            holder.date.text = currentItem.date!!.format(DateTimeFormatter.ofPattern(pattern))
        }

        when (currentItem.status) {
            BillStatus.QUEUED -> holder.status.setTextColor(Color.CYAN)
            BillStatus.RUNNING -> holder.status.setTextColor(Color.parseColor("#CCCC00"))
            BillStatus.PROCESSED -> holder.status.setTextColor(Color.GREEN)
            BillStatus.ERROR -> holder.status.setTextColor(Color.RED)
            else -> holder.status.setTextColor(Color.BLACK)
        }

        if (currentItem.status == null) {
            holder.status.text = "-"
        } else {
            holder.status.text = currentItem.status!!.name
        }

        val requestOptions = RequestOptions().transform(RoundedCorners(16))
        Glide.with(holder.itemView)
            .load(currentItem.imageUrl)
            .apply(requestOptions)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<Bill>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    interface OnClickItemListener {
        fun onClickItem(b: Bill)
    }

    interface OnLongClickItemListener {
        fun onLongClickItem(b: Bill)
    }
}