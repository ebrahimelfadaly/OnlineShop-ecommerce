package com.example.onlineshop.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.data.entity.customProduct.Product
import com.example.onlineshop.databinding.CategoryItemBinding

class ItemCategoryAdapter(var categoryItems: List<Product>, var onClick: ItemRecyclerClick) :
    RecyclerView.Adapter<ItemCategoryAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(categoryItems.get(position).images?.get(0)?.src)
            .into(holder.binding.itemIcon)
        holder.binding.itemTitle.text = "" + categoryItems.get(position).title

        holder.itemView.setOnClickListener {
            onClick.itemOnClick(categoryItems.get(position).id.toLong())
        }
    }

    override fun getItemCount(): Int {
        return categoryItems.size
    }

    class ItemViewHolder(val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root)
}
