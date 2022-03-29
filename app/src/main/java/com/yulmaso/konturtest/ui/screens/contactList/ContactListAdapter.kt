package com.yulmaso.konturtest.ui.screens.contactList

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yulmaso.konturtest.LOG_TAG
import com.yulmaso.konturtest.databinding.ItemContactBinding
import com.yulmaso.konturtest.domain.entity.Contact

class ContactListAdapter(
    private val listener: ContactListListener
): RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    private val listItems = ArrayList<Contact>()

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
//        Log.d(LOG_TAG, "ADAPTER_SET_ITEMS")
        listItems.clear()
        listItems.addAll(items)
        notifyDataSetChanged()
    }

    fun addItems(items: List<Contact>) {
//        Log.d(LOG_TAG, "ADAPTER_ADD_ITEMS")
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