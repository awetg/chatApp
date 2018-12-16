package com.awetdev.chatapp.utilities

import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.awetdev.chatapp.R
import com.awetdev.chatapp.model.database.ChatMessage
import kotlinx.android.synthetic.main.chat_item_from.view.*
import kotlinx.android.synthetic.main.chat_item_to.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatItem(val chatMessage: ChatMessage, val layout: Int)

class ChatAdapter(private val messageList: MutableList<ChatItem> = mutableListOf()) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun getItemCount() = messageList.size

    fun addAll(mList: List<ChatItem>) {
        if (messageList.size < mList.size) {
            val fromIndex = messageList.size
            val subList = mList.subList(fromIndex, mList.size)
            messageList.addAll(subList)
            notifyItemRangeChanged(fromIndex, messageList.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemViewType(position: Int): Int {
        val vh = messageList[position]
        return vh.layout
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val from = R.layout.chat_item_from
        fun bind(chatItem: ChatItem) {
            if (chatItem.layout == from) {
                view.chat_item_from_textview.text = chatItem.chatMessage.message
                view.user_chat_item_from.text = chatItem.chatMessage.sender
                view.time_chat_item_from.text = if (DateUtils.isToday(chatItem.chatMessage.time)) SimpleDateFormat("HH:mm").format(Date(chatItem.chatMessage.time)) else SimpleDateFormat("dd/MM/YY").format(Date(chatItem.chatMessage.time))
            } else {
                view.chat_item_to_textview.text = chatItem.chatMessage.message
                view.time_chat_item_to.text = if (DateUtils.isToday(chatItem.chatMessage.time)) SimpleDateFormat("HH:mm").format(Date(chatItem.chatMessage.time)) else SimpleDateFormat("dd/MM/YY").format(Date(chatItem.chatMessage.time))
            }
        }

    }
}