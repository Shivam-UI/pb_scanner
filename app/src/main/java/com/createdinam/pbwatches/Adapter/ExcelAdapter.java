package com.createdinam.pbwatches.Adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.createdinam.pbwatches.R;

import java.util.List;
import java.util.Map;

public class ExcelAdapter extends BaseQuickAdapter<Map<Integer, Object>, BaseViewHolder> {
    String color_code;
    public ExcelAdapter(@Nullable List<Map<Integer, Object>> data,String color_code) {
        super(R.layout.template1_item, data);
        this.color_code = color_code;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Map<Integer, Object> item) {
        LinearLayout view = (LinearLayout) helper.itemView;
        view.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        for (int i = 0; i < item.size(); i++) {
            TextView textView = new TextView(mContext);
            textView.setTextSize(13);
            textView.setText(item.get(i) + "");
            textView.setBackgroundColor(Color.parseColor(color_code));
            textView.setLayoutParams(layoutParams);
            textView.setPadding(10, 15, 10, 15);
            textView.setBackground(mContext.getResources().getDrawable(R.drawable.textview_border));
            view.addView(textView);
        }

    }
}
