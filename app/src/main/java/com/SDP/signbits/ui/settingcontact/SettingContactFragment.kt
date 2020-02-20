package com.SDP.signbits.ui.settingcontact
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.SDP.signbits.R
import androidx.lifecycle.Observer


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
        val textView: TextView = root.findViewById(R.id.text_settingcontact)
        settingContactViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }


}