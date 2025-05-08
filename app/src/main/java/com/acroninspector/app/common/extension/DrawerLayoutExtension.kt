package com.acroninspector.app.common.extension

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout

inline fun DrawerLayout.addAcronDrawerListener(crossinline onDrawerClosed: (DrawerLayout.DrawerListener) -> Unit) {
    this.addDrawerListener(object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit
        override fun onDrawerStateChanged(newState: Int) = Unit
        override fun onDrawerOpened(drawerView: View) = Unit
        override fun onDrawerClosed(drawerView: View) = onDrawerClosed(this)
    })
}