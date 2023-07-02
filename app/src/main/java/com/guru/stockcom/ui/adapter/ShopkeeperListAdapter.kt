package com.guru.stockcom.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.guru.stockcom.databinding.ListShopkeeperAdapterBinding
import com.guru.stockcom.model.ShopKeeperList
import com.guru.stockcom.ui.activity.HandlerShopkeeper

class ShopkeeperListAdapter(private val onClick: HandlerShopkeeper) :
    ListAdapter<ShopKeeperList, ShopkeeperListAdapter.ShopkeeperViewHolder>(
        ShopkeeperItemDiffCallback()
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ShopkeeperViewHolder {
        val binding =
            ListShopkeeperAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopkeeperViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ShopkeeperViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    inner class ShopkeeperViewHolder(binding: ListShopkeeperAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var mbinding: ListShopkeeperAdapterBinding = binding

        init {
            binding.tvEdit.setOnClickListener {
                onClick.onClickEdit(getItem(adapterPosition))
            }
            binding.tvReport.setOnClickListener {
                onClick.onClickReport(getItem(adapterPosition))
            }
            mbinding.tvCall.setOnClickListener {
                onClick.onClickCall(getItem(adapterPosition))
            }
        }

        fun bindTo(shopkeeper: ShopKeeperList) {
            mbinding.tvName.text = shopkeeper.name
            mbinding.tvUsername.text = shopkeeper.username
            mbinding.tvPassword.text = shopkeeper.password
            mbinding.tvMobileNo.text = shopkeeper.mobile_no

        }
    }

    class ShopkeeperItemDiffCallback : DiffUtil.ItemCallback<ShopKeeperList>() {
        override fun areItemsTheSame(oldItem: ShopKeeperList, newItem: ShopKeeperList): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ShopKeeperList, newItem: ShopKeeperList): Boolean =
            oldItem == newItem

    }
}