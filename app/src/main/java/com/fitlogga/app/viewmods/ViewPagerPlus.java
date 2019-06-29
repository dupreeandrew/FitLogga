package com.fitlogga.app.viewmods;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

/**
 * Enhanced ViewPager View, allowing for extra functionality:
 *
 * - #setPagingEnabled(boolean enabled)
 * - #setTabClickEnabled(TabLayout tabLayout, boolean enabled)
 */
public class ViewPagerPlus extends ViewPager {

    public static class Controller {

        private ViewPagerPlus viewPagerPlus;
        private TabLayout tabLayout;

        private Controller(ViewPagerPlus viewPagerPlus, TabLayout tabLayout) {
            this.viewPagerPlus = viewPagerPlus;
            this.tabLayout = tabLayout;
        }

        public void setPagingEnabled(boolean enabled) {
            viewPagerPlus.setPagingEnabled(enabled);
            viewPagerPlus.setTabClickEnabled(tabLayout, enabled);
        }

    }

    private boolean pagingEnabled;

    public ViewPagerPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.pagingEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.pagingEnabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.pagingEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public Controller getController(TabLayout tabLayout) {
        return new Controller(this, tabLayout);
    }

    private void setPagingEnabled(boolean enabled) {
        this.pagingEnabled = enabled;
    }

    private void setTabClickEnabled(TabLayout tabLayout, boolean enabled) {

        LinearLayout tabStrip = (LinearLayout)tabLayout.getChildAt(0);
        tabStrip.setEnabled(enabled);

        final float ENABLED_OPACITY = 1.00f;
        final float DISABLED_OPACITY = .5f;

        tabStrip.setAlpha(enabled ? ENABLED_OPACITY : DISABLED_OPACITY);

        final int LAST_TAB_INDEX = 8;
        for (int i = 0; i < LAST_TAB_INDEX; i++) {
            tabStrip.getChildAt(i).setClickable(enabled);
        }

    }
}