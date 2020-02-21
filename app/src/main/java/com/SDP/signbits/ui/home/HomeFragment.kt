package com.SDP.signbits.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.SDP.signbits.MainActivity
import com.SDP.signbits.R
import com.SDP.signbits.RPiHandler
import com.SDP.signbits.TextProgressBar
import com.trycatch.mysnackbar.TSnackbar
import com.trycatch.mysnackbar.Prompt


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)


        //text check button
        val inputBox : TextView = root.findViewById(R.id.inputBoxTextHome)
        val inputbutton : Button = root.findViewById(R.id.inputButton_home)

        inputbutton.setOnClickListener{
            val inputchar : CharSequence = inputBox.editableText
            if (inputchar.length == 0) snack(Prompt.ERROR,"Please input something!")
            else if (inputchar.length > 1) snack(Prompt.ERROR,"This is not a valid Character!")
            else if (MainActivity.alphabet.contains(inputchar[0])){
                snack(Prompt.SUCCESS,"We do support this! The robot will perform it now!")
                robotFingerspell(inputchar)
            }
            else {
                snack(Prompt.WARNING,"Sorry! We currently do not support this!")
            }
        }

        val progressBar : TextProgressBar = root.findViewById(R.id.progressBar)
        progressBar.max = MainActivity.alphabet.count()
        progressBar.progress = 15

        val progressBar2 : TextProgressBar = root.findViewById(R.id.progressBar2)
        progressBar2.max = 100
        progressBar2.progress = 25

        return root
    }




    private fun robotFingerspell(charSequence : CharSequence){
        RPiHandler.getInstance(requireContext()).postFingerSpellRequest(charSequence)
    }


    private fun snack(prompt: Prompt, text: CharSequence){
        val duration = TSnackbar.LENGTH_SHORT
        TSnackbar.make(requireView(), text, duration).setPromptThemBackground(prompt).show();
    }


}