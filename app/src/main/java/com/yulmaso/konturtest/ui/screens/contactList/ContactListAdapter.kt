package com.yulmaso.konturtest.ui.screens.contactList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.yulmaso.konturtest.databinding.ItemContactBinding
import com.yulmaso.konturtest.domain.entity.Contact

class ContactListAdapter(
    private val listener: ContactListListener
): RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>(), Filterable {

    private val fullList = ArrayList<Contact>()
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

    override fun getFilter() = object : Filter() {
        override fun performFiltering(searchInput: CharSequence?): FilterResults {
            val filteredList: MutableList<Contact> = ArrayList()

            if (searchInput == null || searchInput.isEmpty()) {
                filteredList.addAll(fullList)
            } else {
                val filterPattern = searchInput.toString().lowercase().trim()
                for (item in fullList) {
                    val phoneWithoutSymbols = item.phone
                        .replace(" ", "")
                        .replace("(", "")
                        .replace(")", "")
                        .replace("-", "")
                    if (item.name.lowercase().contains(filterPattern) || // Поиск по имени
                        phoneWithoutSymbols.contains(filterPattern) || // Поиск по номеру телефона
                        item.phone.contains(filterPattern)
                    ) {
                        filteredList.add(item)
                    }
                }
            }

            return FilterResults().apply { values = filteredList }
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(p0: CharSequence?, results: FilterResults) {
            listItems.clear()
            listItems.addAll(results.values as List<Contact>)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Contact>) {
        listItems.clear()
        listItems.addAll(items)
        fullList.clear()
        fullList.addAll(items)
        notifyDataSetChanged()
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