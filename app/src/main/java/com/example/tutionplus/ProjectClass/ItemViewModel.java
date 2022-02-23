package com.example.tutionplus.ProjectClass;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {

    private final MutableLiveData<String> otp_text = new MutableLiveData<String>();
    private final MutableLiveData<Integer> lottieAnimationViewMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> formStep = new MutableLiveData<>();

    public void setCustomText(String text){
        otp_text.setValue(text);
    }

    public void setAnimation(int animation_id){ lottieAnimationViewMutableLiveData.setValue(animation_id); }

    public void setFormStep(int step) { formStep.setValue(step); }

    public LiveData<Integer> getAnimationId(){ return lottieAnimationViewMutableLiveData; }

    public LiveData<String> getOtpText(){
        return otp_text;
    }

    public LiveData<Integer> getFormStep() { return formStep; }
}
