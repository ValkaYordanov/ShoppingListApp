package com.ebookfrenzy.shoppinglistapp.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
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


    private var productListener = MutableLiveData<MutableList<Product>>()


    fun getData(): MutableLiveData<MutableList<Product>> {
        if (products.isEmpty())

            readDataFromFireBase()
        productListener.value = products
        return productListener
    }

    fun deleteProduct(index: Int) {
        deleteProductFromFirebase(index)
        products.removeAt(index)
        productListener.value = products
    }

    fun deleteProductFromFirebase(index: Int) {
        val product = products[index]
        db.collection("product").document(product.id).delete().addOnSuccessListener {
            Log.d("Snapshot", "DocumentSnapshot with id: ${product.id} successfully deleted!")
        }
            .addOnFailureListener { e -> Log.w("Error", "Error deleting document", e) }
    }

    fun deleteAllProducts() {
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
        batch.commit().addOnCompleteListener {}
    }

    fun updateProduct(product: Product, newName: String, newQuantity: Int, newShop: String) {
        db.collection("product").document(product.id)
            .update("name", newName, "quantity", newQuantity, "shop", newShop)
        readDataFromFireBase()
    }

    fun addProduct(product: Product) {
        db.collection("product")
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.d("Error", "DocumentSnapshot written with ID: " + documentReference.id)
            }
            .addOnFailureListener { e -> Log.w("Error", "Error adding document", e) }

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



}