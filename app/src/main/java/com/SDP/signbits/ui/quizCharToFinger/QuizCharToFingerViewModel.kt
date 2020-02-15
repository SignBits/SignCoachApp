package com.SDP.signbits.ui.quizCharToFinger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizCharToFingerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
//                value = "This is Fragment: Quiz -- Character to Fingerspell"
    }
    val text: LiveData<String> = _text
}
