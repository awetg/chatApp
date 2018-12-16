package com.awetdev.chatapp.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sginup.*
import com.awetdev.chatapp.*
import com.awetdev.chatapp.viewModel.SignUpViewModel


class SignUpActivity : AppCompatActivity() {

    private lateinit var _username: String
    private lateinit var _token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sginup)

        val sharedPreferences = getSharedPreferences(this.resources.getString(R.string.USER_DATA), Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(this.resources.getString(R.string.TOKEN), null)
        val name = sharedPreferences.getString(this.resources.getString(R.string.USER_NAME), null)
        _token = token ?: ""
        _username = name ?: ""

        val viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        viewModel.loggedIn.observe(this, Observer { loggedIn ->
            if (loggedIn!!) {
                if (_token.isNotEmpty()) viewModel.addToken(_token)
                sharedPreferences.edit().putString(this.resources.getString(R.string.USER_NAME), _username).apply()
                User.name = _username
                finish()
            }
        })

        signUp_button_signUp.setOnClickListener {
            if (viewModel.isConnected()) {
                _username = username_editText_signUp.text.toString()
                if (_username.isNotEmpty()) viewModel.setUserName(_username) else Toast.makeText(this, this.resources.getString(R.string.MUST_PROVIDE_USERNAME), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, this.resources.getString(R.string.SERVER_NOT_CONNECTED_TRYING_TO_CONNECT), Toast.LENGTH_LONG).show()
                viewModel.connectToServer()
            }
        }

        already_have_account_signup.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }
    }
}
