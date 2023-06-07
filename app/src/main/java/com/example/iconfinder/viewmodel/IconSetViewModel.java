package com.example.iconfinder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.iconfinder.models.IconSetModel;
import com.example.iconfinder.repo.IconSetRepo;

import java.util.List;

public class IconSetViewModel extends ViewModel {

    private MutableLiveData<List<IconSetModel>> iconSets;
    private IconSetRepo iconSetRepo;

    public IconSetViewModel() {
    }

    public MutableLiveData<List<IconSetModel>> getIconSets(Integer after) {
        iconSetRepo = new IconSetRepo(after);
        iconSets = iconSetRepo.getIconSets();
        return iconSets;
    }
}
