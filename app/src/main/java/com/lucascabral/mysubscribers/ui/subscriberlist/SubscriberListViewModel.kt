package com.lucascabral.mysubscribers.ui.subscriberlist

import androidx.lifecycle.ViewModel
import com.lucascabral.mysubscribers.repository.SubscriberRepository

class SubscriberListViewModel(
    private val repository: SubscriberRepository
) : ViewModel() {

    val allSubscribersEvent = repository.getAllSubscribers()
}