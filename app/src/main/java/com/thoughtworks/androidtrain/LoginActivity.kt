package com.thoughtworks.androidtrain

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editor = getSharedPreferences("UserInfo", 0).edit()
        setContentView(R.layout.login_layout)
        addBtnEvent()
        // Hide the status bar.
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
        supportActionBar?.hide()
    }

    private fun addBtnEvent() {
        val btnLogin: Button = findViewById(R.id.login_button)
        btnLogin.setOnClickListener {
            setUserInfo()
        }
    }

    private fun setUserInfo() {
        editor.putString("UserName", "you")
        editor.putString("Nick", "you")
        editor.putString("Avatar", "avatar.png")
        editor.commit()
    }
}