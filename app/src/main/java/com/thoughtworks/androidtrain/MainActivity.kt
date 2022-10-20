package com.thoughtworks.androidtrain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    object List{
        const val TAG:String="MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(List.TAG,"onCreate")
    }

    fun sendMessage(view: View){
//        val editText = findViewById<EditText>(R.id.reminder)
//        val message = editText.text.toString()
//        val intent = Intent(this, ConstraintAtivity::class.java).apply {
//            this.putExtra(EXTRA_MESSAGE, message)
//            this.putExtra(EXTRA_MESSAGE, message)
//            this.putExtra(EXTRA_MESSAGE, message)
//            this.putExtra(EXTRA_MESSAGE, message)
//        }
        val intent = Intent(this, ConstraintActivity::class.java)
//        intent.putExtra(EXTRA_MESSAGE, message)
//        intent.putExtra(EXTRA_MESSAGE, message)
//        intent.putExtra(EXTRA_MESSAGE, message)
//        intent.putExtra(EXTRA_MESSAGE, message)
        startActivity(intent)
    }
}