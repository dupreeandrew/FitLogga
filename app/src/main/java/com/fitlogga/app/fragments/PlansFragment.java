package com.fitlogga.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.R;
import com.fitlogga.app.activities.PlanCreatorActivity;
import com.fitlogga.app.adapters.plans.PlanSummaryRecyclerAdapter;
import com.fitlogga.app.models.plan.PlanCreator;
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.models.plan.PlanSummary;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        List<PlanSummary> planSummaries = new PlanReader(view.getContext()).getPlanSummaries();
        recyclerView.setAdapter(new PlanSummaryRecyclerAdapter(planSummaries));

    }

    private void initFabAddPost(View view) {
        FloatingActionButton fab = view.findViewById(R.id.fab_add_plan);
        fab.setOnClickListener(this::promptCreatePlanDialog);
    }

    private void promptCreatePlanDialog(View view) {

        String[] choices = view.getResources().getStringArray(R.array.plan_creation_options);
        final int CREATE_NEW_PLAN_INDEX = 0;
        final int DOWNLOAD_PLAN_INDEX = 1;

        new LovelyChoiceDialog(view.getContext())
                .setTopColorRes(R.color.colorPrimaryDark)
                .setIcon(R.drawable.ic_add_white)
                .setTitle("Plan Creator")
                .setMessage("How do you want to create a plan?")
                .setItems(choices, (position, item) -> {
                    switch (position) {
                        case CREATE_NEW_PLAN_INDEX:
                            openPlanCreator(view);
                            break;
                        case DOWNLOAD_PLAN_INDEX:
                            break;

                    }
                })
                .show();
    }

    private void openPlanCreator(View view) {
        Intent intent = new Intent(view.getContext(), PlanCreatorActivity.class);
        startActivity(intent);
    }


}
