package com.tree.scroll;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tree.scroll.databinding.SimpleListItemBinding;

import java.util.List;

/**
 * @author whartonjason
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

    private List<String> mItems;

    private final LayoutInflater inflater;

    SimpleAdapter(Context context, List<String> mItems) {
        this.mItems = mItems;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SimpleListItemBinding binding = DataBindingUtil.
                inflate(inflater, R.layout.simple_list_item, viewGroup, false);
        return new SimpleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int i) {
        holder.binding.simpleText.setText(mItems.get(i));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {

        SimpleListItemBinding binding;

        SimpleViewHolder(@NonNull SimpleListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
