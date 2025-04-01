package com.example.projectpagun.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatusViewModel : ViewModel() {

    private val _statusList = MutableLiveData<List<String>>()
    val statusList: LiveData<List<String>> = _statusList

    fun updateStatusList(newStatusList: List<String>) {
        _statusList.value = newStatusList
    }
}
