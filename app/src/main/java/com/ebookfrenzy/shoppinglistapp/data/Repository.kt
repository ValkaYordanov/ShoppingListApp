package com.ebookfrenzy.shoppinglistapp.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ebookfrenzy.shoppinglistapp.R

object Repository {
    var products = mutableListOf<Product>()
    lateinit var myContext:Context


    fun setContext(cont: Context)
    {
        myContext=cont
    }
    //listener to changes that we can then use in the Activity
    private var productListener = MutableLiveData<MutableList<Product>>()


    fun getData(): MutableLiveData<MutableList<Product>> {
        if (products.isEmpty())
            createTestData()
        productListener.value = products //we inform the listener we have new data
        return productListener
    }

    

    fun createTestData()
    {
        var bitmap = BitmapFactory.decodeResource(myContext.resources, R.drawable.index)
            bitmap = Bitmap.createScaledBitmap(bitmap, 20, 20, true)
        //add some products to the products list - for testing purposes
        Log.d("Repository","create testdata")
        products.add(Product(name="tomater", image=bitmap))
        products.add(Product(name="bønner",image=bitmap))
    }

}