package com.ebookfrenzy.shoppinglistapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import com.ebookfrenzy.shoppinglistapp.adapters.ProductAdapter
import com.ebookfrenzy.shoppinglistapp.data.Product
import com.ebookfrenzy.shoppinglistapp.data.Repository
import com.ebookfrenzy.shoppinglistapp.data.Repository.products

class UpdateProductDialog(val product: Product,var callUpdateFunc: (product: Product, newName: String, newQuantity: Int, newShop: String)-> Unit, var hideKeybaord:(v:View)->Unit) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {


        val view = inflater.inflate(R.layout.update_product_layout, container)
        view.findViewById<EditText>(R.id.ed_updateName).setText("${product.name}")
        view.findViewById<EditText>(R.id.ed_updateQuantity).setText("${product.quantity}")
        view.findViewById<EditText>(R.id.ed_updateShop).setText("${product.name}")

        view.findViewById<Button>(R.id.updateButton).setOnClickListener {
            val newProName = view.findViewById<EditText>(R.id.ed_updateName).text.toString()
            val newShop = view.findViewById<EditText>(R.id.ed_updateQuantity).text.toString()
            val newQuantityString = view.findViewById<EditText>(R.id.ed_updateShop).text.toString()

            if (newProName.isNullOrEmpty() || newShop.isNullOrEmpty() || newQuantityString.isNullOrEmpty()) {
                view.findViewById<EditText>(R.id.ed_updateName).setText("")
                view.findViewById<EditText>(R.id.ed_updateQuantity).setText("")
                view.findViewById<EditText>(R.id.ed_updateShop).setText("")


            }
            if (newProName.isNotEmpty() && newShop.isNotEmpty() && newQuantityString.isNotEmpty()) {
                var quantityField =
                    view.findViewById<EditText>(R.id.ed_updateQuantity).getText().toString().toInt()



                   var name = view.findViewById<EditText>(R.id.ed_updateName).getText().toString()
                   var quantity = quantityField
                   var shop = view.findViewById<EditText>(R.id.ed_updateShop).getText().toString()

                callUpdateFunc(product,name, quantity, shop)


                hideKeybaord(view)
                dismiss()

                Log.d("onUpdate", "${product}")
            }
        }



        view.findViewById<Button>(R.id.cancleButton).setOnClickListener {
            dismiss()
        }

        return view
    }
}