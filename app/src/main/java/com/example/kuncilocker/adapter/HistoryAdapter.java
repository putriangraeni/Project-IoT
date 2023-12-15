package com.example.kuncilocker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuncilocker.R;
import com.example.kuncilocker.model.History;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<History> historyList;

    public HistoryAdapter() {
        historyList = new ArrayList<>();
    }

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = historyList.get(position);

        holder.tvType.setText(getTypeString(history.getType()));
        holder.tvName.setText(history.getName()+" by "+history.getDevice());
        holder.tvTime.setText(formatDateTime(history.getTime()));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    private String getTypeString(int type) {
        // Return the appropriate type string based on the type value
        switch (type) {
            case 0:
                return "Loker Tertutup";
            case 1:
                return "Loker Terbuka";
            default:
                return "";
        }
    }

    private String formatDateTime(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy\nHH:mm:ss", new Locale("id", "ID"));
        return dateFormat.format(date);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvType;
        TextView tvName;
        TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_type);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}

