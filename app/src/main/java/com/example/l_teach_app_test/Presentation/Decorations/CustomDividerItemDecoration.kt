package com.example.l_teach_app_test.Presentation.Decorations

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomDividerItemDecoration(private val divider: Drawable, private val dividerGap: Int) : RecyclerView.ItemDecoration() {

    private val dividerHeight = divider.intrinsicHeight 
    
    private val fullDividerSpace = dividerGap + dividerHeight + dividerGap

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val position = parent.getChildAdapterPosition(child)
            
            if (position == 0) {
                val top = child.top - params.topMargin - fullDividerSpace
                val bottom = top + dividerHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(c)
            }
            
            if (position < state.itemCount - 1) {
                val top = child.bottom + params.bottomMargin + dividerGap 
                val bottom = top + dividerHeight 
                divider.setBounds(left, top, right, bottom)
                divider.draw(c)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        
        if (position == 0) {
            outRect.top = fullDividerSpace
        } else {
            outRect.top = 0 
        }
        
        if (position < state.itemCount - 1) {
            outRect.bottom = fullDividerSpace
        } else {
            outRect.bottom = 0 
        }
    }
}