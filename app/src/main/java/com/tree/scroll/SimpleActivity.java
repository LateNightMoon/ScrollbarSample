package com.tree.scroll;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tree.scroll.databinding.ActivitySimpleBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * @author whartonjason
 */
public class SimpleActivity extends AppCompatActivity {

    private List<String> mItems;

    {
        mItems = new ArrayList<>();
        mItems.add("Android");
        mItems.add("Java");
        mItems.add("Object-c");
        mItems.add("Kotlin");
        mItems.add("Swift");
        mItems.add("PHP");
        mItems.add("Python");
        mItems.add("Go");
        mItems.add("Android-1");
        mItems.add("Java-1");
        mItems.add("Object-c-1");
        mItems.add("Kotlin-1");
        mItems.add("Swift-1");
        mItems.add("PHP-1");
        mItems.add("Python-1");
        mItems.add("Go-1");
        mItems.add("Android-2");
        mItems.add("Java-2");
        mItems.add("Object-c-2");
        mItems.add("Kotlin-2");
        mItems.add("Swift-2");
        mItems.add("PHP-2");
        mItems.add("Python-2");
        mItems.add("Go-2");
    }

    private ActivitySimpleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simple);
        initViewDefault();
    }

    private void initViewDefault() {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        SimpleAdapter adapter = new SimpleAdapter(this, mItems);

        binding.simpleListView.setLayoutManager(manager);
        binding.simpleListView.setHasFixedSize(true);
        binding.simpleListView.setAdapter(adapter);
        binding.scrollBar.bindRecyclerView(binding.simpleListView);
    }

}
