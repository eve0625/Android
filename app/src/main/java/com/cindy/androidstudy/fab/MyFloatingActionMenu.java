package com.cindy.androidstudy.fab;

import android.content.Context;
import android.util.AttributeSet;

import com.github.clans.fab.FloatingActionMenu;

public class MyFloatingActionMenu extends FloatingActionMenu {

    public MyFloatingActionMenu(Context context) {
        this(context, null, 0);
    }

    public MyFloatingActionMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
