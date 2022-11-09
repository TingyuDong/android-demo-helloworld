package com.thoughtworks.androidtrain.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.thoughtworks.androidtrain.TweetsViewModel
import com.thoughtworks.androidtrain.data.model.Tweet
import okhttp3.OkHttpClient

@Composable
fun TweetScreen(
    okHttpClient: OkHttpClient,
    tweetsViewModel: TweetsViewModel = viewModel(),
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    DisposableEffect(lifeCycleOwner){
        val observer = LifecycleEventObserver { _,event ->
            if(event== Lifecycle.Event.ON_CREATE){
                tweetsViewModel.init(okHttpClient)
                tweetsViewModel.fetchData()
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.addObserver(observer)
        }
    }
    val tweets = tweetsViewModel.tweets.observeAsState().value
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        content = {
            item {
                tweets?.forEach {
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
        val avatar = tweet.sender?.avatar
        Avatar(avatar)
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

@Composable
private fun Avatar(avatar: String?) {
    val painter = rememberAsyncImagePainter(avatar)
    val showDialog = remember {
        mutableStateOf(false)
    }
    Image(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable { showDialog.value = true },
        painter = painter,
        contentDescription = "avatar",
        contentScale = ContentScale.Crop
    )
    if (showDialog.value) {
        BigAvatar(painter) {
            showDialog.value = false
        }
    }
}

@Composable
private fun BigAvatar(painter: AsyncImagePainter, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(20.dp)
                .background(Color.White)
        ) {
            Image(
                painter = painter, contentDescription = "BigAvatar",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }
    }
}