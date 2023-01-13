import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.rule.DispatchersRule
import com.thoughtworks.androidtrain.tweets.TweetsViewModel
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class TweetsViewModelTest {
    private lateinit var tweetsViewModel: TweetsViewModel

    @Mock
    private lateinit var fetchTweetsUseCase: FetchTweetsUseCase

    @Mock
    private lateinit var addCommentUseCase: AddCommentUseCase

    @Mock
    private lateinit var addTweetsUseCase: AddTweetUseCase

    @get:Rule
    val dispatchersRule = DispatchersRule()

    @Before
    fun setupViewModel() {
        MockitoAnnotations.openMocks(this)
        tweetsViewModel = TweetsViewModel(
            fetchTweetsUseCase = fetchTweetsUseCase,
            addCommentUseCase = addCommentUseCase,
            addTweetUseCase = addTweetsUseCase
        )
    }

    @Test
    fun loadAllTweetsFromRepository() = runTest {
        flow<Tweet> {  }.toList()

    }
}