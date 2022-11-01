package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import com.thoughtworks.androidtrain.utils.SharedPreferenceUtils


private const val IS_HINT_SHOWN = "isHintShown"

class SharedPreferenceActivity : AppCompatActivity() {
    object List {
        const val TAG: String = "SharedPreferenceActivity"
    }

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
            finish()
        }
    }

    private fun initUI() {
        val textView: TextView = findViewById(R.id.tip)
        val textShow: String = resources.getString(R.string.tips_shown)
        val btnHint: Button = findViewById(R.id.btn_hint)
        val textNotShow: String = resources.getString(R.string.tips_not_shown)
        val isHintShown: Boolean = sharedPreferenceUtils.readBoolean(
            this, IS_HINT_SHOWN, true
        )
        if (isHintShown) {
            textView.text = textShow
            btnHint.visibility = VISIBLE
        } else {
            textView.text = textNotShow
            btnHint.visibility = GONE
        }
    }

    override fun onPause() {
        super.onPause()
        Log.i(List.TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(List.TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(List.TAG, "onDestroy")
    }
}