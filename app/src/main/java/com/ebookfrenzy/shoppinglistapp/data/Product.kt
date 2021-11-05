package com.ebookfrenzy.shoppinglistapp.data

import android.graphics.Bitmap

data class Product(var name:String = "", var image: Bitmap, var quantity:Int=6, var shop:String="Fotex" ) {}