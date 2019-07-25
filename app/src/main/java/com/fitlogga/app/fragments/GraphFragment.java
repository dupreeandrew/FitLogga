package com.fitlogga.app.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.graphlog.GraphLogAdapter;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.models.plan.log.Historics.History;
import com.fitlogga.app.models.plan.log.SQLLogReader;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {


    public GraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_log_graphs);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<History> historyList = getHistoryList();
        recyclerView.setAdapter(new GraphLogAdapter(historyList));

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    private List<History> getHistoryList() {
        String planName = PlanReader.getCurrentPlanName();
        SQLLogReader reader = new SQLLogReader(planName);
        List<History> historyList = reader.getHistoryList(Day.WEDNESDAY, 75);
        reader.selfDestruct();
        return historyList;
    }
}
