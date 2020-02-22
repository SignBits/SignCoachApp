package com.SDP.signbits.ui.settingcontact
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.SDP.signbits.R
import androidx.lifecycle.Observer
import com.SDP.signbits.ui.setting.SettingFragment
import com.SDP.signbits.ui.settingoptions.SettingTermFragment


class SettingContactFragment : Fragment() {

    private lateinit var settingContactViewModel: SettingContactViewModel
    companion object {
        fun newInstance() = SettingContactFragment()
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        settingContactViewModel =
            ViewModelProviders.of(this).get(SettingContactViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_setting_contact, container, false)
        val button : Button = root.findViewById(R.id.Button)
        button.setOnClickListener{
            convertToAnotherFragment(SettingFragment.newInstance())
        }

        val textView: TextView = root.findViewById(R.id.text_settingcontact)
        settingContactViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
//    private fun backToQuiz() {
//        val fragmentManager : FragmentManager = requireFragmentManager()
//        fragmentManager.beginTransaction().remove(this).commit()
//        fragmentManager.popBackStack()
//    }
    private fun convertToAnotherFragment(fragment: Fragment){
        val fragmentManger : FragmentManager = requireFragmentManager()
        fragmentManger.beginTransaction().replace(this.id, fragment).commit()
    }

}