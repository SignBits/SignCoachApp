package com.SDP.signbits.ui.quizCharToFinger

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager

import com.SDP.signbits.R
import com.SDP.signbits.ui.quiz.QuizFragment

class QuizCharToFinger : Fragment() {

    companion object {
        fun newInstance() = QuizCharToFinger()
    }

    private lateinit var viewModel: QuizCharToFingerViewModel

    private var current_char = 0
    val char_array: IntArray = intArrayOf(R.mipmap.ic_letter_b)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(QuizCharToFingerViewModel::class.java)
        val root = inflater.inflate(R.layout.quiz_char_to_finger_fragment_new, container, false)

        val button : Button = root.findViewById(R.id.Button)
        button.setOnClickListener{
            backToQuiz()
        }

        val button_right : Button = root.findViewById(R.id.btn_quiz_right)
        val button_wrong : Button = root.findViewById(R.id.btn_quiz_wrong)

        var text_complete : TextView = root.findViewById(R.id.quiz_complete)

        val image : ImageView = root.findViewById(R.id.quiz_image)
        image.setImageResource(char_array[current_char])
        button_wrong.setOnClickListener{

        }

        button_right.setOnClickListener{
            if(current_char<25) {
                image.setImageResource(char_array[++current_char])
                text_complete.text = current_char.toString() + " of 26 tasks are completed"
            }else
                text_complete.text = "26 of 26 tasks are completed"
        }

        button_wrong.setOnClickListener{

        }

        val button_next : Button = root.findViewById(R.id.button6)
        button_next.setOnClickListener{
            if(current_char<25)
                image.setImageResource(char_array[++current_char])
        }

        return root
    }

    /**
     * This is the method to go back to the quiz fragment. Click on the Quiz text in Char2Finger Page
     * will go back
     */
    private fun backToQuiz() {
        val fragmentManager : FragmentManager = requireFragmentManager()
        fragmentManager.beginTransaction().replace(this.id, QuizFragment()).commit()
    }

    private fun dispatchTakePictureIntent() {
        val REQUEST_IMAGE_CAPTURE = 1
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

    }

}
