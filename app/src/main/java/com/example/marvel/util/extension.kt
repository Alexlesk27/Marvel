package com.example.marvel.util

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

fun Fragment.toast(message: String, duratiom: Int = Toast.LENGTH_LONG) {
    Toast.makeText(
        requireContext(),
        message,
        duratiom
    ).show()
}

fun View.show(){
   visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.INVISIBLE
}

fun loadImage(
    imageView: ImageView,
    path: String,
    extension: String
){
    Picasso.get().load("$path.$extension").into(imageView)
}



fun String.limitCharacter(character: Int):String{
   if (this.length > character){
       val firstCharacter = 0
       return "${this.substring(firstCharacter, character)}...."
   }
    return this
}