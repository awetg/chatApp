package com.awetdev.chatapp.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Toast
import com.awetdev.chatapp.R
import com.awetdev.chatapp.User
import com.awetdev.chatapp.model.database.ChatMessage
import com.awetdev.chatapp.utilities.ChatAdapter
import com.awetdev.chatapp.utilities.ChatItem
import com.awetdev.chatapp.viewModel.ChatViewModel
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    private val fromLayout = R.layout.chat_item_from
    private val toLayout = R.layout.chat_item_to


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val toolbar: Toolbar = findViewById(R.id.select_user_toolbar)
        setSupportActionBar(toolbar)
        val title: String
        val participantList = User.currentChatName.split(",").toMutableList()
        if (participantList.size > 2)
            title = "Group"
        else {
            participantList.remove(User.name)
            title = participantList.joinToString("")
        }
        supportActionBar?.title = "  $title"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setLogo(R.drawable.user_profile_light)

        val adapter = ChatAdapter()

        val viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)

        if (User.accessChatById) viewModel.markReadByChatId(User.currentChatId.toLong()) else viewModel.markReadByChatName(User.currentChatName)

        viewModel.getMessageById().observe(this, Observer { messages ->
            if (messages!!.isNotEmpty()) {
                val tempList = messages.map { if (it.sender == User.name) ChatItem(it, toLayout) else ChatItem(it, fromLayout) }

                adapter.addAll(tempList)
                recyclerview_chat.scrollToPosition(recyclerview_chat.adapter.itemCount - 1)
                User.listOfUnread.remove(messages.component1().chatId)
            }
        })

        recyclerview_chat.adapter = adapter

        send_button_chat.setOnClickListener {
            val input = message_edittext_chat.text.toString()
            message_edittext_chat.text.clear()
            if (!viewModel.sendMessage(input))
                Toast.makeText(this, this.resources.getString(R.string.CAN_NOT_CONNECT), Toast.LENGTH_SHORT).show()
        }
    }
}