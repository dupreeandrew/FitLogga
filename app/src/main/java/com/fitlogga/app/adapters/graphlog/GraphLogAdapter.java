package com.fitlogga.app.adapters.graphlog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.graphlog.viewholders.HistoryViewHolder;
import com.fitlogga.app.adapters.graphlog.viewholders.LineChartUnitGen;
import com.fitlogga.app.models.plan.log.Historics.History;
import com.fitlogga.app.viewmods.datelinechart.DateLineCharter;

import java.util.List;

public class GraphLogAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private List<History> historyList;

    public GraphLogAdapter(List<History> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View standardLayout = inflater.inflate(R.layout.vh_graph_log, parent, false);
        return new HistoryViewHolder(standardLayout);

    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {

        if (historyList.size() == 0) {
            return;
        }

        History history = historyList.get(position);
        DateLineCharter.Unit unit = LineChartUnitGen.get(history);
        String title = history.getName();

        holder.setGraphUnit(unit);
        holder.setTitle(title);


    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
