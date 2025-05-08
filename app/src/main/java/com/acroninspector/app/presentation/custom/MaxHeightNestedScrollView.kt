package com.acroninspector.app.presentation.custom

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView
import com.acroninspector.app.R
import com.acroninspector.app.common.extension.dpToPx

class MaxHeightNestedScrollView : NestedScrollView {

    private var maxHeightPx = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightNestedScrollView)

            maxHeightPx =
                typedArray.getDimension(
                    R.styleable.MaxHeightNestedScrollView_maxHeight,
                    context.dpToPx(DEFAULT_MAX_HEIGHT_DP)
                ).toInt()

            typedArray.recycle()
        } else {
            maxHeightPx = context.dpToPx(DEFAULT_MAX_HEIGHT_DP).toInt()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMSpec = MeasureSpec.makeMeasureSpec(maxHeightPx, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, heightMSpec)
    }

    companion object {
        private const val DEFAULT_MAX_HEIGHT_DP = 180F
    }
}
