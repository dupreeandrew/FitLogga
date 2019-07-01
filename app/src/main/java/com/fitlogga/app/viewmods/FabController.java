package com.fitlogga.app.viewmods;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FabController {

    private FloatingActionButton fab;

    public FabController(FloatingActionButton fab) {
        this.fab = fab;
    }

    public void setEnabled(boolean enable) {
        ViewEnabler.setEnabled(fab, enable);
    }
}
