package com.thoughtworks.androidtrain.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Image
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.model.Tweet
import okhttp3.OkHttpClient

@Composable
fun TweetScreen(
    okHttpClient: OkHttpClient,
    tweetsViewModel: TweetsViewModel = viewModel(),
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                tweetsViewModel.init(okHttpClient)
                tweetsViewModel.fetchData()
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val tweets = tweetsViewModel.tweets.observeAsState().value
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        content = {
            item {
                tweets?.forEach {
                    TweetItem(tweet = it) { comment, tweetId ->
                        tweetsViewModel.saveComment(comment, tweetId)
                    }
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
private fun TweetItem(
    tweet: Tweet, saveComment: (comment: Comment, tweetId: Int) -> Unit
) {
    val showAddCommentItem = remember {
        mutableStateOf(false)
    }
    val yourComments = remember {
        mutableListOf<Comment>()
    }
    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Avatar(tweet.sender?.avatar)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Nick(tweet.sender?.nick.orEmpty())
            TweetContent(tweet.content, tweet.images) {
                showAddCommentItem.value = true
            }
            TweetComments(tweet.comments, yourComments)
            if (showAddCommentItem.value) {
                var addCommentValue = ""
                AddCommentItem(
                    comment = addCommentValue,
                    onSave = {
                        addCommentValue = it
                        showAddCommentItem.value = false
                        Comment(
                            addCommentValue,
                            Sender("you", "you", "avtar.png")
                        ).let { it1 ->
                            yourComments.add(it1)
                            saveComment(it1, tweet.id)
                        }
                    },
                    onCancel = {
                        addCommentValue = ""
                        showAddCommentItem.value = false
                    })
            }
        }
    }
}

@Composable
private fun TweetComments(
    comments: List<Comment>?,
    yourComments: MutableList<Comment>
) {
    comments?.forEach {
        CommentItem(it)
    }
    yourComments.forEach {
        CommentItem(it)
    }
}

@Composable
private fun TweetContent(
    textContent: String?,
    imageContent: List<Image>?,
    showAddCommentItem: () -> Unit
) {
    textContent.orEmpty().takeIf {
        it.isNotEmpty()
    }?.let {
        TextContent(it) {
            showAddCommentItem()
        }
    }
    imageContent?.let {
        ImageContent(it)
    }
}

@Composable
private fun ImageContent(images: List<Image>) {
    LazyRow(content = {
        item {
            images.forEach { it1 ->
                TweetImage(it1)
            }
        }
    })

}

@Composable
private fun TweetImage(it1: Image) {
    val showDialog = remember {
        mutableStateOf(false)
    }
    val painter = rememberAsyncImagePainter(it1.url)
    Image(
        painter = painter, contentDescription = "tweet image",
        modifier = Modifier
            .size(80.dp)
            .padding(4.dp)
            .clickable { showDialog.value = true },
        contentScale = ContentScale.Crop
    )
    if (showDialog.value) {
        BigImage(painter) {
            showDialog.value = false
        }
    }
}

@Composable
private fun AddCommentItem(
    comment: String,
    onSave: (comment: String) -> Unit,
    onCancel: () -> Unit
) {
    val textValue = remember(comment) {
        mutableStateOf(comment)
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = textValue.value, onValueChange = { textValue.value = it },
            modifier = Modifier
                .weight(1f, fill = false)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Button(onClick = { onSave.invoke(textValue.value) }) {
            Text(text = "save")
        }
        Spacer(modifier = Modifier.width(20.dp))
        Button(onClick = onCancel) {
            Text(text = "cancel")
        }
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    Row(verticalAlignment = Alignment.Top) {
        comment.sender?.let { it1 ->
            Text(
                text = it1.nick + ":",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 4.dp, start = 0.dp, end = 4.dp),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = comment.content,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp, start = 0.dp, end = 4.dp),
            color = MaterialTheme.colors.secondaryVariant,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun TextContent(it: String, onClickContent: () -> Unit) {
    Text(
        modifier = Modifier
            .background(Color.LightGray.copy(alpha = 0.3f))
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, start = 0.dp, end = 4.dp)
            .clickable { onClickContent() },
        text = it,
        color = MaterialTheme.colors.secondaryVariant,
        fontSize = 14.sp
    )
}

@Composable
private fun Nick(nick: String) {
    Text(
        text = nick,
        color = MaterialTheme.colors.primary,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
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
        BigImage(painter) {
            showDialog.value = false
        }
    }
}

@Composable
private fun BigImage(painter: AsyncImagePainter, onDismissRequest: () -> Unit) {
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
                    .size(220.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }
    }
}