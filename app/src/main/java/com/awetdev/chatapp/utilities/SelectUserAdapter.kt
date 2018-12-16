package com.awetdev.chatapp.utilities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.awetdev.chatapp.R
import kotlinx.android.synthetic.main.user_row_select_user.view.*


class SelectUserAdapter(private val users: MutableList<String> = mutableListOf()) : RecyclerView.Adapter<SelectUserAdapter.ViewHolder>() {
    private var clickListener: (String) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectUserAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.user_row_select_user, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: SelectUserAdapter.ViewHolder, position: Int) {
        holder.bind(users[position], clickListener)
    }

    fun addAll(usersList: List<String>) {
        val diff = usersList.minus(users)
        if (diff.isNotEmpty()) {
            val fromIndex = users.size
            users.addAll(diff)
            notifyItemRangeChanged(fromIndex, users.size)
        }
    }

    fun setClickListner(newClickListener: (String) -> Unit) {
        this.clickListener = newClickListener
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.username_textview_selectuser

        fun bind(user: String, clickListner: (String) -> Unit) {
            name.text = user
            view.setOnClickListener { clickListner(user) }
        }
    }
}
