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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.thoughtworks.androidtrain.R
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
                TweetItems(tweets, tweetsViewModel)
            }
            item {
                ButtonItem()
            }
        }
    )
}

@Composable
private fun TweetItems(
    tweets: ArrayList<Tweet>?,
    tweetsViewModel: TweetsViewModel
) {
    tweets?.forEach { tweet ->
        TweetItem(tweet = tweet) { comment, tweetId ->
            tweetsViewModel.saveComment(comment, tweetId)
        }
    }
}

@Composable
fun ButtonItem() {
    Text(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth(),
        text = stringResource(id = R.string.have_been_to_the_bottom),
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
    Row(
        modifier = Modifier.padding(
            horizontal = dimensionResource(id = R.dimen.tweet_item_padding_horizontal),
            vertical = dimensionResource(id = R.dimen.tweet_item_padding_vertical)
        )
    ) {
        Avatar(tweet.sender?.avatar)
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_between_avatar_and_content)))
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
                    onSave = { content ->
                        addCommentValue = content
                        showAddCommentItem.value = false
                        Comment(
                            content = addCommentValue,
                            sender = Sender("you", "you", "avtar.png")
                        ).let { comment ->
                            yourComments.add(comment)
                            saveComment(comment, tweet.id)
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
    comments?.forEach { comment ->
        CommentItem(comment)
    }
    yourComments.forEach { yourComment ->
        CommentItem(yourComment)
    }
}

@Composable
private fun TweetContent(
    textContent: String?,
    imageContent: List<Image>?,
    showAddCommentItem: () -> Unit
) {
    textContent?.let { content ->
        TextContent(content) {
            showAddCommentItem()
        }
    }
    imageContent?.let { images ->
        ImageContent(images)
    }
}

@Composable
private fun ImageContent(images: List<Image>) {
    LazyRow(
        content = {
            item {
                images.forEach { image ->
                    TweetImage(image)
                }
            }
        })

}

@Composable
private fun TweetImage(image: Image) {
    val showDialog = remember {
        mutableStateOf(false)
    }
    val painter = rememberAsyncImagePainter(image.url)
    Image(
        painter = painter,
        contentDescription = "tweet image",
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.tweet_image_size))
            .padding(dimensionResource(id = R.dimen.tweet_image_padding))
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
            value = textValue.value,
            onValueChange = { content -> textValue.value = content },
            modifier = Modifier
                .weight(1f, fill = false)
                .align(Alignment.CenterVertically)
        )
        Spacer(
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.space_in_add_comment_item))
        )
        Button(onClick = { onSave.invoke(textValue.value) }) {
            Text(text = "save")
        }
        Spacer(
            modifier = Modifier.width(dimensionResource(id = R.dimen.space_in_add_comment_item))
        )
        Button(onClick = onCancel) {
            Text(text = "cancel")
        }
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .padding(vertical = dimensionResource(id = R.dimen.comment_padding_vertical))
    ) {
        comment.sender?.let { sender ->
            Text(
                text = sender.nick + ":",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier
            )
        }
        Spacer(
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.space_between_nick_and_content))
        )
        Text(
            text = comment.content,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.comment_content_padding_horizontal)
                ),
            color = MaterialTheme.colors.secondaryVariant,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun TextContent(textContent: String, onClickContent: () -> Unit) {
    Text(
        modifier = Modifier
            .background(Color.LightGray.copy(alpha = 0.3f))
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.text_content_padding))
            .clickable { onClickContent() },
        text = textContent,
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
            .size(dimensionResource(id = R.dimen.avatar_size))
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
                .height(dimensionResource(id = R.dimen.big_image_box_height))
                .padding(dimensionResource(id = R.dimen.big_image_box_margin))
                .background(Color.White)
        ) {
            Image(
                painter = painter,
                contentDescription = "BigAvatar",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.big_image_size))
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }
    }
}