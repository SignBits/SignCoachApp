package com.SDP.signbits.ui.setting

import android.content.res.AssetManager
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
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

        setting5.setOnClickListener{
            val menu = PopupMenu(context, it)
            menu.inflate(R.menu.popup_menu)
            menu.show()
        }

    }

    private fun check_for_update(view: View){
        snack(Prompt.SUCCESS, "You have the latest version!")
    }

    private fun terms_and_conditions(view: View){
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
