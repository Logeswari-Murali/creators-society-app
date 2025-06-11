package com.example.creatorsociety

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_ACTION
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatActivity : AppCompatActivity() {
    private lateinit var chatDao: ChatDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var communityName: String
    private val messages = mutableListOf<ChatEntity>()

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private var currentUserId: String = "" // Now storing Firebase UID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        communityName = intent.getStringExtra("communityName") ?: "Chat"
        chatDao = AppDatabase.getDatabase(this).chatDao()

        val tvCommunityName = findViewById<TextView>(R.id.tvCommunityName)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSend = findViewById<ImageView>(R.id.btnSend)
        val btnImage = findViewById<ImageView>(R.id.btnImage)

        tvCommunityName.text = communityName

        // Fetch logged-in user ID from Firebase
        currentUserId = getCurrentUserId()

        recyclerView = findViewById(R.id.recyclerViewChat)
        recyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(messages, this, currentUserId, ::deleteMessage)
        recyclerView.adapter = chatAdapter

        loadMessages()

        // Send button click
        btnSend.setOnClickListener {
            val messageText = etMessage.text.toString().trim()
            if (messageText.isNotEmpty() || selectedImageUri != null) {
                sendMessage(messageText, selectedImageUri)
                etMessage.text.clear()
                selectedImageUri = null
            }
        }

        // Image button click
        btnImage.setOnClickListener {
            pickImage()
        }
    }

    private fun loadMessages() {
        lifecycleScope.launch(Dispatchers.IO) {
            val chatHistory = chatDao.getChatsForCommunity(communityName)
            withContext(Dispatchers.Main) {
                messages.clear()
                messages.addAll(chatHistory)
                chatAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(messages.size - 1)
            }
        }
    }

    private fun sendMessage(text: String, imageUri: Uri?) {
        val imageUriString = imageUri?.toString()?.trim() // Convert Uri to String

        val newMessage = ChatEntity(
            communityName = communityName,
            senderId = currentUserId, // Use Firebase UID as senderId
            message = text.ifEmpty { null },
            imageUri = imageUriString, // Store image URI as String
            timestamp = System.currentTimeMillis()
        )

        lifecycleScope.launch(Dispatchers.IO) {
            chatDao.insertMessage(newMessage)
            withContext(Dispatchers.Main) {
                messages.add(newMessage)
                chatAdapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)
            }
        }
    }

    private fun deleteMessage(message: ChatEntity) {
        val messageIndex = messages.indexOf(message)
        if (messageIndex == -1) return // Prevents crashes if the item is already deleted

        messages.removeAt(messageIndex)
        chatAdapter.notifyItemRemoved(messageIndex)

        // Prevent duplicate Snackbar by checking if it's already displayed
        val snackbar = Snackbar.make(recyclerView, "Message deleted", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            messages.add(messageIndex, message)
            chatAdapter.notifyItemInserted(messageIndex)
        }
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event != DISMISS_EVENT_ACTION) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        chatDao.deleteMessage(message.id)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@ChatActivity, "Message permanently deleted", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })

        snackbar.show()
    }


    private fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
        }
    }
}
