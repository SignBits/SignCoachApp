package com.SDP.signbits.ui.quizCharToFinger

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer

import com.SDP.signbits.R
import com.SDP.signbits.ui.quiz.QuizFragment

class QuizCharToFinger : Fragment() {

    companion object {
        fun newInstance() = QuizCharToFinger()
    }

    private lateinit var viewModel: QuizCharToFingerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(QuizCharToFingerViewModel::class.java)
        val root = inflater.inflate(R.layout.quiz_char_to_finger_fragment, container, false)
        val textView: TextView = root.findViewById(R.id.text_quiz)
        viewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

    fun backToQuiz() {
        val fragmentManager : FragmentManager = requireFragmentManager()
        fragmentManager.beginTransaction().replace(this.id, QuizFragment()).commit()
    }


}
