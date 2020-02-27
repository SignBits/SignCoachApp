package com.SDP.signbits.ui.learn

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.SDP.signbits.R
import android.graphics.Color;
import com.SDP.signbits.RPiHandler
import com.trycatch.mysnackbar.Prompt
import com.trycatch.mysnackbar.TSnackbar
import java.util.*

class LearnFragment : Fragment() {

    private lateinit var learnViewModel: LearnViewModel

    private var currentChar = 0
    private var previousChar = 0
    val char_array: IntArray = intArrayOf(
        R.mipmap.ic_char_a,
        R.mipmap.ic_letter_b,
        R.mipmap.ic_char_c,
        R.mipmap.ic_char_d,
        R.mipmap.ic_char_e,
        R.mipmap.ic_char_f,
        R.mipmap.ic_char_j,
        R.mipmap.ic_char_a,
        R.mipmap.ic_letter_b,
        R.mipmap.ic_char_c,
        R.mipmap.ic_char_d,
        R.mipmap.ic_char_e,
        R.mipmap.ic_char_f,
        R.mipmap.ic_char_j,
        R.mipmap.ic_char_a,
        R.mipmap.ic_letter_b,
        R.mipmap.ic_char_c,
        R.mipmap.ic_char_d,
        R.mipmap.ic_char_e,
        R.mipmap.ic_char_f,
        R.mipmap.ic_char_j,
        R.mipmap.ic_char_a,
        R.mipmap.ic_letter_b,
        R.mipmap.ic_char_c,
        R.mipmap.ic_char_d,
        R.mipmap.ic_char_e
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        learnViewModel =
            ViewModelProviders.of(this).get(LearnViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_learn, container, false)
        //val textView: TextView = root.findViewById(R.id.text_learn)
        learnViewModel.text.observe(this, Observer {
            //textView.text = it
        })

        val pref : SharedPreferences = requireContext().getSharedPreferences("LearningProgress",0)

        val buttonPrevious : Button = root.findViewById(R.id.learn_previous)
        val buttonNext : Button = root.findViewById(R.id.learn_next)
        val buttonFinger :Button = root.findViewById(R.id.learn_fingerspell)
        val buttonAttempt :Button = root.findViewById(R.id.learn_attempt)
        val image : ImageView = root.findViewById(R.id.learn_image)
        image.setImageResource(char_array[currentChar])
        buttonPrevious.setOnClickListener{
            if (previousChar == currentChar) {
                snack(Prompt.ERROR,"You have no previous history!")
                return@setOnClickListener
            }
            image.setImageResource(char_array[previousChar])
            val temp = currentChar
            currentChar = previousChar
            previousChar = temp
            snack(Prompt.SUCCESS,  "Moved to the Previous Letter")
        }

        buttonNext.setOnClickListener{
            if(currentChar<char_array.size-1) {
                image.setImageResource(char_array[++currentChar])
                previousChar++
                if (previousChar >= char_array.size) previousChar=0
                snack(Prompt.SUCCESS, "Moved to the Next Letter")
                val learning = pref.getInt("Learning", 0)
                if (learning <= char_array.size)
                    pref.edit().putInt("Learning", learning+1).apply()
            } else {
                snack(Prompt.WARNING, "You have done all the challenges")
                currentChar = 0
                image.setImageResource(char_array[currentChar])
                previousChar = char_array.size-1
            }
        }

        buttonFinger.setOnClickListener{
            RPiHandler.getInstance(context!!).postFingerSpellRequest(currentChar.toString())
            snack(Prompt.SUCCESS, "Look at the robot!")
        }

        buttonAttempt.setOnClickListener{
            callVision()
        }

    return root

    }

    /**
     * Method to call vision api to make an attempt
     */
    private fun callVision(){
        return
    }

    private fun snack(prompt: Prompt, text: CharSequence) {
        val duration = TSnackbar.LENGTH_SHORT
        TSnackbar.make(requireView(), text, duration).setPromptThemBackground(prompt).show();
    }

}