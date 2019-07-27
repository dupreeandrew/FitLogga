package com.fitlogga.app.adapters.plans;

import android.content.Intent;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.R;
import com.fitlogga.app.activities.PlanLogActivity;
import com.fitlogga.app.models.plan.log.SQLLogReader;
import com.fitlogga.app.viewmods.ViewEnabler;

public class PlanSummaryViewHolder extends RecyclerView.ViewHolder {

    private View view;

    PlanSummaryViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    void setAsActive(boolean b) {
        ImageView checkmark = view.findViewById(R.id.iv_check);
        checkmark.setVisibility(b ? View.VISIBLE : View.INVISIBLE);

        Button activateButton = view.findViewById(R.id.btn_activate);
        ViewEnabler.setEnabled(activateButton, !b);

        String activated = view.getResources().getString(R.string.vh_plan_activated);
        String activate = view.getResources().getString(R.string.vh_plan_activate);
        activateButton.setText(b ? activated : activate);

    }

    void setPlanName(String name) {
        TextView planNameView = view.findViewById(R.id.tv_plan_name);
        planNameView.setText(name);

        TextView planNameFirstCharView = view.findViewById(R.id.tv_plan_name_first_letter);
        String firstChar = String.valueOf(name.charAt(0));
        planNameFirstCharView.setText(firstChar);

        initGraphLogButton(name);
    }

    private void initGraphLogButton(String name) {
        ImageView graphLogView = view.findViewById(R.id.iv_log);
        graphLogView.setOnClickListener(view -> {
            if (atLeastOneExerciseWasLogged(name)) {
                Intent intent = new Intent(view.getContext(), PlanLogActivity.class);
                intent.putExtra(PlanLogActivity.PLAN_NAME_KEY, name);
                view.getContext().startActivity(intent);
            }
            else {
                String msg = "No exercises were logged for this plan";
                Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean atLeastOneExerciseWasLogged(String name) {
        SQLLogReader reader = new SQLLogReader(name);
        return reader.getTotalExercisesLogged() != 0;
    }


    void setDescription(String description) {
        ImageView infoImageView = view.findViewById(R.id.iv_info);
        infoImageView.setOnClickListener(infoView -> {
            Toast.makeText(view.getContext(), description, Toast.LENGTH_LONG).show();
        });
    }

    void setLastUsed(long lastUsedTimestamp) {

        String lastUsedText = "Last used\n" +
                DateUtils.getRelativeTimeSpanString(lastUsedTimestamp);

        TextView collapseContentView = view.findViewById(R.id.tv_last_used);
        collapseContentView.setText(lastUsedText);
    }

    void setActivateButtonClickListener(View.OnClickListener listener) {
        Button activateButton = view.findViewById(R.id.btn_activate);
        activateButton.setOnClickListener(listener);
    }

    void setDeleteButtonClickListener(View.OnClickListener listener) {
        ImageView deleteButton = view.findViewById(R.id.iv_trash);
        deleteButton.setOnClickListener(listener);
    }

    void setEditButtonClickListener(View.OnClickListener listener) {
        ImageView editButton = view.findViewById(R.id.iv_edit);
        editButton.setOnClickListener(listener);
    }



}
