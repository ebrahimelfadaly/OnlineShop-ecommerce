package com.example.onlineshop.ui.product

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.data.entity.allproducts.allProduct

import com.example.onlineshop.data.entity.customProduct.Product

class ProductItemAdapter(private val context: Context, var intentTOProductDetails : MutableLiveData<allProduct>, var onclick:OnclickBrand)
    : RecyclerView.Adapter<ProductItemAdapter.ViewHolderItem>() {
    private val itemName:ArrayList<allProduct> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun addList(brandList: MutableList<allProduct>)
    {
        this.itemName.clear()
        this.itemName.addAll(brandList)
        notifyDataSetChanged()
    }


    class ViewHolderItem(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val itemName: TextView = itemView.findViewById(R.id.itemTitle)
        val itemIcon : ImageView = itemView.findViewById(R.id.itemIcon)
        val itemprice:TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.shop_item,parent,false)
        return ViewHolderItem(view)
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {


        holder.itemName.text=itemName[position].title
        holder.itemprice.text= itemName[position].variants?.get(0)?.price.toString()+"EGP"

        Glide.with(context)
            .load( itemName[position].image.src )
            .into(holder.itemIcon)
        holder.itemView.setOnClickListener {
      intentTOProductDetails.value=itemName[ position]

        }
    }

    override fun getItemCount(): Int {
        return itemName.size
    }
interface OnclickBrand{
        fun getItemProduct(smartCollection: Product,position: Int)
    }

}