package com.thoughtworks.androidtrain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.thoughtworks.androidtrain.R
import com.thoughtworks.androidtrain.model.Tweet

class TweetsAdapter : RecyclerView.Adapter<ViewHolder>() {
    var tweets = arrayListOf<Tweet>()

    fun setTweet(tweetsObj: ArrayList<Tweet>) {
        tweets.clear()
        tweets = tweetsObj
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val from: LayoutInflater = LayoutInflater.from(parent.context)
        return TweetViewHolder(from.inflate(R.layout.tweets_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tweetViewHolder = holder as TweetViewHolder
        tweetViewHolder.tweetView.text = tweets[position].toString()
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    class TweetViewHolder(itemView: View) : ViewHolder(itemView) {
        var tweetView: TextView = itemView.findViewById(R.id.tweet)

    }
}