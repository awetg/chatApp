package com.awetdev.chatapp.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.awetdev.chatapp.R
import com.awetdev.chatapp.User
import com.awetdev.chatapp.utilities.SelectUserAdapter
import com.awetdev.chatapp.viewModel.SelectUserViewModel
import kotlinx.android.synthetic.main.activity_select_user.*

class SelectUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)

        val toolbar: Toolbar = findViewById(R.id.select_user_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Select user"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = SelectUserAdapter()

        adapter.setClickListner { user ->
            User.currentChat = user
            User.currentChatId = ""
            User.accessChatById = false
            User.currentChatName = listOf(user, User.name).asSequence().sorted().joinToString(",")
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }

        val viewModel = ViewModelProviders.of(this).get(SelectUserViewModel::class.java)

        viewModel.userList.observe(this, Observer { userList ->
            if (userList!!.isNotEmpty()) {
                adapter.addAll(userList)
            }
        })
        recyclerview_select_user.adapter = adapter

        fab_togroup_select_select_user.setOnClickListener {
            startActivity(Intent(this, SelectGroupActivity::class.java))
            finish()
        }
    }
}
