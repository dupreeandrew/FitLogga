package com.fitlogga.app.adapters.plans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.fitlogga.app.Event;
import com.fitlogga.app.R;
import com.fitlogga.app.adapters.collapsible.CollapsibleRecyclerAdapter;
import com.fitlogga.app.models.plan.PlanEditor;
import com.fitlogga.app.models.plan.PlanSummary;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.List;

public class PlanSummaryRecyclerAdapter extends CollapsibleRecyclerAdapter<PlanSummaryViewHolder> {

    private List<PlanSummary> planSummaries;

    public PlanSummaryRecyclerAdapter(List<PlanSummary> planSummaries) {
        this.planSummaries = planSummaries;
    }

    @NonNull
    @Override
    public PlanSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vh_plan, parent, false);
        return new PlanSummaryViewHolder(view);
    }

    @Override
    protected void onPostConfigBindViewHolder(@NonNull PlanSummaryViewHolder holder, int position) {
        PlanSummary planSummary = planSummaries.get(position);
        holder.setPlanName(planSummary.getName());
        holder.setCollapseContent(planSummary.getLastUsed(), planSummary.getDescription());
        holder.setAsActive(position == 0);
        holder.setActivateButtonClickListener(view -> activatePlan(view, planSummary, holder.getAdapterPosition()));
        holder.setDeleteButtonClickListener(view -> promptDeletePlan(view, planSummary, position));
        holder.setEditButtonClickListener(view -> PlanEditor.openGUI(view.getContext(), planSummary.getName()));
    }

    private void activatePlan(View view, PlanSummary planSummary, int position) {

        PlanEditor planEditor = new PlanEditor(view.getContext(), planSummary.getName());
        planEditor.setAsCurrentPlan();

        planSummaries.remove(position);
        notifyItemRemoved(position);

        planSummaries.add(0, planSummary);
        notifyItemInserted(0);


        considerDataSetMoved(0, planSummaries.size());
        expandViewHolder(0);

    }


    private void promptDeletePlan(View view, PlanSummary planSummary, int position) {
        new LovelyStandardDialog(view.getContext())
                .setTopColorRes(R.color.colorWarning)
                .setTitle("Are you sure?")
                .setMessage("Are you sure you want to delete this plan?")
                .setPositiveButton("Delete", posView -> deletePlan(view, planSummary, position))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deletePlan(View view, PlanSummary planSummary, int position) {
        PlanEditor planEditor = new PlanEditor(view.getContext(), planSummary.getName());
        planEditor.delete();

        planSummaries.remove(planSummary);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, planSummaries.size());
    }

    @Override
    public int getItemCount() {
        return planSummaries.size();
    }

    @Override
    protected void onViewHolderCollapsed(Event event, PlanSummaryViewHolder viewHolder, Context context) {

    }

    @Override
    protected void onViewHolderExpanded(int adapterPos) {

    }

}
