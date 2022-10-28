package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.adapters.TweetsAdapter
import com.thoughtworks.androidtrain.model.Tweet

class TweetsActivity : AppCompatActivity() {
    private lateinit var tweetsAdapter: TweetsAdapter

    object JsonData {
        const val data = "[" +
                "{\"content\":\"我上幼儿园了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}," +
                "{\"content\":\"我上小学了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}," +
                "{\"content\":\"我上小学了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}," +
                "{\"content\":\"我上小学了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}," +
                "{\"content\":\"我上小学了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}," +
                "{\"content\":\"我上小学了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}," +
                "{\"content\":\"我上小学了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}," +
                "{\"content\":\"我上小学了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}," +
                "{\"content\":\"我上小学了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}," +
                "{\"content\":\"古希腊人一直将荷马史诗视作希腊文化的精华将荷马视作民族的骄傲，但丁更称荷马为" +
                "“诗人之王”。但自17世纪末以来，渐渐开始有学者对于荷马是否确有其人，以及他的籍贯、生活年代、史诗" +
                "是否他一人所作等一系列问题都有不同看法，形成“荷马问题”。\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}," +
                "{\"content\":\"我上初中了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}" +
                "]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)
        initUI()
    }

    private fun initUI() {
        val recyclerView: RecyclerView = findViewById(R.id.tweet_recycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tweetsAdapter = TweetsAdapter()
        recyclerView.adapter = tweetsAdapter
        createTweets()?.let { tweetsAdapter.setTweet(it) }
    }

    private fun createTweets(): ArrayList<Tweet>? {
        val type = object : TypeToken<ArrayList<Tweet>>() {}.type
        return Gson().fromJson(JsonData.data, type)
    }
}