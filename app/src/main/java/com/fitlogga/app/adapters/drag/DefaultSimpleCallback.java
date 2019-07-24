package com.fitlogga.app.adapters.drag;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DefaultSimpleCallback extends ItemTouchHelper.SimpleCallback {

    private ItemTouchAdapter adapter;
    private int sourcePos = -1;
    private int targetPos = -1;

    public DefaultSimpleCallback(ItemTouchAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        new Exception().printStackTrace();
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, @NonNull RecyclerView.ViewHolder target) {
        new Exception().printStackTrace();


        if (sourcePos == -1) {
            sourcePos = source.getAdapterPosition();
        }

        this.targetPos = target.getAdapterPosition();

        adapter.onMove(source.getAdapterPosition(), targetPos);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // do nothing
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (sourcePos != -1 && targetPos != -1) {
            adapter.onDragFinished(sourcePos, targetPos);
        }



        Log.d("testt#", sourcePos + " -> " + targetPos);
    }

}
