package com.guru.stockcom.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.guru.stockcom.databinding.ListShopkeeperReportBinding
import com.guru.stockcom.model.SaleReport
import com.guru.stockcom.utils.DateUtils

class ShopKeeperReportAdapter(private val onClick: (SaleReport) -> Unit) :
    ListAdapter<SaleReport, ShopKeeperReportAdapter.MyViewHolder>(ProductItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyViewHolder {
        val binding =
            ListShopkeeperReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    inner class MyViewHolder(binding: ListShopkeeperReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var mbinding: ListShopkeeperReportBinding = binding

        init {
            itemView.setOnClickListener {
                onClick(getItem(adapterPosition))
            }
        }

        fun bindTo(saleReport: SaleReport) {
            val stock = saleReport.stock.split("-")
            mbinding.tvProductName.text = saleReport.name
            mbinding.tvPrice.text = saleReport.price
            mbinding.tvStock.text = stock[1]+ " KG"
            mbinding.tvDate.text = DateUtils.changeDateFormat(saleReport.out_date)
        }
    }

    class ProductItemDiffCallback : DiffUtil.ItemCallback<SaleReport>() {
        override fun areItemsTheSame(oldItem: SaleReport, newItem: SaleReport): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: SaleReport, newItem: SaleReport): Boolean =
            oldItem == newItem

    }
}