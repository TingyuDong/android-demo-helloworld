package com.thoughtworks.androidtrain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.thoughtworks.androidtrain.utils.SharedPreferenceUtils


private const val IS_HINT_SHOWN = "isHintShown"

class SharedPreferenceActivity : AppCompatActivity() {
    private val sharedPreferenceUtils = SharedPreferenceUtils()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_preference)
        initUI()
        addBtnEvent()
    }

    private fun addBtnEvent() {
        val btnHint: Button = findViewById(R.id.btn_hint)
        btnHint.setOnClickListener {
            sharedPreferenceUtils.writeBoolean(this, IS_HINT_SHOWN, false)
            onPause()
        }
    }

    private fun initUI() {
        val textView: TextView = findViewById(R.id.tip)
        val textShow: String = resources.getString(R.string.tips_shown)
        val textNotShow: String = resources.getString(R.string.tips_not_shown)
        val isHintShown: Boolean = sharedPreferenceUtils.readBoolean(this, IS_HINT_SHOWN, true)
        if (isHintShown) textView.text = textShow
        else textView.text = textNotShow
    }

    override fun onPause() {
        super.onPause()
        startActivity(Intent(this,MainActivity::class.java))
    }
}