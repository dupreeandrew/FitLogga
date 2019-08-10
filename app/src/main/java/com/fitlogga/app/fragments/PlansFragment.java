package com.fitlogga.app.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.R;
import com.fitlogga.app.activities.PlanCreatorActivity;
import com.fitlogga.app.adapters.plans.ItemOffsetDecoration;
import com.fitlogga.app.adapters.plans.PlanSummaryRecyclerAdapter;
import com.fitlogga.app.models.FreeAppSettings;
import com.fitlogga.app.models.PremiumApp;
import com.fitlogga.app.models.plan.PlanCreator;
import com.fitlogga.app.models.plan.PlanExchanger;
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.models.plan.PlanSummary;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.List;


public class PlansFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView(view);
        initFabAddPost(view);

        PlanCreator.setPlanCreationListener(() -> initRecyclerView(view));

    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_plans);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(0));

        List<PlanSummary> planSummaries = PlanReader.getPlanSummaries();
        recyclerView.setAdapter(new PlanSummaryRecyclerAdapter(planSummaries));

    }

    private void initFabAddPost(View view) {
        FloatingActionButton fab = view.findViewById(R.id.fab_add_plan);
        fab.setOnClickListener(fabView -> tryOpenPlanCreatorDialog(getActivity()));
    }

    static void tryOpenPlanCreatorDialog(Activity activity) {
        int numExistingPlans = PlanReader.getNumberOfPlans();
        if (numExistingPlans > FreeAppSettings.MAX_PLANS && !PremiumApp.isEnabled()) {
            String message = "You can only have 3 fitness plans at a time. " +
                    "Upgrade your fitness experience today for unlimited!";
            PremiumApp.popupPremiumAppDialog(activity, message);
        }


        String[] choices = activity.getResources().getStringArray(R.array.plan_creation_options);
        final int CREATE_NEW_PLAN_INDEX = 0;
        final int ENTER_PLAN_CODE_INDEX = 1;

        new LovelyChoiceDialog(activity)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Plan Creator")
                .setMessage("How do you want to create a plan?")
                .setItems(choices, (position, item) -> {
                    switch (position) {
                        case CREATE_NEW_PLAN_INDEX:
                            launchPlanCreatorActivity(activity);
                            break;
                        case ENTER_PLAN_CODE_INDEX:
                            openEnterPlanCodeDialog(activity);
                            break;

                    }
                })
                .show();
    }

    private static void launchPlanCreatorActivity(Activity activity) {
        Intent intent = new Intent(activity, PlanCreatorActivity.class);
        activity.startActivity(intent);
    }

    private static void openEnterPlanCodeDialog(Activity activity) {
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_enter_plan_code, null);
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .show();

        ProgressBar progressBar = dialog.findViewById(R.id.pb_enter_plan);
        progressBar.setVisibility(View.GONE);

        PlanExchanger.DialogListener dialogListener = new PlanExchanger.DialogListener() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
            }

            @Override
            public void onFail(String localizedErrorMessage) {
                progressBar.setVisibility(View.GONE);
                TextInputLayout planCodeLayout = dialogView.findViewById(R.id.input_plan_code_layout);
                planCodeLayout.setError(localizedErrorMessage);
            }
        };

        Button okButton = dialog.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(buttonView -> {
            progressBar.setVisibility(View.VISIBLE);
            okButton.setEnabled(true);

            EditText planCodeView = dialogView.findViewById(R.id.input_plan_code);
            String planCode = planCodeView.getText().toString();
            PlanExchanger.openImportPlanDialog(activity, planCode, dialogListener);
        });

        Button cancelButton = dialog.findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(buttonView -> dialog.dismiss());

        dialog.setOnDismissListener(dialogInterface -> dialogListener.abortTask());
    }


}
