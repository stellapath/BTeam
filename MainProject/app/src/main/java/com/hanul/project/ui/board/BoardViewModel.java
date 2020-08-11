package com.hanul.project.ui.board;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BoardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BoardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Board fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}