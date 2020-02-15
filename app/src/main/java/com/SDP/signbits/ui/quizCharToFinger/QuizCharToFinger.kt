package com.SDP.signbits.ui.quizCharToFinger

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer

import com.SDP.signbits.R

class QuizCharToFinger : Fragment() {

    companion object {
        fun newInstance() = QuizCharToFinger()
    }

    private lateinit var viewModel: QuizCharToFingerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.quiz_char_to_finger_fragment, container, false)
        val textView: TextView = root.findViewById(R.id.text_quiz)
        viewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(QuizCharToFingerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
