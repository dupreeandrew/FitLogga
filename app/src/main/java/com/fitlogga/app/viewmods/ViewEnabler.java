package com.fitlogga.app.viewmods;

import android.view.View;

public class ViewEnabler {
    /**
     * Quickly enables/disables a view, and applies opacity changes.
     */
    public static void setEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        final float ENABLED_OPACITY = 1.00f;
        final float DISABLED_OPACITY = .5f;
        view.setAlpha(enabled ? ENABLED_OPACITY : DISABLED_OPACITY);
    }
}
