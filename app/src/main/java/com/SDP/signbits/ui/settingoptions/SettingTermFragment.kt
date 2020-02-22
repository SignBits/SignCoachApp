package com.SDP.signbits.ui.settingoptions
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


class SettingTermFragment : Fragment() {

    private lateinit var settingstermviewModel: SettingTermViewModel
    companion object {
        fun newInstance() = SettingTermFragment()
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        settingstermviewModel =
            ViewModelProviders.of(this).get(SettingTermViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_setting_term, container, false)
        val button : Button = root.findViewById(R.id.Button)
        button.setOnClickListener{
            convertToAnotherFragment(SettingFragment.newInstance())
        }

        val textView: TextView = root.findViewById(R.id.text_settingterm)
        settingstermviewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
    private fun convertToAnotherFragment(fragment: Fragment){
        val fragmentManger : FragmentManager = requireFragmentManager()
        fragmentManger.beginTransaction().replace(this.id, fragment).commit()
    }

}