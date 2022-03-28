package com.yulmaso.konturtest.ui.screens.contactList

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yulmaso.konturtest.databinding.FragmentContactListBinding
import com.yulmaso.konturtest.domain.entity.Contact
import com.yulmaso.konturtest.ui.BaseFragment
import com.yulmaso.konturtest.utils.Pagination
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
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!viewModel.noMoreData &&
                        adapter.itemCount < layoutManager.findLastVisibleItemPosition() + 15)
                            viewModel.loadMore()
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    override fun initObservers() {
        viewModel.contactsToAdd.observeEvent(viewLifecycleOwner) {
            it?.let { adapter.addItems(it) } ?: adapter.setItems(viewModel.contactsFull)
        }
        viewModel.searchInput.observe(viewLifecycleOwner) {
            binding.clearIv.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        }
        viewModel.error.observe(viewLifecycleOwner) { it?.let { showMessage(it) } }
    }

    override fun onContactClick(item: Contact) {
        viewModel.onContactClick(item)
    }
}