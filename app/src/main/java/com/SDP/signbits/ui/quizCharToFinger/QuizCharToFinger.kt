package com.SDP.signbits.ui.quizCharToFinger

import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.SDP.signbits.MainActivity

import com.SDP.signbits.R
import com.SDP.signbits.RPiHandler
import com.SDP.signbits.ui.quiz.QuizFragment
import com.trycatch.mysnackbar.Prompt
import com.trycatch.mysnackbar.TSnackbar
import kotlinx.android.synthetic.main.quiz_char_to_finger_fragment_new.*
import java.lang.Math.random
import java.lang.StringBuilder

class QuizCharToFinger : Fragment() {

    companion object {
        fun newInstance() = QuizCharToFinger()
    }

    private lateinit var viewModel: QuizCharToFingerViewModel

    private var current_char = 0
    val char_array: IntArray = intArrayOf(
        R.mipmap.ic_char_a,
        R.mipmap.ic_letter_b,
        R.mipmap.ic_char_c,
        R.mipmap.ic_char_d,
        R.mipmap.ic_char_e,
        R.mipmap.ic_char_f,
        R.mipmap.ic_char_g,
        R.mipmap.ic_char_j,
        R.mipmap.ic_char_a,
        R.mipmap.ic_letter_b,
        R.mipmap.ic_char_c,
        R.mipmap.ic_char_d,
        R.mipmap.ic_char_e,
        R.mipmap.ic_char_f,
        R.mipmap.ic_char_g,
        R.mipmap.ic_char_j,
        R.mipmap.ic_char_a,
        R.mipmap.ic_letter_b,
        R.mipmap.ic_char_c,
        R.mipmap.ic_char_d,
        R.mipmap.ic_char_e,
        R.mipmap.ic_char_f,
        R.mipmap.ic_char_g,
        R.mipmap.ic_char_j,
        R.mipmap.ic_char_g,
        R.mipmap.ic_char_j
    )

    // last img for finish


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(QuizCharToFingerViewModel::class.java)
        val root = inflater.inflate(R.layout.quiz_char_to_finger_fragment_new, container, false)

        val userInfo = requireActivity().getSharedPreferences("LearningProgress", 0)
        current_char = userInfo!!.getInt("C2FNumber", 0)

        for (j in 0 until char_array.size-1){
            val index =(random()* (char_array.size-1)).toInt()
            val temp = char_array[j]
            char_array[j] = char_array[index]
            char_array[index] =temp
        }


        val button_start: Button = root.findViewById(R.id.button)


        val button_next: Button = root.findViewById(R.id.button4)
        val isconl=true

        val text_complete: TextView = root.findViewById(R.id.quiz_complete)
        val text_accuracy: TextView = root.findViewById(R.id.quiz_accuracy)

        val image: ImageView = root.findViewById(R.id.quiz_image)
        image.setImageResource(char_array[current_char % char_array.size])

        text_complete.text = (current_char).toString() + " of ${MainActivity.alphabet.count()} tasks " +
        "are completed"

        button_start.setOnClickListener {
            userInfo.edit().putInt("C2FNumber", userInfo.getInt("C2FNumber",0)+1).apply()
            if(isconl){
                    snack(Prompt.SUCCESS, "Correct!\n" + "Moved to the Next Challenge")
                    current_char = (current_char + 1) % char_array.size
                    image.setImageResource(char_array[current_char])
                    text_complete.text = "$current_char of 26 tasks are completed"
                    userInfo.edit().putInt("C2FCorrect", userInfo.getInt("C2FCorrect", 0)+1).apply()
                }else{
                    snack(Prompt.ERROR, "Wrong! Please look at the robot")
                    val corr = userInfo.getInt("C2FCorrect", 0)
                    var total = userInfo.getInt("C2FNumber", 1)
                    if (total == 0) total=1
                    text_accuracy.text = "Accuracy ${corr * 100 / total}%"
                    FingerSpell()
            }
        }


        button_next.setOnClickListener {
            userInfo.edit().putInt("C2FNumber", userInfo.getInt("C2FNumber",0)+1).apply()
            if (current_char < char_array.size -1) {
                current_char = (current_char + 1) % char_array.size
                image.setImageResource(char_array[current_char])
                snack(Prompt.ERROR,  "Moved to the Next Challenge")
            } else {
                snack(Prompt.WARNING,  "You have tried all the challenges!")
                current_char = 0
            }
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(QuizCharToFingerViewModel::class.java)

        imageButtonC2F.setOnClickListener {
            backToQuiz()
        }

    }


    /**
     * This is the method to go back to the quiz fragment. Click on the Quiz text in Char2Finger Page
     * will go back
     */
    private fun backToQuiz() {
        val fragmentManager: FragmentManager = requireFragmentManager()
        fragmentManager.beginTransaction().replace(this.id, QuizFragment()).commit()
    }

    override fun onStop() {
        super.onStop()

        val userInfo = this.activity?.getSharedPreferences("data", 0)
        userInfo?.edit()?.putInt("quiz", current_char)?.commit()
    }

    private fun snack(prompt: Prompt, text: CharSequence) {
        val duration = TSnackbar.LENGTH_SHORT
        TSnackbar.make(requireView(), text, duration).setPromptThemBackground(prompt).show();
    }

    private fun FingerSpell(){
        return
    }

}
