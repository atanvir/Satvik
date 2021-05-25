package com.biteMe.utils.helpers

import com.google.android.material.appbar.AppBarLayout
import java.lang.Math.abs

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    enum class State { EXPANDED, COLLAPSED, IDLE }

    private var mCurrentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (abs(i) >= appBarLayout.totalScrollRange) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: State?)

}