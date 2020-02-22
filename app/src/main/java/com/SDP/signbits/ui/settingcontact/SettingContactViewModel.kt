package com.SDP.signbits.ui.settingcontact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingContactViewModel : ViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = "This is contactus Fragment"
    }
    val text: LiveData<String> = _text
}
