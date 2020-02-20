package com.SDP.signbits.ui.setting

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager

import com.SDP.signbits.R
import com.SDP.signbits.ui.quiz.QuizViewModel

class SettingFragment : Fragment() {

    companion object {
        fun newInstance() = SettingFragment()
    }

    private lateinit var settingsviewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsviewModel =
            ViewModelProviders.of(this).get(SettingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_setting, container, false)
        // These are the buttons in the quiz fragment view
        val textupdates : TextView = root.findViewById(R.id.textView2)
        val textsearchbot : TextView = root.findViewById(R.id.textView3)
        val textcontactus : TextView = root.findViewById(R.id.textView4)
        val textterms : TextView = root.findViewById(R.id.textView5)
        val textcache : TextView = root.findViewById(R.id.textView6)
        val textRpi : TextView = root.findViewById(R.id.textView7)
//        textupdates.setOnClickListener{
//            convertToAnotherFragment(SettingTerms.newInstance())
//        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settingsviewModel = ViewModelProviders.of(this).get(SettingViewModel::class.java)
        // TODO: Use the ViewModel

    }
    /**
     * This is the method to convert to another fragment.
     *
     * @param fragment This is the fragment of the destination. Need to be a fragment object instead
     *        of Fragment Id etc
     */
    private fun convertToAnotherFragment(fragment: Fragment){
        val fragmentManger : FragmentManager = requireFragmentManager()
        fragmentManger.beginTransaction().replace(this.id, fragment).commit()

    }
}
