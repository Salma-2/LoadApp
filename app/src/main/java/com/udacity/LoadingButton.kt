package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var progress = 0.0

    private val paint = Paint().apply {
        textSize = 60.0f
        textAlign = Paint.Align.CENTER
    }
    private val rect = RectF(
        740f,
        50f,
        810f,
        110f
    )
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }

    private var valueAnimator: ValueAnimator

    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()
        invalidate()
    }


    fun downloadCompleted() {
        valueAnimator.cancel()
        buttonState = ButtonState.Completed
        invalidate()
    }

    init {
        isClickable = true

        valueAnimator = AnimatorInflater.loadAnimator(
            context,
            R.animator.loading_animator) as ValueAnimator
        valueAnimator.addUpdateListener(updateListener)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = context.getColor(R.color.colorPrimary)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        if (buttonState == ButtonState.Loading) {
            paint.color = resources.getColor(R.color.colorPrimaryDark)
            canvas.drawRect(0f, 0f, width * (progress / 100).toFloat(), height.toFloat(), paint)
            paint.color = resources.getColor(R.color.colorAccent)
            canvas.drawArc(rect, 0f, (360 * (progress / 100).toFloat()), true, paint)
        }

        val buttonLabel =
            if (buttonState == ButtonState.Loading)
                resources.getString(R.string.button_loading)
            else resources.getString(R.string.button_name)

        paint.color = Color.WHITE
        canvas.drawText(buttonLabel,
            (width / 2).toFloat(),
            (height / 2 + 20).toFloat(),
            paint)
    }

    override fun performClick(): Boolean {
        if (buttonState == ButtonState.Completed) {
            buttonState = ButtonState.Loading
            valueAnimator.start()
        }
        super.performClick()


        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}