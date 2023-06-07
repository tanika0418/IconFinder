package com.example.iconfinder.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iconfinder.R;
import com.example.iconfinder.adapter.IconListAdapter;
import com.example.iconfinder.adapter.IconSetAdapter;
import com.example.iconfinder.custom.BucketRecyclerView;
import com.example.iconfinder.helpers.MyApplicationHelper;
import com.example.iconfinder.models.IconModel;
import com.example.iconfinder.models.IconSetModel;

import java.util.List;

public class IconSetDetail extends AppCompatActivity {

    private List<IconModel> icons;
    private IconListAdapter iconListAdapter;
    private TextView iconHead;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_set_detail);

        View view = findViewById(android.R.id.content);

        iconHead = findViewById(R.id.iconset_name_head);
        backBtn = findViewById(R.id.back_btn);

        IconSetModel iconSetModel = ((MyApplicationHelper) getApplicationContext()).getIconSetModel();

        icons = iconSetModel.getIcons();
        iconHead.setText(iconSetModel.getName());

        BucketRecyclerView recyclerView = findViewById(R.id.rv_iconset_detail);
        LinearLayoutManager layoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(layoutManager);
        iconListAdapter = new IconListAdapter(this,icons,view);
        recyclerView.setAdapter(iconListAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}