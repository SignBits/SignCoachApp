package com.SDP.signbits

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import android.util.AttributeSet
import android.util.Log
import android.widget.ProgressBar

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
        Log.d("DrawBox", "x and y are $x,$y")
        canvas.drawText(text!!, x.toFloat(), y.toFloat(), myPaint)
    }

    private fun initText() {
        myPaint.textAlign = Paint.Align.LEFT
        myPaint.color = Color.WHITE
        myPaint.textScaleX *= 7
    }

    private fun setText() {
        setText(this.progress)
    }

    private fun setText(progress: Int) {
        val i = progress * 100 / this.max
        if (id == R.id.progressBar)
            text = "Learning Progress: $i%"
        else
            text = "Quiz Accuracy: $i%"
    }
}