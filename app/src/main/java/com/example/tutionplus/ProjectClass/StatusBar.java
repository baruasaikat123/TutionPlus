package com.example.tutionplus.ProjectClass;

import android.content.Context;
import android.os.Build;
import android.view.Window;

import androidx.core.content.ContextCompat;

import com.example.tutionplus.R;

public class StatusBar {

    public void getStatusBarColor(Window window, Context context, int colorCode){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.setStatusBarColor(ContextCompat.getColor(context, colorCode));
        }
    }
}
