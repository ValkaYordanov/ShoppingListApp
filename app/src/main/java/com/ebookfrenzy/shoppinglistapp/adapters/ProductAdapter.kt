package com.ebookfrenzy.shoppinglistapp.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ebookfrenzy.shoppinglistapp.R
import com.ebookfrenzy.shoppinglistapp.data.Product
import com.ebookfrenzy.shoppinglistapp.data.Repository
import java.io.ByteArrayInputStream
import java.io.InputStream


class ProductAdapter(var products: MutableList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false)



        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {


        holder.itemName.text = products[position].name
        holder.itemDetail.text = "Quantity:" + products[position].quantity +" Shop: " +products[position].shop
        holder.itemImage.setImageBitmap(products[position].image) // holder.itemImage.setImageBitmap()
    }

    override fun getItemCount(): Int {
        return Repository.products.size
    }

    fun createProduct(product:Product)
    {
        Repository.addProduct(product)
    }
    fun deleteAllProducts()
    {
        Repository.deleteAllProducts()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var itemImage: ImageView
        var itemName: TextView
        var itemDetail: TextView
        init {

            itemImage = itemView.findViewById(R.id.item_image)
            itemName = itemView.findViewById(R.id.item_name)
            itemDetail = itemView.findViewById(R.id.item_detail)
        }

    }
}
