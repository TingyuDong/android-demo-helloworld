package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LanguageSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)
        addBtnEvent()
    }

    private fun addBtnEvent() {
        val btn_android: Button = findViewById(R.id.btn_android)
        val btn_java: Button = findViewById(R.id.btn_java)
        btn_android.setOnClickListener {
        }
        btn_java.setOnClickListener {
        }

    }


}