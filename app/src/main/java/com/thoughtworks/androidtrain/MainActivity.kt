package com.thoughtworks.androidtrain

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.thoughtworks.androidtrain.compose.ComposeActivity
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Image
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.tweets.TweetsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    object List {
        const val TAG: String = "MainActivity"
    }

    private val tweetsViewModel: TweetsViewModel by viewModel()

    private val startActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.resultCode == Activity.RESULT_OK) {
                val contactUri: Uri? = it.data!!.data
                val projection: Array<String> =
                    arrayOf(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    )
                if (contactUri != null) {
                    contentResolver.query(
                        contactUri, projection, null, null, null
                    ).use { cursor ->
                        if (cursor != null) {
                            showContactToast(cursor)
                            showContactDialog(cursor)
                        }
                    }
                }
                Log.i(List.TAG, it.data!!.data.toString())
            }
        }

    private fun showContactDialog(cursor: Cursor) {
        val builder = AlertDialog.Builder(this)
        val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val contactName = cursor.getString(nameIndex)
        val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val phoneNum = cursor.getString(numberIndex)
        builder.setTitle(contactName)
        builder.setMessage(phoneNum)
        builder.setPositiveButton("OK") { _, _ -> }
        val dialog: AlertDialog = builder.create()
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    private fun showContactToast(cursor: Cursor) {
        if (cursor.moveToFirst()) {
            val phoneNum: String
            val contactName: String
            with(cursor) {
                val nameIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                contactName = getString(nameIndex)
                val numberIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                phoneNum = getString(numberIndex)
            }
            Toast.makeText(this, "$contactName $phoneNum", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addBtnEvent() {
        val btnLogin: Button = findViewById(R.id.pick_login)
        val btnPickContact: Button = findViewById(R.id.btn_pick_contact)
        val btnFragment: Button = findViewById(R.id.btn_fragment)
        val btnRecyclerview: Button = findViewById(R.id.btn_recyclerView)
        val btnSharedPreference: Button = findViewById(R.id.shared_preference)
        val btnRoom: Button = findViewById(R.id.add_tweet)
        val btnCompose: Button = findViewById(R.id.btn_compose)
        btnLogin.setOnClickListener {
            login()
        }
        btnPickContact.setOnClickListener {
            pickContact()
        }
        btnFragment.setOnClickListener {
            fragment()
        }
        btnRecyclerview.setOnClickListener {
            recyclerview()
        }
        btnSharedPreference.setOnClickListener {
            sharedPreference()
        }
        btnRoom.setOnClickListener {
            addTweet()
        }
        btnCompose.setOnClickListener {
            compose()
        }
    }

    private fun compose() {
        val intent = Intent(this, ComposeActivity::class.java)
        startActivity(intent)
    }

    private fun login() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun sharedPreference() {
        val intent = Intent(this, SharedPreferenceActivity::class.java)
        startActivity(intent)
    }

    private fun recyclerview() {
        val intent = Intent(this, TweetsActivity::class.java)
        startActivity(intent)
    }

    private fun pickContact() {
        val intent = Intent(Intent.ACTION_PICK, Uri.parse("content://contacts")).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            type = "vnd.android.cursor.dir/phone_v2"
        }
        startActivity.launch(intent)
    }

    private fun fragment() {
        val intent = Intent(this, LanguageSelectionActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_homework)
        Log.i(List.TAG, "onCreate")
        addBtnEvent()
    }

    override fun onStart() {
        super.onStart()
        Log.i(List.TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(List.TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(List.TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(List.TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(List.TAG, "onDestroy")
    }

    fun sendMessage() {
        val intent = Intent(this, ConstraintActivity::class.java)
        startActivity(intent)
    }

    private fun addTweet() {
        val tweetSender = Sender(
            username = "Aiolia",
            nick = "Aio",
            avatar = "https://thoughtworks-mobile-2018.herokuapp.com/images/user/avatar/001.jpeg"
        )
        val commentSender = Sender(
            username = "Saga",
            nick = "Saga",
            avatar = "https://thoughtworks-mobile-2018.herokuapp.com/images/user/avatar/001.jpeg"
        )
        val image = Image(
            url = "https://thoughtworks-mobile-2018.herokuapp.com/images/user/avatar/001.jpeg"
        )
        val comment = Comment(content = "真不错", sender = commentSender)
        tweetsViewModel.saveTweet(
            Tweet(
                id = 0,
                content = "沙发",
                sender = tweetSender,
                images = listOf(image),
                comments = listOf(comment),
                error = null,
                unknownError = null
            )
        )
    }
}