package com.example.tutionplus.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tutionplus.ProjectClass.ItemViewModel;
import com.example.tutionplus.R;
import com.google.android.material.textfield.TextInputEditText;


public class AdditionalInfo extends Fragment {

    View view;
    Button final_submit_btn;
    TextInputEditText teacher_description;
    ImageView teacher_pic;
    private SharedPreferences sharedPreferences;
    private ItemViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_addition_info, container, false);

        //binding
        binding();

        //sharedPreferences
        sharedPreferences = getContext().getSharedPreferences("teacherInfo", Context.MODE_PRIVATE);

        //view model
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        //get state
        getState();


        return view;
    }

    private void binding(){
        teacher_description = view.findViewById(R.id.teacher_description);
        teacher_pic = view.findViewById(R.id.teacher_pic);
        final_submit_btn = view.findViewById(R.id.final_submit_btn);
    }

    private void setState(){

    }

    private void getState(){

    }
}