package com.lucascabral.mysubscribers.ui.subscriber

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.lucascabral.mysubscribers.R
import com.lucascabral.mysubscribers.data.db.SubscriberDatabase
import com.lucascabral.mysubscribers.data.db.dao.SubscriberDAO
import com.lucascabral.mysubscribers.extension.hideKeyboard
import com.lucascabral.mysubscribers.repository.DatabaseDataSource
import com.lucascabral.mysubscribers.repository.SubscriberRepository
import kotlinx.android.synthetic.main.subscriber_fragment.*

class SubscriberFragment : Fragment(R.layout.subscriber_fragment) {

    private val viewModel: SubscriberViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val subscriberDAO: SubscriberDAO = SubscriberDatabase.getInstance(
                    requireContext()).subscriberDAO
                val repository: SubscriberRepository = DatabaseDataSource(subscriberDAO)
                return SubscriberViewModel(repository) as T
            }
        }
    }

    private val args: SubscriberFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.Subscriber?.let { subscriber ->
            subscriberNameInputEditText.setText(subscriber.name)
            subscriberEmailInputEditText.setText(subscriber.email)
            subscriberAddButton.setText(R.string.subscriber_button_update)
            subscriberDeleteButton.visibility = View.VISIBLE
        }

        observeEvents()
        setListeners()
    }

    private fun observeEvents() {
        viewModel.subscriberStateEventData.observe(viewLifecycleOwner) { SubscriberState ->
            when (SubscriberState) {
                is SubscriberViewModel.SubscriberState.Inserted,
                is SubscriberViewModel.SubscriberState.Updated,
                is SubscriberViewModel.SubscriberState.Deleted -> {
                    clearFields()
                    hideKeyboard()
                    requireView().requestFocus()
                    findNavController().popBackStack()
                }
            }
        }

        viewModel.messageEventData.observe(viewLifecycleOwner) { stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun clearFields() {
        subscriberNameInputEditText.text?.clear()
        subscriberEmailInputEditText.text?.clear()
    }

    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }

    private fun setListeners() {
        subscriberAddButton.setOnClickListener {
            val name = subscriberNameInputEditText.text.toString()
            val email = subscriberEmailInputEditText.text.toString()

            viewModel.addOrUpdateSubscriber(name, email, args.Subscriber?.id ?: 0)
        }

        subscriberDeleteButton.setOnClickListener {
            viewModel.removeSubscriber(args.Subscriber?.id ?: 0)
        }
    }
}