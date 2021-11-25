package com.ebookfrenzy.shoppinglistapp

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.number.NumberFormatter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebookfrenzy.shoppinglistapp.adapters.ProductAdapter
import com.ebookfrenzy.shoppinglistapp.data.Product
import com.ebookfrenzy.shoppinglistapp.data.Repository
import com.ebookfrenzy.shoppinglistapp.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.lang.RuntimeException
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    //you need to have an Adapter for the products
    lateinit var adapter: ProductAdapter
    lateinit var binding : ActivityMainBinding
    lateinit var viewModel : MainViewModel
    private lateinit var db: FirebaseFirestore

    private fun addProduct() {
        val proName=findViewById<EditText>(R.id.ed_productName).getText().toString()
        val shop=findViewById<EditText>(R.id.ed_shop).getText().toString()
        val qantitystring = findViewById<EditText>(R.id.ed_quantity).getText().toString()

        if(proName.isNullOrEmpty()|| qantitystring.isNullOrEmpty()|| shop.isNullOrEmpty())
        {
            findViewById<EditText>(R.id.ed_productName).setText("")
            findViewById<EditText>(R.id.ed_quantity).setText("")
            findViewById<EditText>(R.id.ed_shop).setText("")
        }

        val qantityField = findViewById<EditText>(R.id.ed_quantity).getText().toString().toInt()


//        var bitmap = BitmapFactory.decodeResource(Repository.myContext.resources, R.drawable.index)
//        bitmap = Bitmap.createScaledBitmap(bitmap, 20, 20, true)

//        var product = Product( name = proName,shop = shop,quantity = qantity)
//        adapter.createProduct(product)


        val product = Product(
            name = findViewById<EditText>(R.id.ed_productName).getText().toString(),
            quantity = qantityField,
            shop = findViewById<EditText>(R.id.ed_shop).getText().toString()
        )
        db.collection("product")
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.d("Error", "DocumentSnapshot written with ID: " + documentReference.id)
            }
            .addOnFailureListener { e -> Log.w("Error", "Error adding document", e) }

        findViewById<EditText>(R.id.ed_productName).setText("")
        findViewById<EditText>(R.id.ed_quantity).setText("")
        findViewById<EditText>(R.id.ed_shop).setText("")

        Log.d("onCreate", "${product}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Repository.setContext(this)

        FirebaseApp.initializeApp(applicationContext)
        db = Firebase.firestore

        //Crashlytics.getInstance().crash();
        //throw RuntimeException("test crash")
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val btnCreate = findViewById<Button>(R.id.btnCreate)
        btnCreate.setOnClickListener { addProduct() }



        viewModel.getData().observe(this, Observer {
            Log.d("Products","Found ${it.size} products")
            updateUI(it)
        })




    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
    fun updateUI(products : MutableList<Product>) {
        val layoutManager = LinearLayoutManager(this)

        /*you need to have a defined a recylerView in your
        xml file - in this case the id of the recyclerview should
        be "recyclerView" - as the code line below uses that */

         binding.recyclerView.layoutManager = layoutManager

        adapter = ProductAdapter(products)

        /*connecting the recyclerview to the adapter  */
          binding.recyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("icon_pressed", "${item.itemId}")
        when (item.itemId) {
            R.id.item_addProductItem -> {
                Toast.makeText(this, "About item clicked!", Toast.LENGTH_LONG)
                    .show()
                return true
            }
            R.id.item_sortByName ->
            {
                Repository.products.sortBy { it.name }
                    adapter.notifyDataSetChanged()

            }
            R.id.item_sortByQunatity ->
            {
                    Repository.products.sortBy { it.quantity }
                            adapter.notifyDataSetChanged()

            }
            R.id.item_refresh -> {
                Toast.makeText(this, "Delete item clicked!", Toast.LENGTH_LONG)
                    .show()
                return true
            }
            R.id.item_deleteAllItems -> {
              adapter.deleteAllProducts()
                return true
            }
            R.id.item_refresh -> {
                Toast.makeText(this, "Refresh item clicked!", Toast.LENGTH_LONG)
                    .show()
                return true
            }
        }

        return false //we did not handle the event

    }
}