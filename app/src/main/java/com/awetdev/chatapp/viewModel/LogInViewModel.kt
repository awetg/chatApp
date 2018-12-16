package com.awetdev.chatapp.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.awetdev.chatapp.model.AppRepository

class LogInViewModel : ViewModel() {
    private val _appResository = AppRepository.getInstance()

    init {
        if (!_appResository.isConnected()) _appResository.connectToServer()
    }

    fun logIn(userName: String) = _appResository.logIn(userName)
    val loggedIn: LiveData<Boolean> = _appResository.getLoggedIn()
    fun addToken(token: String) = _appResository.addToken(token)
    fun connectToServer() = _appResository.connectToServer()
    fun isConnected(): Boolean = _appResository.isConnected()
    val connected: LiveData<Boolean> = _appResository.getConnected()
    val getConnectionError: LiveData<Boolean> = _appResository.getConnectionError()
    val getLogInError: LiveData<Boolean> = _appResository.getLogInError()
}