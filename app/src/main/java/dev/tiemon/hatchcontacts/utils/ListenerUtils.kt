package dev.tiemon.hatchcontacts.utils

import android.view.View

object ListenerUtils {

    @JvmStatic
    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener =
            SafeClickListener(500) {
                onSafeClick(it)
            }
        setOnClickListener(safeClickListener)
    }

    fun List<View?>.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        forEach { it?.setSafeOnClickListener(onSafeClick) }
    }
}