package com.awetdev.chatapp.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.awetdev.chatapp.model.AppRepository

class SignUpViewModel : ViewModel() {
    private val appRepository = AppRepository.getInstance()
    val loggedIn: LiveData<Boolean> = appRepository.getLoggedIn()

    init {
        if (!appRepository.isConnected()) appRepository.connectToServer()
    }

    fun setUserName(username: String) = appRepository.setUserName(username)
    fun addToken(token: String) = appRepository.addToken(token)
    fun connectToServer() = appRepository.connectToServer()
    fun isConnected() = appRepository.isConnected()

}