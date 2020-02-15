package com.SDP.signbits.ui.quizFingerToChar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizFingerToCharViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Fragment: Quiz -- Fingerspell to Character"
    }
    val text: LiveData<String> = _text
}
