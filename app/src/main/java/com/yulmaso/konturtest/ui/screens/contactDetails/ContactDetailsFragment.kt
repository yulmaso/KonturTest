package com.yulmaso.konturtest.ui.screens.contactDetails

import android.os.Bundle
import android.view.View
import com.yulmaso.konturtest.R
import com.yulmaso.konturtest.databinding.FragmentContactDetailsBinding
import com.yulmaso.konturtest.domain.entity.Contact
import com.yulmaso.konturtest.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ContactDetailsFragment : BaseFragment<FragmentContactDetailsBinding>(
    FragmentContactDetailsBinding::inflate
) {

    companion object {
        private const val ARG_CONTACT = "contact_arg"

        /**
         * @param contact   - Contact to display
         * @return A new instance of fragment ContactDetailsFragment.
         */
        fun newInstance(contact: Contact) =
            ContactDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CONTACT, contact)
                }
            }
    }

    private val viewModel by viewModel<ContactDetailsViewModel> {
        parametersOf(requireArguments().getParcelable(ARG_CONTACT))
    }

    override fun initView() {
        binding.viewmodel = viewModel
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener { viewModel.onBackBtnClick() }
    }
}