package com.fitlogga.app.adapters.drag;

public interface ItemTouchAdapter {
    void onMove(int sourcePos, int targetPos);
    void onDragFinished(int sourcePos, int targetPos);
    void onSwipe();
}
