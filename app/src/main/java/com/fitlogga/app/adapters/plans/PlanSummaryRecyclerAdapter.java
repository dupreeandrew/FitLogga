package com.fitlogga.app.adapters.plans;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.R;
import com.fitlogga.app.models.PremiumApp;
import com.fitlogga.app.models.plan.PlanEditor;
import com.fitlogga.app.models.plan.PlanExchanger;
import com.fitlogga.app.models.plan.PlanSummary;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.List;

public class PlanSummaryRecyclerAdapter extends RecyclerView.Adapter<PlanSummaryViewHolder> {

    private Activity activity;
    private List<PlanSummary> planSummaries;

    public PlanSummaryRecyclerAdapter(Activity activity, List<PlanSummary> planSummaries) {
        this.activity = activity;
        this.planSummaries = planSummaries;
    }

    @NonNull
    @Override
    public PlanSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vh_plan_revised, parent, false);
        return new PlanSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanSummaryViewHolder holder, int position) {
        PlanSummary planSummary = planSummaries.get(position);
        holder.setPlanName(planSummary.getName());
        holder.setLastUsed(planSummary.getLastUsed());
        holder.setDescription(planSummary.getDescription());
        holder.setAsActive(position == 0);
        holder.setActivateButtonClickListener(view -> activatePlan(view, planSummary, holder.getAdapterPosition()));
        holder.setDeleteButtonClickListener(view -> promptDeletePlan(view, planSummary, position));
        holder.setEditButtonClickListener(view -> PlanEditor.openGUI(view.getContext(), planSummary.getName()));
        holder.setShareButtonClickListener(view -> {
            if (PremiumApp.isEnabled()) {
                openStartShareDialog(planSummary.getName());
            }
            else {
                PremiumApp.popupPremiumAppDialog(activity, "Upgrade to premium to " +
                        "share unlimited fitness plans!");
            }
        });
    }

    private void activatePlan(View view, PlanSummary planSummary, int position) {

        PlanEditor planEditor = new PlanEditor(view.getContext(), planSummary.getName());
        planEditor.setAsCurrentPlan();

        planSummaries.remove(position);
        notifyItemRemoved(position);

        planSummaries.add(0, planSummary);
        notifyItemInserted(0);

        notifyItemChanged(1);


        //considerDataSetMoved(0, planSummaries.size());
        //xpandViewHolder(0);

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

    private void openStartShareDialog(String planName) {
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_plan_saved, null);
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .show();


        ImageView checkMark = dialog.findViewById(R.id.iv_check);
        TextView yourPlanCodeView = dialog.findViewById(R.id.tv_your_plan_code);
        TextView planCodeView = dialog.findViewById(R.id.tv_plan_code);

        checkMark.setVisibility(View.INVISIBLE);
        yourPlanCodeView.setVisibility(View.GONE);
        planCodeView.setVisibility(View.GONE);

        Button cancelButton = dialog.findViewById(R.id.btn_cancel);

        PlanExchanger.ExportDialogListener listener = new PlanExchanger.ExportDialogListener() {
            @Override
            public void onSuccess(String planCode) {
                ProgressBar progressBar = dialog.findViewById(R.id.pb_plan_saving);
                progressBar.setVisibility(View.INVISIBLE);

                Animation fadeIn = AnimationUtils.loadAnimation(checkMark.getContext(), android.R.anim.fade_in);
                checkMark.setAnimation(fadeIn);
                checkMark.setVisibility(View.VISIBLE);

                yourPlanCodeView.setVisibility(View.VISIBLE);
                planCodeView.setVisibility(View.VISIBLE);

                TextView planCodeView = dialog.findViewById(R.id.tv_plan_code);
                planCodeView.setText(planCode);
            }

            @Override
            public void onFail(String localizedErrorMessage) {
                dialog.dismiss();
                Toast.makeText(activity, localizedErrorMessage, Toast.LENGTH_LONG).show();
            }
        };

        PlanExchanger.exportPlan(planName, listener);

        cancelButton.setOnClickListener(buttonView -> dialog.dismiss());

        dialog.setOnDismissListener(dialogInterface -> listener.abortTask());
    }

    @Override
    public int getItemCount() {
        return planSummaries.size();
    }

}
