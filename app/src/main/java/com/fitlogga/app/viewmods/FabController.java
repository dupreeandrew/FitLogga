package com.fitlogga.app.viewmods;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FabController {

    private FloatingActionButton fab;

    public FabController(FloatingActionButton fab) {
        this.fab = fab;
    }

    public void setEnabled(boolean enable) {
        fab.setEnabled(enable);

        final float ENABLED_OPACITY = 1.00f;
        final float DISABLED_OPACITY = .75f;
        fab.setAlpha(enable ? ENABLED_OPACITY : DISABLED_OPACITY);
    }
}
