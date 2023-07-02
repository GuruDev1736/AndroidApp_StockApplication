package com.guru.stockcom.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.guru.stockcom.databinding.ListProductAdapterBinding
import com.guru.stockcom.model.ProductList

class ProductListAdapter(private val onClick: (ProductList) -> Unit) :
    ListAdapter<ProductList, ProductListAdapter.ProductViewHolder>(ProductItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ProductViewHolder {
        val binding =
            ListProductAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    inner class ProductViewHolder(binding: ListProductAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var mbinding: ListProductAdapterBinding = binding

        init {
            itemView.setOnClickListener {
                onClick(getItem(adapterPosition))
            }
        }

        fun bindTo(product: ProductList) {
            mbinding.tvProductName.text = product.name
            mbinding.tvPrice.text = product.price
            mbinding.tvStock.text = product.avaliable_stock
            mbinding.tvProfitMargin.text = product.profit_margin
            mbinding.tvExpDate.text = product.exp_date
        }
    }

    class ProductItemDiffCallback : DiffUtil.ItemCallback<ProductList>() {
        override fun areItemsTheSame(oldItem: ProductList, newItem: ProductList): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ProductList, newItem: ProductList): Boolean =
            oldItem == newItem

    }
}