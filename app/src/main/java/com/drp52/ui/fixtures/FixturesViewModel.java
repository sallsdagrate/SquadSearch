package com.drp52.ui.fixtures;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FixturesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FixturesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is fixture fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}