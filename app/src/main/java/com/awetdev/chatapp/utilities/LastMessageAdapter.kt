package com.awetdev.chatapp.utilities

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.awetdev.chatapp.R
import com.awetdev.chatapp.User
import com.awetdev.chatapp.model.database.ChatMessage
import kotlinx.android.synthetic.main.home_chat_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class LastMessageAdapter(private var lastMessages: MutableList<ChatMessage> = mutableListOf()) : RecyclerView.Adapter<LastMessageAdapter.ViewHolder>() {

    private var clickListner: (ChatMessage) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.home_chat_row, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount() = lastMessages.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lastMessages[position], clickListner)
    }

    fun setClickListener(newClickListener: (ChatMessage) -> Unit) {
        clickListner = newClickListener
    }

    fun addAll(mList: List<ChatMessage>) {
        val fromIndex = lastMessages.size
        lastMessages.addAll(mList)
        notifyItemRangeChanged(fromIndex, lastMessages.size)
    }

    fun purgeAdd(mList: List<ChatMessage>) {
        lastMessages = mList.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        val chatName: TextView = view.chatname_textview_home
        val lastMessageTextView: TextView = view.last_message_home
        val timeTextView: TextView = view.time_textview_home
        val unreadCounterTextView = view.counter_textview_home

        fun bind(chatMessage: ChatMessage, clickListener: (ChatMessage) -> Unit) {

            chatName.text = (chatMessage.chatName)
            lastMessageTextView.text = chatMessage.message
            timeTextView.text = if (DateUtils.isToday(chatMessage.time)) SimpleDateFormat("HH:mm").format(Date(chatMessage.time)) else SimpleDateFormat("dd/MM/YY").format(Date(chatMessage.time))

            val unreadCount = User.listOfUnread[chatMessage.chatId]
            if (unreadCount != null && unreadCount > 0) {
                unreadCounterTextView.text = unreadCount.toString()
                unreadCounterTextView.visibility = View.VISIBLE
                lastMessageTextView.typeface = Typeface.DEFAULT_BOLD
            } else {
                unreadCounterTextView.visibility = View.INVISIBLE
                lastMessageTextView.typeface = Typeface.DEFAULT
            }

            view.setOnClickListener { clickListener(chatMessage) }
        }
    }
}