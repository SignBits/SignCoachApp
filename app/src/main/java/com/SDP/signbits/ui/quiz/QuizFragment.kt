package com.SDP.signbits.ui.quiz

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.SDP.signbits.R
import com.SDP.signbits.ui.quizCharToFinger.QuizCharToFinger
import com.SDP.signbits.ui.quizFingerToChar.QuizFingerToChar


class QuizFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizViewModel =
            ViewModelProviders.of(this).get(QuizViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_quiz, container, false)

        // These are the buttons in the quiz fragment view
        val buttonC2F : CardView = root.findViewById(R.id.cardC2F)
        val buttonF2C : CardView = root.findViewById(R.id.cardF2C)

        val pref : SharedPreferences = requireContext().getSharedPreferences("LearningProgress",0)
        val pbC2F : ProgressBar = root.findViewById(R.id.progressBar3)
        pbC2F.max = pref.getInt("C2FNumber", 1)
        if (pbC2F.max == 0) pbC2F.max =1
        pbC2F.progress = pref.getInt("C2FCorrect", -1)


        val textView : TextView = root.findViewById(R.id.textView12)
        textView.text = "You have done ${pbC2F.progress} Character to Fingerspell " +
                "quizzes with accuracy of ${pbC2F.progress * 100 /pbC2F.max}"

        val tv_1 : TextView = root.findViewById(R.id.tv_1)
        tv_1.text = "${pbC2F.progress * 100 /pbC2F.max}%"

        val pbF2C : ProgressBar = root.findViewById(R.id.progressBar4)
        pbF2C.max = pref.getInt("F2CNumber", 1)
        if (pbF2C.max == 0) pbF2C.max = 1
        pbF2C.progress = pref.getInt("F2CCorrect", -1)

        val textView2 : TextView = root.findViewById(R.id.textView14)
        textView2.text = "You have done ${pbF2C.progress} Fingerspell to Character quizzes with " +
                "accuracy of ${pbF2C.progress*100/pbF2C.max}%"

        val tv_2 : TextView = root.findViewById(R.id.tv_2)
        tv_2.text = "${pbF2C.progress * 100 /pbF2C.max}%"

        buttonC2F.setOnClickListener{
            convertToAnotherFragment(QuizCharToFinger.newInstance())
        }

        buttonF2C.setOnClickListener{
            convertToAnotherFragment(QuizFingerToChar.newInstance())
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        val pref : SharedPreferences = requireContext().getSharedPreferences("LearningProgress",0)
        val pbC2F : ProgressBar = requireActivity().findViewById(R.id.progressBar3)
        pbC2F.max = pref.getInt("C2FNumber", 1)
        if (pbC2F.max == 0) pbC2F.max =1
        pbC2F.progress = pref.getInt("C2FCorrect", -1)

        val textView : TextView = requireActivity().findViewById(R.id.textView12)
        textView.text = "You have done ${pbC2F.progress} Character to Fingerspell " +
                "quizzes with accuracy of ${pbC2F.progress * 100 /pbC2F.max}"

        val tv_1 : TextView = requireActivity().findViewById(R.id.tv_1)
        tv_1.text = "${pbC2F.progress * 100 /pbC2F.max}%"

        val pbF2C : ProgressBar = requireActivity().findViewById(R.id.progressBar4)
        pbF2C.max = pref.getInt("F2CNumber", 1)
        if (pbF2C.max == 0) pbF2C.max = 1
        pbF2C.progress = pref.getInt("F2CCorrect", -1)

        val textView2 : TextView = requireActivity().findViewById(R.id.textView14)
        textView2.text = "You have done ${pbF2C.progress} Fingerspell to Character quizzes with " +
                "accuracy of ${pbF2C.progress*100/pbF2C.max}%"

        val tv_2 : TextView = requireActivity().findViewById(R.id.tv_2)
        tv_2.text = "${pbF2C.progress * 100 /pbF2C.max}%"
    }

    /**
     * This is the method to convert to another fragment.
     *
     * @param fragment This is the fragment of the destination. Need to be a fragment object instead
     *        of Fragment Id etc
     */
    private fun convertToAnotherFragment(fragment: Fragment){
        val fragmentManger : FragmentManager = requireFragmentManager()
        val transaction = fragmentManger.beginTransaction().apply {
            replace(this@QuizFragment.id, fragment)
            addToBackStack(null)
        }
        transaction.commit()

    }
}