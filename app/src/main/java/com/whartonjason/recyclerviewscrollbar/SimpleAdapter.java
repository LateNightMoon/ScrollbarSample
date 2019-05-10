package com.whartonjason.recyclerviewscrollbar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

    private List<String> mItems;

    private LayoutInflater inflater;

    public SimpleAdapter(Context context, List<String> mItems) {
        this.mItems = mItems;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.simple_text_recycler_item, viewGroup, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder simpleViewHolder, int i) {
        String s = mItems.get(i);
        simpleViewHolder.simpleText.setText(s);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.simple_text)
        TextView simpleText;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
