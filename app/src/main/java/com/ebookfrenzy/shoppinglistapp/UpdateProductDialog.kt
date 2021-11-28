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

class UpdateProductDialog(val product: Product) : DialogFragment() {
    lateinit var adapter: ProductAdapter

//    private fun doInputCheck(start: Int, end: Int): Boolean {
//        if (start < 1 || end < 1) {
//            Toast.makeText(
//                context,
//                context?.getText(R.string.no_negative_numbers),
//                Toast.LENGTH_LONG
//            ).show()
//            return false
//        }
//        if (end < start) {
//            Toast.makeText(
//                context,
//                context?.getText(R.string.end_number_bigger_than_start),
//                Toast.LENGTH_LONG
//            )
//                .show()
//            return false
//        }
//
//        return true
//
//    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val view = inflater.inflate(R.layout.update_product_layout, container)


        view.findViewById<Button>(R.id.updateButton).setOnClickListener {
            val newProName = view.findViewById<EditText>(R.id.ed_updateName).getText().toString()
            val newShop = view.findViewById<EditText>(R.id.ed_updateQuantity).getText().toString()
            val newQuantityString = view.findViewById<EditText>(R.id.ed_updateShop).getText().toString()

            if (newProName.isNullOrEmpty() || newShop.isNullOrEmpty() || newQuantityString.isNullOrEmpty()) {
                view.findViewById<EditText>(R.id.ed_updateName).setText("")
                view.findViewById<EditText>(R.id.ed_updateQuantity).setText("")
                view.findViewById<EditText>(R.id.ed_updateShop).setText("")


            }
            if (newProName.isNotEmpty() && newShop.isNotEmpty() && newQuantityString.isNotEmpty()) {
                var quantityField =
                    view.findViewById<EditText>(R.id.ed_updateQuantity).getText().toString().toInt()



                   var name = view.findViewById<EditText>(R.id.ed_productName).getText().toString()
                   var quantity = quantityField
                   var shop = view.findViewById<EditText>(R.id.ed_shop).getText().toString()

                adapter.updateProduct(product,name, quantity, shop)

                view.findViewById<EditText>(R.id.ed_updateName).setText("")
                view.findViewById<EditText>(R.id.ed_updateQuantity).setText("")
                view.findViewById<EditText>(R.id.ed_updateShop).setText("")


                Log.d("onUpdate", "${product}")
            }
        }



        view.findViewById<Button>(R.id.cancleButton).setOnClickListener {
            dismiss()
        }

        return view
    }
}