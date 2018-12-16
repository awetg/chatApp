package com.awetdev.chatapp.view

import android.app.NotificationManager
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.text.format.DateUtils
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.awetdev.chatapp.*
import com.awetdev.chatapp.model.database.ChatMessage
import com.awetdev.chatapp.utilities.LastMessageAdapter
import com.awetdev.chatapp.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var userName: String
    private lateinit var adapter: LastMessageAdapter
    private lateinit var searchView: SearchView
    private lateinit var searchLivedata: LiveData<List<ChatMessage>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val sharedPreferences = getSharedPreferences(this.resources.getString(R.string.USER_DATA), Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(this.resources.getString(R.string.USER_NAME), null)
        userName = name ?: ""
        User.name = userName

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        searchLivedata = viewModel.searchDB("##########") //dummy values sent

        val extras = intent.extras
        if (extras != null && extras.containsKey(this.resources.getString(R.string.CHAT_ID))) {
            User.currentChatId = extras.get(this.resources.getString(R.string.CHAT_ID)).toString()
            User.currentChatName = extras.get(this.resources.getString(R.string.CHAT_NAME)).toString()
            User.accessChatById = true

            // once one notification clicked cancel all
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()

            startActivity(Intent(this, ChatActivity::class.java))
        }

        val toolbar: Toolbar = findViewById(R.id.home_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setLogo(R.drawable.ic_chat_bubble_outline_black_24dp)
        supportActionBar?.title = "  chats"

        adapter = LastMessageAdapter()
        adapter.setClickListener { lastMessage ->
            User.currentChat = lastMessage.chatId.toString()
            User.currentChatId = lastMessage.chatId.toString()
            User.accessChatById = true
            User.currentChatName = lastMessage.chatName
            startActivity(Intent(this, ChatActivity::class.java))
        }

        observeLastMessages()

        recyclerview_home.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.home_recycler_divider)!!)
        recyclerview_home.addItemDecoration(dividerItemDecoration)

        fab_home.setOnClickListener {
            startActivity(Intent(this, SelectUserActivity::class.java))
        }

        checkAuth()
    }

    fun observeLastMessages() {
        viewModel.getLastMessages().observe(this, Observer { messages ->
            if (messages!!.isNotEmpty()) {
                messages.forEach {
                    val participantList = it.chatName.split(",").toMutableList()
                    if (participantList.size > 2)
                        it.chatName = "Group"
                    else {
                        participantList.remove(User.name)
                        it.chatName = participantList.joinToString("")
                    }
                }
                adapter.purgeAdd(messages)
            }
        })
    }

    override fun onResume() {
        observeLastMessages()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_view_menu, menu)
        searchView = menu?.findItem(R.id.action_search_home)?.actionView as SearchView
        searchView.setOnQueryTextListener(onQueryListner)
        searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View?) {
                observeLastMessages()
                searchLivedata.removeObserver(observer)
            }

        })
        return true
    }

    private fun checkAuth() {
        if (!User.logged) {

            viewModel.connected.observe(this, Observer { connected ->
                if (connected!!) {
                    viewModel.logIn(userName)
                }
            })

            viewModel.getConnectionError.observe(this, Observer { error ->
                if (error!!) {
                    Toast.makeText(this, this.resources.getString(R.string.CAN_NOT_CONNECT), Toast.LENGTH_LONG).show()
                }
            })

            viewModel.getLogInError.observe(this, Observer { error ->
                if (error!!) {
                    Toast.makeText(this, this.resources.getString(R.string.CAN_NOT_LOGIN_FORWARD_TO_SIGNUP), Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, SignUpActivity::class.java))

                }
            })
        }
    }

    val onQueryListner = (object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null && query.isNotEmpty()) getSearchResult(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText != null && newText.isNotEmpty()) getSearchResult(newText)
            return true
        }

    })

    fun getSearchResult(query: String) {
        val queryText = "%$query%"
        searchLivedata = viewModel.searchDB(queryText)
        searchLivedata.observe(this, observer)
    }

    val observer = Observer<List<ChatMessage>> { resultList ->
        if (resultList != null && resultList.isNotEmpty()) {
            adapter.purgeAdd(resultList)
        }
    }
}