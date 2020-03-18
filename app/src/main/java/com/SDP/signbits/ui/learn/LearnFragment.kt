package com.SDP.signbits.ui.learn

import android.app.Activity
import android.content.Intent
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
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.SDP.signbits.ClassifierActivity
import com.SDP.signbits.MainActivity
import com.SDP.signbits.RPiHandler
import com.trycatch.mysnackbar.Prompt
import com.trycatch.mysnackbar.TSnackbar

class LearnFragment : Fragment() {

    private lateinit var learnViewModel: LearnViewModel

    private var currentChar = 0

    val charImageArray: IntArray = intArrayOf(
        R.mipmap.ic_char_f,
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
        R.mipmap.ic_char_d
    )
    val requestVisionCode = 1
    val alphabet = MainActivity.alphabet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        learnViewModel =
            ViewModelProviders.of(this).get(LearnViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_learn, container, false)

        Log.d("LearnCreate", "Learn Fragment is created")
        val pref : SharedPreferences = requireActivity().getSharedPreferences("LearningProgress",0)
        currentChar = getCurrentChar()?.let { alphabet.indexOf(it) }!!

        val buttonPrevious : android.widget.ImageButton = root.findViewById(R.id.learn_previous)
        val buttonNext : android.widget.ImageButton = root.findViewById(R.id.learn_next)
        val buttonFinger :Button = root.findViewById(R.id.learn_fingerspell)
        val buttonAttempt :Button = root.findViewById(R.id.learn_attempt)
        val image : ImageView = root.findViewById(R.id.learn_image)
        image.setImageResource(charImageArray[currentChar])


        buttonPrevious.setOnClickListener{
            if (getPreviousChar() == null) {
                snack(Prompt.ERROR,"You have no previous history!")
                return@setOnClickListener
            }
            val previousChar = getPreviousChar()?.let { it1 -> alphabet.indexOf(it1) }
            image.setImageResource(charImageArray[previousChar!!])
            updatePref(getPreviousChar()!!)
            snack(Prompt.SUCCESS,  "Moved to the Previous Letter")
        }

        buttonNext.setOnClickListener{
            snack(Prompt.SUCCESS, "Moved to the Next Letter")
            currentChar = (currentChar + 1) % alphabet.length
            updatePref(alphabet[currentChar])
            image.setImageResource(charImageArray[currentChar])
        }

        buttonFinger.setOnClickListener{
            RPiHandler.getInstance(requireActivity()).postFingerSpellRequest(currentChar.toString())
            snack(Prompt.SUCCESS, "Look at the robot!")
        }

        buttonAttempt.setOnClickListener{
            callVision(getCurrentChar().toString())
        }

    return root

    }


    private fun getCurrentChar() : Char? {
        val pref: SharedPreferences = requireActivity().getSharedPreferences("LearningProgress", 0)
        return pref.getString("LearnSequence", "A")?.last()
    }

    private fun getPreviousChar() : Char? {
        val pref: SharedPreferences = requireActivity().getSharedPreferences("LearningProgress", 0)
        val string = pref.getString("LearnSequence", "A")
        if (string != null && string.length >= 2) {
            return string.get(string.length-2)
        } else {
            snack(Prompt.ERROR, "You don't have any previous history!")
            return null
        }

    }

    private fun updatePref(new : Char) {
        val pref: SharedPreferences = requireActivity().getSharedPreferences("LearningProgress", 0)
        val string = pref.getString("LearnSequence","A")
        pref.edit().putString("LearnSequence", String.format("%s%s".takeLast(20), string, new)).apply()
    }
    /**
     * Method to call vision api to make an attempt
     */
    private fun callVision(charSequence: CharSequence){
        val intent = Intent(activity, ClassifierActivity::class.java).apply {
            putExtra("CharSequence", charSequence)
        }
        startActivityForResult(intent, requestVisionCode)

    }

    private fun snack(prompt: Prompt, text: CharSequence) {
        val duration = TSnackbar.LENGTH_SHORT
        TSnackbar.make(requireView(), text, duration).setPromptThemBackground(prompt).show();
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("Fragment", "Learn")
        outState.putBundle("Nav", findNavController().saveState())
        Log.d("SaveState","StateSaved")
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        Log.d("LearnResume", "Learn Fragment is resumed")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestVisionCode)
            if (resultCode ==  Activity.RESULT_OK) {
                val pref : SharedPreferences = requireActivity().getSharedPreferences("LearningProgress",0)
                val learning = pref.getInt("Learning", 0)
                if (learning <= charImageArray.size)
                    pref.edit().putInt("Learning", learning+1).apply()
                snack(Prompt.SUCCESS, "Correct")
            } else {
                snack(Prompt.ERROR, "Wrong")
            }

    }
}