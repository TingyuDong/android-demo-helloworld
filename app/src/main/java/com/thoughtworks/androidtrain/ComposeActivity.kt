package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.utils.JSONResourceUtils

class ComposeActivity : AppCompatActivity() {
    var tweets = ArrayList<Tweet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tweets.addAll(
            Gson().fromJson(
                JSONResourceUtils().jsonResourceReader(resources, R.raw.tweets),
                object : TypeToken<ArrayList<Tweet>>() {}.type
            )
        )
        setContent {
            Content()
        }
    }

    @Composable
    private fun Content() {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            content = {
                item {
                    tweets.forEach {
                        TweetItem(tweet = it)
                    }
                }
                item {
                    ButtonItem()
                }
            }
        )
    }

    @Composable
    fun ButtonItem() {
        Text(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth(),
//            text = resources.getString(R.string.have_been_to_the_bottom),
            text = "到底了",
            color = Color.White,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
    }

    @Composable
    private fun TweetItem(tweet: Tweet) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            val painter = rememberAsyncImagePainter(tweet.sender?.avatar)
            Image(
                modifier = Modifier.size(40.dp).clip(CircleShape),
                painter = painter,
                contentDescription = "avatar",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = tweet.sender?.nick.orEmpty(),
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                tweet.content.orEmpty().takeIf {
                    it.isNotEmpty()
                }?.let {
                    Text(
                        modifier = Modifier
                            .background(Color.LightGray.copy(alpha = 0.3f))
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 4.dp, start = 0.dp, end = 4.dp),
                        text = it,
                        color = MaterialTheme.colors.secondaryVariant,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    fun ContentPreview() {
        tweets.add(getTweet())
        tweets.add(getTweet())
        Content()
    }

    private fun getTweet(): Tweet {
        val sender = Sender(
            username = "Aiolia",
            nick = "Aio",
            avatar = "https://thoughtworks-mobile-2018.herokuapp.com/images/user/avatar/001.jpeg"
        )
        return Tweet(
            id = 7,
            content = "沙发",
            sender = sender,
            images = null,
            comments = null,
            error = null,
            unknownError = null
        )
    }
}