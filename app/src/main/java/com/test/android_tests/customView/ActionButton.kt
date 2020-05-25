package com.test.android_tests.customView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.toBitmap
import com.test.android_tests.R

class ActionButton(context: Context, attrs: AttributeSet) :
    CardView(context, attrs) {

    private val paint = Paint()

    private val buttonRadius = getContext().resources.getDimension(R.dimen.action_button_radius)

    private val checkedImage: Drawable?
    private val image: Drawable?
    private val checkable: Boolean
    private val shape: Int
    private val background: Int
    private val elev: Float

    private var checked = false

    init {

        val abAttrs =
            getContext().obtainStyledAttributes(attrs, R.styleable.ActionButton)

        checkedImage = abAttrs.getDrawable(R.styleable.ActionButton_ab_checkedImage)
        image = abAttrs.getDrawable(R.styleable.ActionButton_ab_image)
        checkable = abAttrs.getBoolean(R.styleable.ActionButton_ab_checkable, false)
        shape = abAttrs.getInt(R.styleable.ActionButton_ab_shape, 0)
        background =
            abAttrs.getColor(R.styleable.ActionButton_ab_backgroundColor, Color.TRANSPARENT)
        elev = abAttrs.getDimension(
            R.styleable.ActionButton_ab_elevation,
            getContext().resources.getDimension(R.dimen.action_button_elevation)
        )

        abAttrs.recycle()

        val set = intArrayOf(android.R.attr.selectableItemBackground)
        val superAttrs = getContext().obtainStyledAttributes(attrs, set)
        val selectableItemBackground = superAttrs.getDrawable(0)
        superAttrs.recycle()

        cardElevation = elev
        radius = if (shape == 0) buttonRadius
        else buttonRadius * 0.3F
        isClickable = true
        isFocusable = true
        foreground = selectableItemBackground
        setCardBackgroundColor(background)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size = (buttonRadius * 2).toInt()
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {
            val bitmap = getBitmap()

            val left = width / 2F - bitmap.width / 2F
            val top = height / 2F - bitmap.height / 2F

            canvas.drawBitmap(bitmap, left, top, paint)
        }
    }

    private fun getBitmap(): Bitmap {
        if (image == null) throw NullPointerException("Attribute ab_uncheckedImage is not set")

        return if (checkable) {
            if (checkedImage == null) throw NullPointerException("Attribute ab_checkedImage is not set")

            if (checked) checkedImage.toBitmap() else image.toBitmap()
        } else
            image.toBitmap()
    }

    override fun performClick(): Boolean {
        checked = !checked
        invalidate()
        return super.performClick()
    }
}