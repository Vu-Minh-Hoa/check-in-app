package com.lxn.gdghanoicheckin.features.confirm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxn.gdghanoicheckin.constant.TypeCheckIn
import com.lxn.gdghanoicheckin.features.confirm.ConfirmActivity.Companion.KEY
import com.lxn.gdghanoicheckin.network.cache.EmailCaching
import com.lxn.gdghanoicheckin.network.model.SaveEmailScannedBody
import com.lxn.gdghanoicheckin.network.retrofit.POST_ACTION_SAVE_EMAIL_SCANNED
import com.lxn.gdghanoicheckin.repository.CheckInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, private val checkInRepository: CheckInRepository
) : ViewModel() {
    var stateConfirm: MutableLiveData<Pair<TypeCheckIn, String>> = MutableLiveData()
    private var state = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            getDataFromScan()
        }
    }

    private suspend fun getDataFromScan() {
        val data = savedStateHandle.get<String>(KEY) ?: return
        withContext(Dispatchers.IO) {
            val isCheckEmail = checkEmailExited(data)
            if (!isEmail(data)) {
                stateConfirm.postValue(Pair(TypeCheckIn.NotEmail, data))
            } else if (isCheckEmail) {
                stateConfirm.postValue(Pair(TypeCheckIn.Existed, data))
            } else {
                checkInRepository.sendQrScanned(
                    SaveEmailScannedBody(
                        action = POST_ACTION_SAVE_EMAIL_SCANNED,
                        email = data
                    ), stateConfirm
                )
            }
        }
    }

    private fun checkEmailExited(email: String): Boolean {
        val listEmail = EmailCaching.listEmailScanned
        return listEmail.contains(email)
    }

    private val EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
    private fun isEmail(data: String): Boolean {
        val regex = Regex(EMAIL_REGEX)
        return regex.matches(data)
    }

}