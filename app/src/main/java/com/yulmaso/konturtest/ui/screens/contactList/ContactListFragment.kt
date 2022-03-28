package com.yulmaso.konturtest.ui.screens.contactList

import android.view.View
import com.yulmaso.konturtest.databinding.FragmentContactListBinding
import com.yulmaso.konturtest.domain.entity.Contact
import com.yulmaso.konturtest.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactListFragment : BaseFragment<FragmentContactListBinding>(
    FragmentContactListBinding::inflate
), ContactListAdapter.ContactListListener {

    companion object {
        fun newInstance() = ContactListFragment()
    }

    private val viewModel by viewModel<ContactListViewModel>()

    private val adapter by lazy { ContactListAdapter(this) }

    override fun initView() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter = adapter
    }

    override fun initObservers() {
        viewModel.contacts.observe(viewLifecycleOwner) { adapter.setItems(it) }
        viewModel.searchInput.observe(viewLifecycleOwner) {
            binding.clearIv.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            adapter.filter.filter(it)
        }
        viewModel.error.observe(viewLifecycleOwner) { showMessage(it) }
    }

    override fun onContactClick(item: Contact) {
        viewModel.onContactClick(item)
    }
}