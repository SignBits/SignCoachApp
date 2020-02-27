package com.SDP.signbits

import android.content.Context
import android.content.res.AssetManager
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.ProgressBar
import com.SDP.signbits.R

internal class TextProgressBar : ProgressBar {
    var text: String? = null
    val myPaint: Paint = Paint()

    constructor(context: Context?) : super(context) {
        initText()
    }

    constructor(
        context: Context?,
        attr: AttributeSet?
    ) : super(context, attr) {
        initText()
    }

    constructor(
        context: Context?,
        attr: AttributeSet?,
        defStyle: Int
    ) : super(context, attr, defStyle) {
        initText()
    }

    @Synchronized
    override fun setProgress(progress: Int) {
        setText(progress)
        super.setProgress(progress)
    }

    @Synchronized
    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rect = Rect()
        myPaint.getTextBounds(text, 0, text!!.length, rect)
        val x = width / 2 - rect.centerX()
        val y = height / 2 - rect.centerY()
        canvas.drawText(text!!, x.toFloat(), y.toFloat(), myPaint)
    }

    private fun initText() {
        myPaint.textAlign = Paint.Align.LEFT
        myPaint.color = Color.WHITE
        if (id == R.id.progressBar || id == R.id.progressBar2) {
            myPaint.textScaleX *= 7
            val t = myPaint.textSize * 0.8
            myPaint.textSize = t.toFloat()
        }
        if (id == R.id.progressBar3 || id == R.id.progressBar4) myPaint.textScaleX = myPaint
            .textScaleX *4 /5
    }

    private fun setText() {
        setText(this.progress)
    }

    private fun setText(progress: Int) {
        val i = progress * 100 / this.max
        if (id == R.id.progressBar)
            text = "Learning Progress: $i%"
        else if (id == R.id.progressBar2)
            text = "Quiz Accuracy: $i%"
        else if (id == R.id.progressBar3 || id == R.id.progressBar4)
            text = "$i%"
    }
}