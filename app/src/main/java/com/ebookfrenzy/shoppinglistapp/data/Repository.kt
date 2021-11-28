package com.ebookfrenzy.shoppinglistapp.data

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ebookfrenzy.shoppinglistapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

@SuppressLint("StaticFieldLeak")
object Repository {
    var products = mutableListOf<Product>()
    lateinit var myContext: Context
    val db = Firebase.firestore

    fun setContext(cont: Context) {
        myContext = cont

    }

    //listener to changes that we can then use in the Activity
    private var productListener = MutableLiveData<MutableList<Product>>()

    fun getProduct(index: Int): Product {
        val product = products[index]
        var pro = Product("","", 0, "")

        val docRef = db.collection("product").document(product.id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    pro.id = "${document.data?.get("id")}"
                    pro.name = "${document.data?.get("name")}"
                    pro.quantity = document.data?.get("quantity").toString().toInt()
                    pro.shop = "${document.data?.get("shop")}"


                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        return pro
    }

    fun getData(): MutableLiveData<MutableList<Product>> {
        if (products.isEmpty())
        //createTestData()
            readDataFromFireBase()
        productListener.value = products //we inform the listener we have new data
        return productListener
    }

    fun deleteProduct(index: Int) { // in the Repository
        deleteProductFromFirebase(index)
        products.removeAt(index)
        productListener.value = products

    }

    fun deleteProductFromFirebase(index: Int) {
        val product = products[index]
        db.collection("product").document(product.id).delete().addOnSuccessListener {
            Log.d("Snapshot", "DocumentSnapshot with id: ${product.id} successfully deleted!")
            //products.removeAt(index) //removes it from the list
        }
            .addOnFailureListener { e -> Log.w("Error", "Error deleting document", e) }
    }

    fun deleteAllProducts() { // in the Repository
        deleteAllFirebase()
        products.clear()
        productListener.value = products
    }

    private fun deleteAllFirebase() {
        val batch = db.batch()
        for (product in products) {
            val ref = db.collection("product").document(product.id)
            batch.delete(ref)
        }

        // Commit the batch
        batch.commit().addOnCompleteListener {}
    }

    fun updateProduct(product: Product, newName: String, newQuantity: Int, newShop: String) {
        db.collection("product").document(product.id)
            .update("name", newName, "qiantity", newQuantity, "shop", newShop)
        readDataFromFireBase()
    }

    fun addProduct(product: Product) {
        db.collection("product")
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.d("Error", "DocumentSnapshot written with ID: " + documentReference.id)
            }
            .addOnFailureListener { e -> Log.w("Error", "Error adding document", e) }

//        products.add(product);
//        productListener.value = products
        readDataFromFireBase()
    }

    private fun readDataFromFireBase() {
        products.clear()
        db.collection("product").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Repository", "${document.id} => ${document.data}")
                    val product = document.toObject<Product>()
                    product.id = document.id  //set the ID in the product class
                    products.add(product)
                }
                productListener.value = products //notify our listener we have new data
            }
            .addOnFailureListener { exception ->
                Log.d("Repository", "Error getting documents: ", exception)
            }
    }

    fun createTestData() {

//        var bitmap = BitmapFactory.decodeResource(myContext.resources, R.drawable.index)
//            bitmap = Bitmap.createScaledBitmap(bitmap, 20, 20, true)
        //add some products to the products list - for testing purposes
        Log.d("Repository", "create testdata")
        products.add(Product(name = "tomater", quantity = 1, shop = "fotex"))
        products.add(Product(name = "bønner", quantity = 3, shop = "ikea"))
        //products.add(Product(name="bønner",image=bitmap, quantity = 3,shop = "ikea"))

    }

}