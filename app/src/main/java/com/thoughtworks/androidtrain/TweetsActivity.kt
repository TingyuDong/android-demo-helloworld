package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.model.Tweet

class TweetsActivity : AppCompatActivity() {
    object JsonData {
        const val data = "[{\"content\":\"我毕业了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweets)
        getJson()
    }

    private fun getJson() {
        val type = object : TypeToken<List<Tweet>>() {}.type
        val tweets = Gson().fromJson<List<Tweet>>(JsonData.data, type)
        Toast.makeText(this, tweets[0].toString(), Toast.LENGTH_SHORT).show()
    }
}