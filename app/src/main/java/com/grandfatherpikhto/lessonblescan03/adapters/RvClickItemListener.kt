package com.grandfatherpikhto.lessonblescan03.adapters

import android.view.View

interface RvClickItemListener <T> {
    fun onClickItem(model: T, view: View) {}
    fun onLongClickItem(model: T, view: View) {}
}