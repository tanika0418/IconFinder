package com.example.iconfinder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.iconfinder.models.IconModel;
import com.example.iconfinder.repo.SearchIconRepo;

import java.util.List;

public class IconListViewModel extends ViewModel {

    private MutableLiveData<List<IconModel>> iconData;
    private SearchIconRepo iconFinderRepo;

    public IconListViewModel() {
    }

    public MutableLiveData<List<IconModel>> getIconData(String query,Integer offset) {
        iconFinderRepo = new SearchIconRepo(query,offset);
        iconData = iconFinderRepo.getMainIcons();
        return iconData;
    }
}
