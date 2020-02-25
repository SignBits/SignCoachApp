package com.SDP.signbits

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class MyLinearLayout : LinearLayout {
    constructor(context: Context) : super(context)

    constructor(
        context: Context?,
        attr: AttributeSet?
    ) : super(context, attr)

    constructor(
        context: Context?,
        attr: AttributeSet?,
        defStyle: Int
    ) : super(context, attr, defStyle)

}