package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.thoughtworks.androidtrain.fragments.AndroidLanguageFragment
import com.thoughtworks.androidtrain.fragments.JavaLanguageFragment
import com.thoughtworks.androidtrain.utils.UiUtils

class LanguageSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)
        addBtnEvent()
    }

    private fun addBtnEvent() {
        val btnAndroid: Button = findViewById(R.id.btn_android)
        val btnJava: Button = findViewById(R.id.btn_java)
        UiUtils().replaceFragment(
            supportFragmentManager,
            JavaLanguageFragment(),
            R.id.text_box,
            null
        )
        btnAndroid.setOnClickListener {
            UiUtils().replaceFragment(
                supportFragmentManager,
                AndroidLanguageFragment(),
                R.id.text_box,
                null
            )
        }
        btnJava.setOnClickListener {
            UiUtils().replaceFragment(
                supportFragmentManager,
                JavaLanguageFragment(),
                R.id.text_box,
                null
            )
        }

    }


}