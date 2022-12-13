package com.thoughtworks.androidtrain.tweets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.thoughtworks.androidtrain.R
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Image
import com.thoughtworks.androidtrain.data.model.Tweet

@OptIn(ExperimentalMaterialApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun TweetScreen(
    viewModel: TweetsViewModel,
    state: TweetsState = rememberTweetsState(viewModel = viewModel),
) {
    val tweets by viewModel.tweets.collectAsStateWithLifecycle()
    val message by viewModel.message.observeAsState(initial = "")
    val isRefreshing by viewModel.isRefreshing.observeAsState(initial = false)
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.refresh() })

    state.showMessage(message)

    Box(Modifier.pullRefresh(pullRefreshState)){
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            content = {
                item {
                    TweetItems(
                        tweets = tweets,
                        saveComment = state.saveComment(),
                    )
                }
                item {
                    BottomItem()
                }
            }
        )
        PullRefreshIndicator(isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
private fun TweetItems(
    tweets: List<Tweet>,
    saveComment: (Int, String, MutableState<Boolean>) -> Unit,
) {
    tweets.forEach { tweet ->
        TweetItem(tweet = tweet, saveComment = saveComment)
    }
}

@Composable
private fun BottomItem() {
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
    tweet: Tweet,
    saveComment: (Int, String, MutableState<Boolean>) -> Unit,
) {
    Row(
        modifier = Modifier.padding(
            horizontal = dimensionResource(id = R.dimen.tweet_item_padding_horizontal),
            vertical = dimensionResource(id = R.dimen.tweet_item_padding_vertical)
        )
    ) {
        Avatar(tweet.sender?.avatar)
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_between_avatar_and_content)))
        TweetSenderNickAndContentsAndComments(
            tweet = tweet,
            saveComment = saveComment,
        )
    }
}

@Composable
private fun TweetSenderNickAndContentsAndComments(
    tweet: Tweet,
    saveComment: (Int, String, MutableState<Boolean>) -> Unit,
) {
    val showAddCommentItem = remember { mutableStateOf(false) }
    Column {
        Nick(tweet.sender?.nick.orEmpty())
        TweetContents(
            textContent = tweet.content,
            imageContent = tweet.images
        ) { showAddCommentItem.value = true }
        TweetComments(tweet.comments)
        if (showAddCommentItem.value) {
            AddCommentItem(
                onSave = { commentContent ->
                    saveComment(tweet.id, commentContent, showAddCommentItem)
                },
                onCancel = {
                    showAddCommentItem.value = false
                }
            )
        }
    }
}

@Composable
private fun TweetComments(
    comments: List<Comment>?
) {
    comments?.forEach { comment ->
        CommentItem(comment)
    }
}

@Composable
private fun TweetContents(
    textContent: String?,
    imageContent: List<Image>?,
    showAddCommentItem: () -> Unit
) {
    textContent?.also { content ->
        TextContent(content) {
            showAddCommentItem()
        }
    }
    imageContent?.also { images ->
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
    onSave: (String) -> Unit,
    onCancel: () -> Unit
) {
    val textValue = remember {
        mutableStateOf("")
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
        Button(onClick = { onSave(textValue.value) }) {
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
        comment.sender.apply {
            Text(
                text = nick + stringResource(id = R.string.dwukropek),
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