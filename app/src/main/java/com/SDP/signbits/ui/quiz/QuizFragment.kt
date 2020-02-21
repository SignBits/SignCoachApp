package com.SDP.signbits.ui.quiz

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.SDP.signbits.R
import com.SDP.signbits.ui.quizCharToFinger.QuizCharToFinger
import com.SDP.signbits.ui.quizFingerToChar.QuizFingerToChar
import com.google.android.material.snackbar.SnackbarContentLayout
import kotlinx.android.synthetic.main.fragment_quiz.*

class QuizFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizViewModel =
            ViewModelProviders.of(this).get(QuizViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_quiz, container, false)

        // These are the buttons in the quiz fragment view
        val buttonC2F : Button = root.findViewById(R.id.button)
        val buttonF2C : Button = root.findViewById(R.id.button2)

        buttonC2F.setOnClickListener{
            convertToAnotherFragment(QuizCharToFinger.newInstance())
        }

        buttonF2C.setOnClickListener{
            convertToAnotherFragment(QuizFingerToChar.newInstance())
        }
        return root
    }

    /**
     * This is the method to convert to another fragment.
     *
     * @param fragment This is the fragment of the destination. Need to be a fragment object instead
     *        of Fragment Id etc
     */
    private fun convertToAnotherFragment(fragment: Fragment){
        val fragmentManger : FragmentManager = requireFragmentManager()
        val transaction = fragmentManger.beginTransaction().apply {
            replace(this@QuizFragment.id, fragment)
            addToBackStack(null)
        }
        transaction.commit()

    }
}