package com.SDP.signbits.ui.quizFingerToChar

import android.app.VoiceInteractor
import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.SDP.signbits.MainActivity
import com.SDP.signbits.R
import com.SDP.signbits.RPiHandler
import java.lang.StringBuilder
import com.trycatch.mysnackbar.TSnackbar
import com.trycatch.mysnackbar.Prompt


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

        val button : ImageButton = root.findViewById(R.id.imageButton3)
        button.setOnClickListener{
            val fragmentManager : FragmentManager = requireFragmentManager()
            fragmentManager.beginTransaction().apply {
                remove(this@QuizFingerToChar)
            }.commit()
            fragmentManager.popBackStack()
        }

        val buttonStart :Button = root.findViewById(R.id.buttonStart)
        var symbol : CharSequence = ""
        var isconcl = true
        buttonStart.setOnClickListener{
            if (isconcl){
                symbol = randomString()
                Log.d("Learning Random String", symbol.toString())
                RPiHandler.getInstance(requireContext()).postFingerSpellRequest(symbol)
//                buttonStart.setTextColor(Color.RED)
                isconcl = false
                snack(Prompt.SUCCESS, "Look at the SignBits!")
            } else {
                snack(Prompt.ERROR, "You have already started!!")
            }
        }

        val hintbutton : Button =root.findViewById(R.id.buttonHint)
        hintbutton.setOnClickListener(){
            if (symbol == "") snack(Prompt.ERROR, "You have not started!")
            else {
                snack(Prompt.WARNING, "Look at the Robot Again!")
                RPiHandler.getInstance(requireContext()).postFingerSpellRequest(symbol)
            }
        }

        val submitButton : Button = root.findViewById(R.id.buttonSubmit)
        submitButton.setOnClickListener(){
            if (symbol == "") snack(Prompt.ERROR, "You have not started!")
            else {
                val text : TextView = root.findViewById(R.id.editTextF2C)
                if (text.editableText.contains(symbol) && text.editableText.length == symbol
                        .length){
                    snack(Prompt.SUCCESS, "Correct!\n" + "Moved to the Next Challenge and click "
                            + "on the START " +"button now")
                    text.editableText.clear()
                    symbol = ""
                    isconcl = true
//                    buttonStart.setTextColor(Color.BLACK)
                } else {
                    snack(Prompt.ERROR, "Wrong! The Correct one is $symbol")
                    text.editableText.clear()
                }

            }
        }

        val nextButton : Button = root.findViewById(R.id.buttonNext)
        nextButton.setOnClickListener{
            symbol = ""
            snack(Prompt.SUCCESS, "Moved to the Next Challenge!")
            isconcl = true
//            buttonStart.setTextColor(Color.BLACK)
        }

        return root
    }


    private fun randomString() : CharSequence {
        val generator = java.util.Random()
        val stringBuilder : StringBuilder = StringBuilder()
        val rlength = generator.nextInt(5)
        var char : Char
        for (i in 0..rlength){
            char = MainActivity.alphabet.random()
            stringBuilder.append(char)

        }
        return stringBuilder.toString()
    }

    private fun snack(prompt: Prompt, text: CharSequence){
        val duration = TSnackbar.LENGTH_SHORT
        TSnackbar.make(requireView(), text, duration).setPromptThemBackground(prompt).show();
    }

    private fun correct(){
        val pref : SharedPreferences = requireContext().getSharedPreferences("LearningProgress",0)

    }


}
