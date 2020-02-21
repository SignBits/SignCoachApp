package com.SDP.signbits.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.SDP.signbits.MainActivity
import com.SDP.signbits.R
import com.SDP.signbits.VolleySingleton
import com.SDP.signbits.ui.setting.SettingFragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import com.SDP.signbits.RPiHandler


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
            if (inputchar.length > 1) snackBar("This is not a valid Character!")
            else if (MainActivity.alphabet.contains(inputchar[0])){
                snackBar("We do support this! The robot will perform it now!")
                robotFingerspell(inputchar)
            }
            else {
                snackBar("Sorry! We currently do not support this!")
            }
        }

        val progressBar : ProgressBar = root.findViewById(R.id.progressBar)
        progressBar.max = MainActivity.alphabet.count()
        progressBar.progress = 15
        val textViewpb1 : TextView = root.findViewById(R.id.textViewPB1)
        textViewpb1.text = String.format("Your Learning Progress is %d", 15)

        val progressBar2 : ProgressBar = root.findViewById(R.id.progressBar2)
        progressBar2.max = 100
        progressBar2.progress = 15
        val textViewpb2 : TextView = root.findViewById(R.id.textViewPB2)
        textViewpb2.text = String.format("Your Quiz Challenge Accuracy is %d", 15)

        return root
    }


//    private fun populateDictionary(linearLayout: LinearLayout){
//        val alphabet = 'A'..'Z'
//
//        alphabet.forEach {
//            val btnTag = Button(activity)
//            btnTag.layoutParams = LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//            btnTag.textSize = 32.0f
//            btnTag.textAlignment = View.TEXT_ALIGNMENT_CENTER
//            btnTag.text = it.toString()
//            btnTag.id = it.toInt()
//            btnTag.setOnClickListener { _ -> RPIHandler.getInstance(this.requireContext())
//                .sendFingerSpellRequest(
//                    it.toString()
//                )
//            }
//            linearLayout.addView(btnTag)
//        }
//    }

    private fun populateDictionary(linearLayout: LinearLayout){
        val alphabet = 'A'..'Z'

        alphabet.forEach {
            val btnTag = Button(activity)
            btnTag.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            btnTag.textSize = 32.0f
            btnTag.textAlignment = View.TEXT_ALIGNMENT_CENTER
            btnTag.text = it.toString()
            btnTag.id = it.toInt()
            btnTag.setOnClickListener { _ -> RPiHandler.getInstance(this.requireContext())
                .postFingerSpellRequest(
                    it.toString()
                )
            }
            linearLayout.addView(btnTag)
        }
    private fun robotFingerspell(charSequence : CharSequence){
        return
    }


    private fun snackBar(text: CharSequence){
        val duration = Snackbar.LENGTH_LONG
        val snackbar = Snackbar.make(requireView(), text, duration)
        snackbar.show()
    }


}