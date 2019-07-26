package com.fitlogga.app.adapters.graphlog.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.graphlog.GraphLogRecyclerAdapter;
import com.fitlogga.app.models.plan.log.Historics.History;

import java.util.List;

public class DayViewHolder extends RecyclerView.ViewHolder {

    private View itemView;

    public DayViewHolder(@NonNull View itemView) { // literally a fragment
        super(itemView);
        this.itemView = itemView;
    }

    public void setHistoryList(List<History> historyList) {
        RecyclerView recyclerView = itemView.findViewById(R.id.rv_log_graphs);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new GraphLogRecyclerAdapter(historyList));
    }




}
