package com.SDP.signbits.ui.quizFingerToChar

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.SDP.signbits.R
import com.SDP.signbits.VolleySingleton
import com.SDP.signbits.ui.quiz.QuizFragment


class QuizFingerToChar : Fragment() {

    companion object {
        fun newInstance() =
            QuizFingerToChar()
    }

    private lateinit var viewModel: QuizFingerToCharViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(QuizFingerToCharViewModel::class.java)
        val root = inflater.inflate(R.layout.quiz_finger_to_char_fragment, container, false)

        val button : Button = root.findViewById(R.id.buttonTopBar)
        button.setOnClickListener{
            val fragmentManager : FragmentManager = requireFragmentManager()
            fragmentManager.beginTransaction().remove(this).commit()
        }

        val buttonStart :Button = root.findViewById(R.id.buttonStart)
        var isconcl = true
        buttonStart.setOnClickListener(){
            if (isconcl){
                robotFingerSpell()
                buttonStart.setTextColor(Color.RED)
            }
        }
        return root
    }


    private fun robotFingerSpell(){
        return
    }


}
