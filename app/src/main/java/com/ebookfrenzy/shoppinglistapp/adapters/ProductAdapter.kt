package com.ebookfrenzy.shoppinglistapp.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ebookfrenzy.shoppinglistapp.R
import com.ebookfrenzy.shoppinglistapp.UpdateProductDialog
import com.ebookfrenzy.shoppinglistapp.data.Product
import com.ebookfrenzy.shoppinglistapp.data.Repository
import java.io.ByteArrayInputStream
import java.io.InputStream
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.ebookfrenzy.shoppinglistapp.MainActivity
import com.google.android.material.internal.ContextUtils.getActivity


class ProductAdapter(var products: MutableList<Product>,var callBackFunc: (product: Product)-> Unit) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        holder.itemName.text = products[position].name
        holder.itemDetail.text = "Quantity:" + products[position].quantity +"\nShop: " +products[position].shop
    }

    fun returnPos(holder: ProductAdapter.ViewHolder):Int{

        return holder.returnPosition()
    }

    override fun getItemCount(): Int {
        return Repository.products.size
    }
    fun updateProduct(product:Product, newName: String, newQuantity: Int, newShop: String)
    {
        Repository.updateProduct(product, newName, newQuantity, newShop)
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

        //var itemImage: ImageView
        var itemName: TextView
        var itemDetail: TextView
        var itemDelete :Button
        var itemUpdate :Button
        init {

            //itemImage = itemView.findViewById(R.id.item_image)
            itemName = itemView.findViewById(R.id.item_name)
            itemDetail = itemView.findViewById(R.id.item_detail)
            itemDelete = itemView.findViewById<Button>(R.id.btn_delete)
            itemUpdate = itemView.findViewById<Button>(R.id.btnUpdate)

            itemDelete.setOnClickListener {
                val position = adapterPosition

                Repository.deleteProduct(position)
                notifyItemRemoved(position) //this line notify the adapter

            }

            itemUpdate.setOnClickListener {
                val position = adapterPosition
                callBackFunc(products[position])

            }

        }


        fun returnPosition():Int {
           return adapterPosition
        }

        }



    }

