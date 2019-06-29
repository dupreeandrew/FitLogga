package com.fitlogga.app.adapters.collapsible;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.Event;

/**
 * This class represents a recycler adapter that can be expanded/collapsed.
 * Only setup for the CollapsibleViewHolder is required.
 */
public abstract class CollapsibleRecyclerAdapter<VH extends CollapsibleViewHolder> extends RecyclerView.Adapter<VH> {



    private int previousExpandedPosition = -1;
    private int expandedPosition = -1;
    private VH lastCollapsedViewHolder;
    private VH expandedViewHolder;

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        addCollapseFunctionality(holder, position);
        onPostConfigBindViewHolder(holder, position);
    }

    protected abstract void onPostConfigBindViewHolder(VH viewHolder, int position);

    private void addCollapseFunctionality(VH viewHolder, int position) {
        boolean justExpanded = (position == expandedPosition);
        viewHolder.handleCollapseContent(justExpanded);

        if (justExpanded) {
            previousExpandedPosition = position;
            this.lastCollapsedViewHolder = viewHolder;
        }
        else {


            // onViewHolderCollapsed(viewHolder);
        }

        viewHolder.setExpandCollapseClickListener(view -> {

            // Event event = new Event(false);
            //onViewHolderCollapsed(event);

            Event event = new Event(false);
            if (justExpanded) {
                Log.d("#12!", "Collapsed");
                onViewHolderCollapsed(event, viewHolder, view.getContext());
            }
            else { // so another one opened up, i need integer of collapsed one

                if (expandedPosition == previousExpandedPosition) {
                    if (lastCollapsedViewHolder != null) {
                        Log.d("#12!", "Collapsed");
                        onViewHolderCollapsed(event, lastCollapsedViewHolder, view.getContext());
                        expandedViewHolder = null;
                    }
                }


                Log.d("#12!", "Expanded @" + viewHolder.getAdapterPosition());
                expandedViewHolder = viewHolder;
                onViewHolderExpanded(viewHolder);



            }

            Log.d("heh", event.toString());
            if (!event.isCancelled()) {

                expandedPosition = justExpanded ? -1 : position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);

            }

        });

    }

    protected abstract void onViewHolderCollapsed(Event event, VH viewHolder, Context context);

    protected abstract void onViewHolderExpanded(VH expandedViewHolder);

    protected final void considerDataSetMoved(int newPos, int adapterSize) {
        notifyItemRangeChanged(newPos + 1, adapterSize - 1);
    }

    protected final void considerDataSetSwapped(int oldPos, int newPos) {

        if (oldPos == expandedPosition) {
            expandedPosition = newPos;
        }
        else if (newPos == expandedPosition) {
            expandedPosition = oldPos;
        }

        notifyItemChanged(oldPos);
        notifyItemChanged(newPos);

    }

    public final void expandViewHolder(int position) {
        collapsePresentExpandedViewHolder();
        expandedPosition = position;
        notifyItemChanged(expandedPosition);
        notifyItemChanged(previousExpandedPosition);

        // todo:: make sure that it is returning the correct expanded view holder.
        onViewHolderExpanded(expandedViewHolder);
    }

    protected final void collapsePresentExpandedViewHolder() {
        if (expandedPosition == -1) {
            return;
        }

        expandedPosition = -1;
        notifyItemChanged(expandedPosition);
        notifyItemChanged(previousExpandedPosition);

    }

    @Nullable
    protected VH getExpandedViewHolder() {
        return this.expandedViewHolder;
    }
}
