package com.fitlogga.app.adapters.collapsible;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a view holder that can be expanded.
 * When extending this class, the view "iv_expand_collapse" MUST exist!
 */
public abstract class CollapsibleViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private ImageView arrow;

    public CollapsibleViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.arrow = view.findViewById(R.id.iv_expand_collapse);
    }

    final void handleCollapseContent(boolean isExpanded) {
        view.setActivated(isExpanded);

        for (View collapsibleView : getCollapsibleViews()) {
            collapsibleView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }

        if (isExpanded) {
            Drawable upArrow = view.getResources().getDrawable(R.drawable.ic_arrow_up);
            arrow.setImageDrawable(upArrow);
        }
        else {
            Drawable downArrow = view.getResources().getDrawable(R.drawable.ic_arrow_down);
            arrow.setImageDrawable(downArrow);
        }

    }

    private List<View> getCollapsibleViews() {
        List<View> viewList = new ArrayList<>();
        for (int viewResourceId : getCollapsibleViewResourceIds()) {
            View collapsibleView = view.findViewById(viewResourceId);

            if (collapsibleView == null) {
                String resourceIdString = view.getResources().getResourceName(viewResourceId);
                String message = "Received a null view ID: " + resourceIdString;
                throw new NullPointerException(message);
            }

            viewList.add(collapsibleView);
        }
        return viewList;
    }

    /**
     * Gets the views that can be collapsed/expanded.
     */
    protected abstract int[] getCollapsibleViewResourceIds();

    final void setExpandCollapseClickListener(View.OnClickListener listener) {
        view.findViewById(R.id.iv_expand_collapse).setOnClickListener(listener);
    }

    public final void simulateCollapseButtonClick() {
        arrow.callOnClick();
    }


}
