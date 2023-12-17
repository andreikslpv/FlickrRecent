package com.andreikslpv.flickrrecent.presentation.ui.recyclers

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class SpaceItemDecoration(
    private val paddingTopInDp: Int = 0,
    private val paddingBottomInDp: Int = 0,
    private val paddingLeftInDp: Int = 0,
    private val paddingRightInDp: Int = 0,
) : RecyclerView.ItemDecoration() {
    private val Int.convertPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = paddingRightInDp.convertPx
        outRect.left = paddingLeftInDp.convertPx
        outRect.bottom = paddingBottomInDp.convertPx
        outRect.top = paddingTopInDp.convertPx
    }
}