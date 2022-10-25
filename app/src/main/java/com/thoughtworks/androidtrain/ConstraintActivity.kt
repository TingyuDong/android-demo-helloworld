package com.thoughtworks.androidtrain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ConstraintActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_ativity)
    }

    fun login() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}