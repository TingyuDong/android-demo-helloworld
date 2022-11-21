package com.thoughtworks.androidtrain.compose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.thoughtworks.androidtrain.TweetsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

//// Inject Library
//fun provideUseCase(useCase: UserCase) {
//    //
//}
//fun getUserCase(): UseCase {
//    //
//}

class ComposeActivity : AppCompatActivity() {
    private val tweetsViewModel: TweetsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TweetScreen(
                tweetsViewModel = tweetsViewModel
            )
        }
    }
}