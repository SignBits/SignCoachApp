package com.SDP.signbits.ui.settingTermsAndConditions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingTermViewModel : ViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = "This is terms&c Fragment"
    }
    val text: LiveData<String> = _text
}
