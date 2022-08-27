package com.example.v2_ltd.activity;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.v2_ltd.R;
import com.example.v2_ltd.adapter.MyListAdapter;
import com.example.v2_ltd.database.AppDatabase;
import com.example.v2_ltd.database.entity.SaveData;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    List<SaveData> dataList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        AppDatabase mAppDatabase = AppDatabase.getInstance(this);

        List<SaveData> data = mAppDatabase.dataDao().findAll();
        if (data.size() > 0) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            MyListAdapter adapter = new MyListAdapter(data);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }


    }
}