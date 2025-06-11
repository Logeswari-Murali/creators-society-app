package com.example.creatorsociety

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class ChatAdapter(
    private val messages: MutableList<ChatEntity>,
    private val context: Context,
    private val currentUserId: String,
    private val onDeleteMessage: (ChatEntity) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderTextView: TextView = view.findViewById(R.id.tvSender)
        val messageContainer: LinearLayout = view.findViewById(R.id.messageContainer)
        val messageTextView: TextView = view.findViewById(R.id.tvMessage)
        val chatImageView: ImageView = view.findViewById(R.id.imgMessage)
        val deleteIcon: ImageView = view.findViewById(R.id.imgDelete) //Delete Icon

        fun bind(
            context: Context,
            message: ChatEntity,
            currentUserId: String,
            onDeleteMessage: (ChatEntity) -> Unit
        ) {
            val isSentMessage = message.senderId == currentUserId

            // Adjust alignment
            val params = messageContainer.layoutParams as ConstraintLayout.LayoutParams
            if (isSentMessage) {
                messageContainer.setBackgroundResource(R.drawable.chat_bubble_sent)
                senderTextView.text = "You"
                senderTextView.setTextColor(ContextCompat.getColor(context, R.color.grey))
                senderTextView.visibility = View.VISIBLE
                messageTextView.setTextColor(ContextCompat.getColor(context, R.color.brand_1))
                params.startToStart = ConstraintLayout.LayoutParams.UNSET
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            } else {
                messageContainer.setBackgroundResource(R.drawable.chat_bubble_received)
                senderTextView.text = "User"
                senderTextView.setTextColor(ContextCompat.getColor(context, R.color.grey))
                senderTextView.visibility = View.VISIBLE
                messageTextView.setTextColor(ContextCompat.getColor(context, R.color.brand_beige))
                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                params.endToEnd = ConstraintLayout.LayoutParams.UNSET
            }
            messageContainer.layoutParams = params

            // Handle text messages
            if (!message.message.isNullOrEmpty()) {
                messageTextView.text = message.message
                messageTextView.visibility = View.VISIBLE
            } else {
                messageTextView.visibility = View.GONE
            }

            // Handle image messages
            if (!message.imageUri.isNullOrEmpty()) {
                chatImageView.visibility = View.VISIBLE
                Glide.with(context)
                    .load(Uri.parse(message.imageUri))
                    .placeholder(R.drawable.default_img_loader)
                    .error(R.drawable.default_img_loader)
                    .into(chatImageView)
            } else {
                chatImageView.visibility = View.GONE
            }

            // Hide delete icon initially
            deleteIcon.visibility = View.GONE

            // Long press to show delete icon
            messageContainer.setOnLongClickListener {
                deleteIcon.visibility = View.VISIBLE
                true
            }

            deleteIcon.setOnClickListener {
                deleteIcon.visibility = View.GONE
                onDeleteMessage(message) // Only notify ChatActivity
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(context, messages[position], currentUserId, onDeleteMessage)
    }

    override fun getItemCount(): Int = messages.size

    fun removeMessage(message: ChatEntity) {
        val index = messages.indexOfFirst { it.id == message.id }
        if (index != -1) {
            messages.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
