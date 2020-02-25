package com.SDP.signbits.ui.learn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.SDP.signbits.R
import android.graphics.Color;
import com.SDP.signbits.ui.quizCharToFinger.QuizCharToFinger

class LearnFragment : Fragment() {

    private lateinit var learnViewModel: LearnViewModel

    private var current_char = 0
    val char_array: IntArray = intArrayOf(R.mipmap.ic_char_a,R.mipmap.ic_letter_b,R.mipmap
        .ic_char_c,R.mipmap.ic_char_d,R.mipmap.ic_char_e,R.mipmap.ic_char_f,R.mipmap.ic_char_j)

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

        val button_previous : Button = root.findViewById(R.id.learn_previous)
        val button_next : Button = root.findViewById(R.id.learn_next)
        val button_finger :Button = root.findViewById(R.id.learn_fingerspell)

        val image : ImageView = root.findViewById(R.id.learn_image)
        image.setImageResource(char_array[current_char])
        button_previous.setOnClickListener{
            if(current_char>0)
                image.setImageResource(char_array[--current_char])
        }

        button_next.setOnClickListener{
            if(current_char<25)
                image.setImageResource(char_array[++current_char])

        }

        var iscon =true
        button_finger.setOnClickListener{
            if(iscon){
                FingerSpell()
                button_finger.setTextColor(Color.RED)
            }


        }

    return root

    }

    private fun FingerSpell(){
        return
    }

    override fun onStop() {
        super.onStop()

        val userInfo = this.activity?.getSharedPreferences("data", 0)
        userInfo?.edit()?.putInt("learn", current_char)?.commit()
    }
}