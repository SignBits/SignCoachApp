package com.SDP.signbits.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.SDP.signbits.R
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
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })

        val dictionaryScrollView: LinearLayout = root.findViewById(R.id.dictionaryScrollView)

        populateDictionary(dictionaryScrollView)
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
    }


}