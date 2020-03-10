package com.SDP.signbits.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.SDP.signbits.R
import com.SDP.signbits.ui.settingTermsAndConditions.SettingTermFragment
import com.trycatch.mysnackbar.Prompt
import com.trycatch.mysnackbar.TSnackbar
import kotlinx.android.synthetic.main.fragment_setting.*


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


        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settingsviewModel = ViewModelProviders.of(this).get(SettingViewModel::class.java)

        linear_5.setOnClickListener {
            val menu = PopupMenu(context, it)
            menu.inflate(R.menu.popup_menu)
            menu.show()
        }

        linear_4.setOnClickListener {
            terms_and_conditions()
        }

        linear_1.setOnClickListener(){
            check_for_update()
        }

        linear_2.setOnClickListener(){
        }

        linear_3.setOnClickListener(){
            clearPref()
        }

    }

    private fun clearPref(){
        val pref : SharedPreferences = requireContext().getSharedPreferences("LearningProgress",0)
        val editor = pref.edit()
        for (i in arrayListOf("Learning", "F2CNumber", "C2FNumber", "F2CCorrect", "C2FCorrect")){
            editor.putInt(i, 0).apply()
        }
        snack(Prompt.SUCCESS, "Clear Succeeded!")
    }

    private fun check_for_update() {
        snack(Prompt.SUCCESS, "You have the latest version!")
    }

    private fun terms_and_conditions(){
        val fragmentManger : FragmentManager = requireFragmentManager()
        val transaction = fragmentManger.beginTransaction().apply {
            replace(this@SettingFragment.id, SettingTermFragment())
            addToBackStack(null)
        }
        transaction.commit()
    }

    private fun snack(prompt: Prompt, text: CharSequence){
        val duration = TSnackbar.LENGTH_SHORT
        TSnackbar.make(requireView(), text, duration).setPromptThemBackground(prompt).show();
    }

}
