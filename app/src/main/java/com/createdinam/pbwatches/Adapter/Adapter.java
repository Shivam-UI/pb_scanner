package com.createdinam.pbwatches.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.createdinam.pbwatches.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ExcelHolder> {

    List<String> mList;
    Context mContext;

    public Adapter(List<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ExcelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.scanner_item_list,parent,false);
        return new ExcelHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcelHolder holder, int position) {
         holder.tv_text_scanner_code.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ExcelHolder extends RecyclerView.ViewHolder {
        TextView tv_scanner_id, tv_text_scanner_code;

        public ExcelHolder(@NonNull View itemView) {
            super(itemView);
            tv_scanner_id = itemView.findViewById(R.id.tv_scanner_id);
            tv_text_scanner_code = itemView.findViewById(R.id.tv_text_scanner_code);
        }
    }

}
