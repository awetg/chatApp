package com.awetdev.chatapp.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.awetdev.chatapp.R
import com.awetdev.chatapp.User
import com.awetdev.chatapp.viewModel.LogInViewModel
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {

    private lateinit var _viewModel: LogInViewModel
    private lateinit var _userName: String
    private lateinit var _token: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val sharedPreferences = getSharedPreferences(this.resources.getString(R.string.USER_DATA), Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(this.resources.getString(R.string.TOKEN), null)
        val name = sharedPreferences.getString(this.resources.getString(R.string.USER_NAME), null)
        _token = token ?: ""
        _userName = name ?: ""

        _viewModel = ViewModelProviders.of(this).get(LogInViewModel::class.java)
        _viewModel.loggedIn.observe(this, Observer { loggedIn ->
            if (loggedIn!!) {
                if (_token.isNotEmpty()) _viewModel.addToken(_token)
                sharedPreferences.edit().putString(this.resources.getString(R.string.USER_NAME), _userName).apply()
                User.name = _userName
                finish()
            }
        })

        _viewModel.getLogInError.observe(this, Observer { error ->
            if (error!!) {
                Toast.makeText(this, this.resources.getString(R.string.CAN_NOT_LOGIN), Toast.LENGTH_LONG).show()
            }
        })

        login_button_login.setOnClickListener {
            if (_viewModel.isConnected()) {
                _userName = username_editText_logIn.text.toString()
                if (_userName.isNotEmpty()) _viewModel.logIn(_userName) else Toast.makeText(this, this.resources.getString(R.string.MUST_PROVIDE_USERNAME), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, this.resources.getString(R.string.SERVER_NOT_CONNECTED_TRYING_TO_CONNECT), Toast.LENGTH_LONG).show()
                _viewModel.connectToServer()
            }
        }

        signUp_textView_login.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }
}
