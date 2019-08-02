package com.fitlogga.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(0));

        List<PlanSummary> planSummaries = PlanReader.getPlanSummaries();
        recyclerView.setAdapter(new PlanSummaryRecyclerAdapter(planSummaries));

    }

    private void initFabAddPost(View view) {
        FloatingActionButton fab = view.findViewById(R.id.fab_add_plan);
        fab.setOnClickListener(fabView -> promptCreatePlanDialog(view));
    }

    static void promptCreatePlanDialog(View view) {

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
                            tryToOpenPlanCreator(view);
                            break;
                        case DOWNLOAD_PLAN_INDEX:
                            break;

                    }
                })
                .show();
    }

    private static void tryToOpenPlanCreator(View view) {
        int numExistingPlans = PlanReader.getNumberOfPlans();
        if (numExistingPlans < FreeAppSettings.MAX_PLANS) {
            Intent intent = new Intent(view.getContext(), PlanCreatorActivity.class);
            view.getContext().startActivity(intent);
        }
        else {
            String message = "You can only have 3 fitness plans at a time. " +
                    "Upgrade your fitness experience today for unlimited!";
            PremiumApp.popupPremiumAppDialog(view.getContext(), message);
        }
    }


}
