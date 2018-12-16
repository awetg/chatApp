package com.awetdev.chatapp.utilities

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.awetdev.chatapp.R
import kotlinx.android.synthetic.main.user_row_select_user.view.*

class UserItem(val name: String, var selected: Boolean)


class SelectGroupAdapter(private val userItems: MutableList<UserItem> = mutableListOf()) : RecyclerView.Adapter<SelectGroupAdapter.ViewHolder>() {
    private var clickListener: (UserItem, Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectGroupAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.user_row_select_user, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount() = userItems.size

    override fun onBindViewHolder(holder: SelectGroupAdapter.ViewHolder, position: Int) {
        val item = userItems[position]
        val bgColor = if (item.selected) Color.argb(154, 107, 191, 255) else Color.TRANSPARENT
        holder.itemView.setBackgroundColor(bgColor)
        holder.bind(item, clickListener, position)
    }

    fun addAll(usersList: List<UserItem>) {
        val diff = usersList.minus(userItems)
        if (diff.isNotEmpty()) {
            val fromIndex = userItems.size
            userItems.addAll(diff)
            notifyItemRangeChanged(fromIndex, userItems.size)
        }
    }

    fun setClickListner(newClickListener: (UserItem, Int) -> Unit) {
        this.clickListener = newClickListener
    }

    fun getSelectedUser(): List<UserItem> = userItems.filter { it.selected }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.username_textview_selectuser

        fun bind(user: UserItem, clickListner: (UserItem, Int) -> Unit, position: Int) {
            name.text = user.name
            view.setOnClickListener { clickListner(user, position) }
        }
    }
}
