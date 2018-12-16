package com.awetdev.chatapp.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.awetdev.chatapp.model.SelectUserRepository

class SelectUserViewModel : ViewModel() {
    private val newMessageRepository = SelectUserRepository.getInstance()

    init {
        newMessageRepository.loadUsers()
    }

    val userList: LiveData<List<String>> = newMessageRepository.getUserList()
}