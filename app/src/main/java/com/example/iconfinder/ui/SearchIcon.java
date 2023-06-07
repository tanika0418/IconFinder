package com.example.iconfinder.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.iconfinder.R;
import com.example.iconfinder.adapter.IconListAdapter;
import com.example.iconfinder.adapter.IconSetAdapter;
import com.example.iconfinder.custom.BucketRecyclerView;
import com.example.iconfinder.models.IconModel;
import com.example.iconfinder.models.IconSetModel;
import com.example.iconfinder.viewmodel.IconListViewModel;
import com.example.iconfinder.viewmodel.IconSetViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchIcon extends AppCompatActivity {

    private ProgressBar progressBarMain;
    private CardView progressBar;
    private LinearLayout searchNow;
    private List<IconModel> iconsList = new ArrayList<>();
    private IconListAdapter iconListAdapter;
    private IconListViewModel iconListViewModel;
    private SearchView searchView;
    private String q;
    private boolean isScrolling;
    int scrollCount = 0;
    int currentItems, totalItems, scrollOutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_icon);

        View view = findViewById(android.R.id.content);

        progressBar = findViewById(R.id.progress);
        progressBarMain = findViewById(R.id.progress_main);
        searchNow = findViewById(R.id.search_now);
        searchView = findViewById(R.id.search_bar);

        BucketRecyclerView recyclerView = findViewById(R.id.rv_search_icon);
        LinearLayoutManager layoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(layoutManager);
        iconListAdapter = new IconListAdapter(this,iconsList,view);
        recyclerView.setAdapter(iconListAdapter);

        iconListViewModel = new ViewModelProvider(this).get(IconListViewModel.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                iconsList.clear();
                q = query;
                searchNow.setVisibility(View.GONE);
                progressBarMain.setVisibility(View.VISIBLE);
                //---( Tobedone --)
                iconListViewModel.getIconData(query,null).observe(SearchIcon.this, new Observer<List<IconModel>>() {
                    @Override
                    public void onChanged(List<IconModel> iconModels) {
                        progressBarMain.setVisibility(View.GONE);
                        iconsList.addAll(iconModels);
                        iconListAdapter.notifyDataSetChanged();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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

                if(isScrolling && (currentItems + scrollOutItems == totalItems) && totalItems>39)
                {
                    isScrolling = false;
                    scrollCount++;
                    progressBar.setVisibility(View.VISIBLE);
                    iconListViewModel.getIconData(q,40*scrollCount).observe(SearchIcon.this, new Observer<List<IconModel>>() {
                        @Override
                        public void onChanged(List<IconModel> iconModels) {
                            progressBar.setVisibility(View.GONE);
                            iconsList.addAll(iconModels);
                            iconListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}