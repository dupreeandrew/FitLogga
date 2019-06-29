package com.fitlogga.app.adapters.collapsible;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.R;

/**
 * This class represents a view holder that can be expanded.
 * When extending this class, the view "iv_expand_collapse" MUST exist!
 */
public abstract class CollapsibleViewHolder extends RecyclerView.ViewHolder {

    private View view;

    public CollapsibleViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    final void handleCollapseContent(boolean isExpanded) {
        view.setActivated(isExpanded);

        View[] collapsibleViews = getCollapsibleViews();
        for (View collapsibleView : collapsibleViews) {
            collapsibleView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }

        ImageView arrow = view.findViewById(R.id.iv_expand_collapse);

        if (isExpanded) {
            Drawable upArrow = view.getResources().getDrawable(R.drawable.ic_arrow_up);
            arrow.setImageDrawable(upArrow);
        }
        else {
            Drawable downArrow = view.getResources().getDrawable(R.drawable.ic_arrow_down);
            arrow.setImageDrawable(downArrow);
        }

    }

    /**
     * Gets the views that can be collapsed/expanded.
     */
    protected abstract View[] getCollapsibleViews();

    final void setExpandCollapseClickListener(View.OnClickListener listener) {
        view.findViewById(R.id.iv_expand_collapse).setOnClickListener(listener);
    }


}
