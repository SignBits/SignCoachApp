package com.SDP.signbits.ui.quizCharToFinger

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.FragmentManager

import com.SDP.signbits.R
import com.SDP.signbits.ui.quiz.QuizFragment
import com.SDP.signbits.ui.setting.SettingViewModel
import com.trycatch.mysnackbar.Prompt
import com.trycatch.mysnackbar.TSnackbar
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.quiz_char_to_finger_fragment_new.*

class QuizCharToFinger : Fragment() {

    companion object {
        fun newInstance() = QuizCharToFinger()
    }

    private lateinit var viewModel: QuizCharToFingerViewModel

    private var current_char = 0
    val char_array: IntArray = intArrayOf(
        R.mipmap.ic_char_a,
        R.mipmap.ic_letter_b,
        R.mipmap
            .ic_char_c,
        R.mipmap.ic_char_d,
        R.mipmap.ic_char_e,
        R.mipmap.ic_char_f,
        R.mipmap.ic_char_g,
        R
            .mipmap.ic_char_j
    )
    // last img for finish


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(QuizCharToFingerViewModel::class.java)
        val root = inflater.inflate(R.layout.quiz_char_to_finger_fragment_new, container, false)

        val userInfo = this.activity?.getSharedPreferences("data", 0)
        current_char = userInfo!!.getInt("quiz", 0)



        val button_start: Button = root.findViewById(R.id.button)


        val button_next: Button = root.findViewById(R.id.button4)
        val isconl=true

        var text_complete: TextView = root.findViewById(R.id.quiz_complete)
        var text_accuracy: TextView = root.findViewById(R.id.quiz_accuracy)

        val image: ImageView = root.findViewById(R.id.quiz_image)
        image.setImageResource(char_array[current_char])

        text_complete.text = (current_char).toString() + " of 26 tasks are completed"

        button_start.setOnClickListener {
        if(isconl){
                snack(Prompt.SUCCESS, "Correct!\n" + "Moved to the Next Challenge")
                image.setImageResource(char_array[++current_char])
                text_complete.text = current_char.toString() + " of 26 tasks are completed"
            }else{
                snack(Prompt.ERROR, "Wrong! Please look at the robot")
                text_accuracy.text = "Accuracy 80%"
                FingerSpell()
        }
        }


        button_next.setOnClickListener {
            if (current_char < 25) {
                image.setImageResource(char_array[++current_char])
                snack(Prompt.ERROR,  "Moved to the Next Challenge")
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
