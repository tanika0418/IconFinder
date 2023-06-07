package com.example.iconfinder.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.iconfinder.R;
import com.example.iconfinder.adapter.IconListAdapter;
import com.example.iconfinder.adapter.IconSetAdapter;
import com.example.iconfinder.custom.BucketRecyclerView;
import com.example.iconfinder.custom.CustomSnacks;
import com.example.iconfinder.models.IconModel;
import com.example.iconfinder.models.IconSetModel;
import com.example.iconfinder.viewmodel.IconListViewModel;
import com.example.iconfinder.viewmodel.IconSetViewModel;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private IconSetAdapter iconSetAdapter;
    private IconSetViewModel iconSetViewModel;
    private List<IconSetModel> iconSets = new ArrayList<>();
    private ImageView searchIcon;
    private ProgressBar progressBarMain;
    private CardView progressBar;
    private boolean isScrolling;
    BucketRecyclerView recyclerView;
    int currentItems, totalItems, scrollOutItems;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    private CustomSnacks customSnacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        customSnacks = new CustomSnacks();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission,WRITE_EXTERNAL_STORAGE_CODE);
            }
        }

        progressBar = findViewById(R.id.progress);
        progressBarMain = findViewById(R.id.progress_main);
        progressBarMain.setVisibility(View.VISIBLE);
        searchIcon = findViewById(R.id.search_icon);

        recyclerView = findViewById(R.id.rv_mainIcons);
        LinearLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        iconSetAdapter = new IconSetAdapter(this,iconSets);
        recyclerView.setAdapter(iconSetAdapter);

        iconSetViewModel = new ViewModelProvider(this).get(IconSetViewModel.class);
        iconSetViewModel.getIconSets(null).observe(this, new Observer<List<IconSetModel>>() {
            @Override
            public void onChanged(List<IconSetModel> iconSetModels) {
                if (iconSetModels != null){
                    iconSets.addAll(iconSetModels);
                    progressBarMain.setVisibility(View.GONE);
                    iconSetAdapter.notifyDataSetChanged();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems))
                {
                    isScrolling = false;
                    int id = iconSets.get(iconSets.size()-1).getId();
                    progressBar.setVisibility(View.VISIBLE);
                    iconSetViewModel.getIconSets(id).observe(Dashboard.this, new Observer<List<IconSetModel>>() {
                        @Override
                        public void onChanged(List<IconSetModel> iconSetModels) {
                            if (iconSetModels != null){
                                iconSets.addAll(iconSetModels);
                                iconSetAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this,SearchIcon.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        View view = findViewById(android.R.id.content);
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //---( Permission Granted )---//
                customSnacks.successSnack(view, "Storage Permission Granted!");
            } else {
                //---( Permission Denied )---//
                customSnacks.failSnack(view, "Storage Permission Denied!");
            }
        }
    }
}
