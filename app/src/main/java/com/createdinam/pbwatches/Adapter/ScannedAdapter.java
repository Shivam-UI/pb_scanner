package com.createdinam.pbwatches.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.createdinam.pbwatches.R;

import java.util.List;

public class ScannedAdapter extends RecyclerView.Adapter<ScannedAdapter.ScanHolder> {

    List<String> mList;
    Context mContext;

    public ScannedAdapter(List<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ScanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.scan_list_items,parent,false);
        return new ScanHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanHolder holder, int position) {
        holder.tv_item_scan.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ScanHolder extends RecyclerView.ViewHolder{
        TextView tv_item_scan;
        public ScanHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_scan = itemView.findViewById(R.id.tv_item_scan);
        }
    }
}
