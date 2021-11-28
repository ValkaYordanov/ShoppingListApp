package com.ebookfrenzy.shoppinglistapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ebookfrenzy.shoppinglistapp.adapters.ProductAdapter
import com.ebookfrenzy.shoppinglistapp.data.Product
import com.ebookfrenzy.shoppinglistapp.data.Repository
import com.ebookfrenzy.shoppinglistapp.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    //you need to have an Adapter for the products
    lateinit var adapter: ProductAdapter
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private lateinit var db: FirebaseFirestore
    private val RESULT_CODE_PREFERENCES = 1
   lateinit var btnUpdate : Button

    private fun positiveClicked() {
        val toast = Toast.makeText(
            this,
            "You delete all products from the list!", Toast.LENGTH_LONG
        )
        toast.show()
        //binding.dataText.text = "" //clearing the data
        adapter.deleteAllProducts()
    }


    //callback function from yes/no dialog - for no choice
    private fun negativeClick() {
        //Here we override the method and can now do something
        val toast = Toast.makeText(
            this,
            "NO was pressed!", Toast.LENGTH_LONG
        )
        toast.show()
    }



    private fun addProduct() {
        val proName = findViewById<EditText>(R.id.ed_productName).getText().toString()
        val shop = findViewById<EditText>(R.id.ed_shop).getText().toString()
        val quantityString = findViewById<EditText>(R.id.ed_quantity).getText().toString()

        if (proName.isNullOrEmpty() || quantityString.isNullOrEmpty() || shop.isNullOrEmpty()) {
            findViewById<EditText>(R.id.ed_productName).setText("")
            findViewById<EditText>(R.id.ed_quantity).setText("")
            findViewById<EditText>(R.id.ed_shop).setText("")
            Toast.makeText(this, "You need to fill all 3 fields!", Toast.LENGTH_SHORT)
                .show()
            hideKeybaord(getWindow().getDecorView().getRootView());
        }
        if (proName.isNotEmpty() && quantityString.isNotEmpty() && shop.isNotEmpty()) {
            var quantityField =
                findViewById<EditText>(R.id.ed_quantity).getText().toString().toInt()


            val product = Product(
                name = findViewById<EditText>(R.id.ed_productName).getText().toString(),
                quantity = quantityField,
                shop = findViewById<EditText>(R.id.ed_shop).getText().toString()
            )
            adapter.createProduct(product)

            findViewById<EditText>(R.id.ed_productName).setText("")
            findViewById<EditText>(R.id.ed_quantity).setText("")
            findViewById<EditText>(R.id.ed_shop).setText("")


            hideKeybaord(getWindow().getDecorView().getRootView());
            Log.d("onCreate", "${product}")
        }
    }
    private fun hideKeybaord(v: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
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

        //btnUpdate = findViewById(R.id.btn_Update)!!



        viewModel.getData().observe(this, Observer {
            Log.d("Products", "Found ${it.size} products")
            updateUI(it)
        })


        val name = PreferenceHandler.getName(this)
        updateUIPref(name)

    }

    fun btnUpdate() {
        var position = adapter.returnPos(adapter.ViewHolder(binding.root))
        var productToUpdate: Product
        productToUpdate = Repository.getProduct(position)
        val dialog = UpdateProductDialog(productToUpdate)
        dialog.show(supportFragmentManager, "UpdateProductDialog")
    }

    fun updateUI(products: MutableList<Product>) {
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

    private fun updateUIPref(name: String) {
        findViewById<TextView>(R.id.myName).setText(name + "'s list")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_CODE_PREFERENCES)
        //the code means we came back from settings
        {
            //I can can these methods like this, because they are static

            val name = PreferenceHandler.getName(this)

            val message = "Welcome, $name, to your shopping list?"
            val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
            toast.show()
            updateUIPref(name)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("icon_pressed", "${item.itemId}")
        when (item.itemId) {
            R.id.item_action_share -> {
                var allProducts = ""

                for (product in Repository.products) {
                    allProducts=allProducts + "${product.name} | ${product.quantity} | ${product.shop} \n"
                }
                val intent=Intent()
                intent.action=Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT,allProducts)
                intent.type="text/plain"
                startActivity(Intent.createChooser(intent,resources.getString(R.string.share)))
                return true
            }
            R.id.item_action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent, RESULT_CODE_PREFERENCES)
                return true
            }
            R.id.item_sortByName -> {
                Repository.products.sortBy { it.name }
                adapter.notifyDataSetChanged()

            }
            R.id.item_sortByQunatity -> {
                Repository.products.sortBy { it.quantity }
                adapter.notifyDataSetChanged()

            }

            R.id.item_deleteAllItems -> {
                if(adapter.products.isNotEmpty()) {
                    val dialog = MyDialogFragment(::positiveClicked, ::negativeClick)
                    //Here we show the dialog
                    //The tag "MyFragement" is not important for us.
                    dialog.show(supportFragmentManager, "myFragment")
                }else
                {
                    Toast.makeText(this, "You don't have products in the list!", Toast.LENGTH_LONG)
                        .show()
                }
                return true
            }

        }

        return false //we did not handle the event

    }
}