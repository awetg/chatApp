package com.awetdev.chatapp.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.awetdev.chatapp.R
import com.awetdev.chatapp.User
import com.awetdev.chatapp.utilities.SelectGroupAdapter
import com.awetdev.chatapp.utilities.UserItem
import com.awetdev.chatapp.viewModel.SelectUserViewModel
import kotlinx.android.synthetic.main.activity_select_group.*

class SelectGroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_group)

        val toolbar: Toolbar = findViewById(R.id.group_select_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Create group"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = SelectGroupAdapter()

        adapter.setClickListner { user, pos ->
            user.selected = !user.selected
            adapter.notifyItemChanged(pos)
        }

        val viewModel = ViewModelProviders.of(this).get(SelectUserViewModel::class.java)

        viewModel.userList.observe(this, Observer { userList ->
            if (userList!!.isNotEmpty()) {
                val userItems = userList.map { UserItem(it, false) }
                adapter.addAll(userItems)
            }
        })
        recyclerview_group_select.adapter = adapter

        fab_select_group.setOnClickListener { view ->
            if (adapter.getSelectedUser().isNotEmpty()) {
                User.currentChat = adapter.getSelectedUser().map { it.name }.sorted().joinToString(" ")
                User.currentChatId = ""
                User.accessChatById = false
                val chatName = adapter.getSelectedUser().map { it.name }.toMutableList()
                chatName.add(User.name)
                User.currentChatName = chatName.asSequence().sorted().joinToString(",")
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please select at least one user.", Toast.LENGTH_LONG).show()
            }
        }

    }
}
