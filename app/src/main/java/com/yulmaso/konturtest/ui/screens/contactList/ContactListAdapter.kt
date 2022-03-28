package com.yulmaso.konturtest.ui.screens.contactList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.yulmaso.konturtest.databinding.ItemContactBinding
import com.yulmaso.konturtest.domain.entity.Contact
import java.util.*

class ContactListAdapter(
    private val listener: ContactListListener
): RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    private val listItems = LinkedList<Contact>()

    interface ContactListListener {
        fun onContactClick(item: Contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(listItems[position])
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Contact>) {
        listItems.clear()
        listItems.addAll(items)
        notifyDataSetChanged()
    }

    fun addItems(items: List<Contact>) {
        val newFirst = listItems.size
        listItems.addAll(items)
        notifyItemRangeInserted(newFirst, items.size)
    }

    inner class ContactViewHolder(
        private val binding: ItemContactBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Contact) {
            binding.contact = item
            itemView.setOnClickListener { listener.onContactClick(item) }
        }
    }
}