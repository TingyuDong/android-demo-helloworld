package com.thoughtworks.androidtrain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.thoughtworks.androidtrain.R
import com.thoughtworks.androidtrain.data.model.Tweet

class TweetsAdapter : RecyclerView.Adapter<ViewHolder>() {
    object ViewType {
        const val TWEET_TYPE = 0
        const val LAST_TYPE = 1
        const val EMPTY_TYPE = 2
    }

    private var tweets = arrayListOf<Tweet?>()

    fun setTweet(tweetsObj: ArrayList<Tweet?>) {
        tweets.clear()
        tweets = tweetsObj
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val from: LayoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.TWEET_TYPE -> TweetViewHolder(
                from.inflate(R.layout.tweets_item_layout, parent, false)
            )
            else -> {
                BottomViewHolder(
                    from.inflate(R.layout.bottom_item_layout, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (tweets[position] != null && tweets.lastIndex != position) {
            val tweetViewHolder = holder as TweetViewHolder
//            tweets[position]?.sender?.let {
//                tweetViewHolder.tweetAvatar.setImageURI(Uri.parse(it.avatar))
//            }
            tweetViewHolder.tweetName.text = tweets[position]?.sender?.username
            tweetViewHolder.tweetContent.text = tweets[position]?.content
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (tweets.lastIndex != position && tweets[position] != null) ViewType.TWEET_TYPE
        else if (tweets.lastIndex == position) ViewType.LAST_TYPE
        else ViewType.EMPTY_TYPE
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    class TweetViewHolder(itemView: View) : ViewHolder(itemView) {
        var tweetAvatar: ImageView = itemView.findViewById(R.id.tweet_avatar)
        var tweetName: TextView = itemView.findViewById(R.id.tweet_name)
        var tweetContent: TextView = itemView.findViewById(R.id.tweet_content)
    }

    class BottomViewHolder(itemView: View) : ViewHolder(itemView)
}