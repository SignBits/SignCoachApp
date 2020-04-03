package com.SDP.signbits.ui.settingTermsAndConditions
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.SDP.signbits.R
import com.SDP.signbits.ui.setting.SettingFragment
import kotlinx.android.synthetic.main.fragment_setting_term.*


class SettingTermFragment : Fragment() {

    private lateinit var settingstermviewModel: SettingTermViewModel

    companion object{
        fun newInstance() = SettingTermFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        settingstermviewModel =
            ViewModelProviders.of(this).get(SettingTermViewModel::class.java)

        return inflater.inflate(R.layout.fragment_setting_term, container, false)
    }

    override fun onStart() {
        super.onStart()
        backButtonTerm.setOnClickListener{
            val fragmentManager : FragmentManager = requireFragmentManager()
            fragmentManager.beginTransaction().apply {
                replace(this@SettingTermFragment.id, SettingFragment.newInstance())
            }.commit()
        }
    }

}