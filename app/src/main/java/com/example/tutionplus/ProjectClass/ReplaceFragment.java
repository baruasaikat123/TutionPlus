package com.example.tutionplus.ProjectClass;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class ReplaceFragment {

    public void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int layoutCode){

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layoutCode, fragment);
        fragmentTransaction.commit();
    }

}
