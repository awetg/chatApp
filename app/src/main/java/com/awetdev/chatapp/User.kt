package com.awetdev.chatapp

object User {
    var name: String = "unkown"
    var currentChat = "0"
    var currentChatId = ""
    var accessChatById = true
    var currentChatName = "Group"

    var logged = false

    var listOfUnread: MutableMap<Long,Int> = mutableMapOf()

    var selectedUserGroup: MutableList<String> = mutableListOf()
}