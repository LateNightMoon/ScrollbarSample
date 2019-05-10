package com.whartonjason.recyclerviewscrollbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_view_bar)
    RecyclerViewScrollBar recyclerViewBar;

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewDefault();
    }

    private void initViewDefault() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        SimpleAdapter adapter = new SimpleAdapter(this,mItems);
        recyclerView.setAdapter(adapter);
        recyclerViewBar.bindRecyclerView(recyclerView);
    }

}
