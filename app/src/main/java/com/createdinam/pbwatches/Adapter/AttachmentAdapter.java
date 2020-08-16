package com.createdinam.pbwatches.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.createdinam.pbwatches.Model.AttachmentModel;
import com.createdinam.pbwatches.R;

import java.util.List;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.DocumentHolder> {

    List<AttachmentModel> mList;
    Context mContext;

    public AttachmentAdapter(List<AttachmentModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DocumentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.file_name_list,parent,false);
        return new DocumentHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentHolder holder, int position) {
        holder.tv_file_name.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class DocumentHolder extends RecyclerView.ViewHolder {
        TextView tv_file_name, tv_create_date;

        public DocumentHolder(@NonNull View itemView) {
            super(itemView);
            tv_file_name = itemView.findViewById(R.id.tv_file_name);
            tv_create_date = itemView.findViewById(R.id.tv_create_date);
        }
    }
}
