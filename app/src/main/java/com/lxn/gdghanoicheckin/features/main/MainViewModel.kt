package com.lxn.gdghanoicheckin.features.main

import androidx.lifecycle.ViewModel
import com.lxn.gdghanoicheckin.repository.CheckInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(DelicateCoroutinesApi::class)
class MainViewModel @Inject constructor(
    private val repository: CheckInRepository
) : ViewModel() {

    init {
        getListEmailScanned()
    }

    private fun getListEmailScanned() {
        GlobalScope.launch(Dispatchers.IO) {
            //repository.getAllEmailScanned()
        }
    }
}