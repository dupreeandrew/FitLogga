package com.fitlogga.app.adapters.plans;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.collapsible.CollapsibleViewHolder;

public class PlanSummaryViewHolder extends CollapsibleViewHolder {

    private View view;

    PlanSummaryViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    void setAsActive(boolean b) {
        ImageView checkmark = view.findViewById(R.id.iv_check);
        checkmark.setVisibility(b ? View.VISIBLE : View.INVISIBLE);

        Button activateButton = view.findViewById(R.id.btn_activate);
        activateButton.setEnabled(!b);
        activateButton.setText(b ? "Activated" : "Activate");

        final int DISABLED_OPACITY = 120;
        final int ENABLED_OPACITY = 255;
        activateButton.getBackground().setAlpha(b ? DISABLED_OPACITY : ENABLED_OPACITY);

    }

    void setPlanName(String name) {
        TextView planNameView = view.findViewById(R.id.tv_plan_name);
        planNameView.setText(name);
    }

    void setCollapseContent(long lastUsedTimestamp, String description) {

        Log.d("blaaa1", description + " : " + lastUsedTimestamp);

        CharSequence lastUsedText = "Last used " +
                DateUtils.getRelativeTimeSpanString(lastUsedTimestamp);
        String collapseContent = lastUsedText + "\n" + description;

        TextView collapseContentView = view.findViewById(R.id.tv_collapse_content);
        collapseContentView.setText(collapseContent);
    }

    @Override
    protected View[] getCollapsibleViews() {
        return new View[]{
                view.findViewById(R.id.btn_activate),
                view.findViewById(R.id.btn_delete),
                view.findViewById(R.id.divider),
                view.findViewById(R.id.tv_collapse_content)
        };
    }

    void setActivateButtonClickListener(View.OnClickListener listener) {
        Button activateButton = view.findViewById(R.id.btn_activate);
        activateButton.setOnClickListener(listener);
    }

    void setDeleteButtonClickListener(View.OnClickListener listener) {
        Button deleteButton = view.findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(listener);
    }

    void setEditButtonClickListener(View.OnClickListener listener) {
        ImageView editButton = view.findViewById(R.id.iv_edit);
        editButton.setOnClickListener(listener);
    }



}
