package com.SDP.signbits.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.SDP.signbits.MainActivity
import com.SDP.signbits.R
import com.SDP.signbits.ui.quizCharToFinger.QuizCharToFinger
import com.SDP.signbits.ui.quizFingerToChar.QuizFingerToChar
import com.SDP.signbits.TextProgressBar


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

        val pbC2F : TextProgressBar = root.findViewById(R.id.progressBar3)
        pbC2F.max = MainActivity.alphabet.count()
        pbC2F.progress = 15


        val textView : TextView = root.findViewById(R.id.textView12)
        textView.text = "You have done ${pbC2F.progress} Character to Fingerspell " +
                "quizzes with accuracy of ${pbC2F.progress * 100 /pbC2F.max}"

        val pbF2C : TextProgressBar = root.findViewById(R.id.progressBar4)
        pbF2C.max = 100
        pbF2C.progress = 15

        val textView2 : TextView = root.findViewById(R.id.textView14)
        textView2.text = "You have done ${pbF2C.progress} Fingerspell to Character quizzes with " +
                "accuracy of ${pbF2C.progress*100/pbF2C.max}%"

        buttonC2F.setOnClickListener{
            convertToAnotherFragment(QuizCharToFinger.newInstance())
        }

        buttonF2C.setOnClickListener{
            convertToAnotherFragment(QuizFingerToChar.newInstance())
        }
        return root
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