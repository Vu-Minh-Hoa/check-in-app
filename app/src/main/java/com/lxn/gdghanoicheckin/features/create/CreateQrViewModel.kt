package com.lxn.gdghanoicheckin.features.create

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxn.gdghanoicheckin.repository.CheckInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateQrViewModel @Inject constructor(
    private val repository: CheckInRepository,
    private val application: Application
) : ViewModel() {

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                createQrWithEmail()
            }
        }
    }

    private suspend fun createQrWithEmail() {
        withContext(Dispatchers.IO) {
            val listEmail = getAllEmailFromSheet()
            generatedQRData(listEmail)
        }
    }


    private suspend fun generatedQRData(listData: ArrayList<String>) {
        listData.forEach { email ->
            delay(2000L)
            val qrBitMap = repository.createBitMapQr(email)
            if (qrBitMap != null) {
                repository.pushQrToFirebase(Pair(email, qrBitMap), handleSuccess = { saveObject ->
                    viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            repository.sendQRCreated(saveObject)
                        }
                    }
                })
            }
        }
    }

    private suspend fun getAllEmailFromSheet(): ArrayList<String> {
        val dataResponse = repository.getEmailsCreateQr()
        val listMerge: ArrayList<String> = ArrayList()
        dataResponse.forEach { data ->
            val stringBuilder = StringBuilder()
            data.forEachIndexed { index, subData ->
                if (index == 0) {
                    stringBuilder.append(subData)
                }
            }
            listMerge.add(stringBuilder.toString())
        }
        val newList = arrayListOf<String>()
        newList.addAll(listMerge)
        newList.removeAt(0)
        return newList
    }


}