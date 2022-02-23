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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tutionplus.ProjectClass.ItemViewModel;
import com.example.tutionplus.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class BasicInfo extends Fragment {

    View view;
    TextInputEditText teacher_name;
    TextInputLayout teacher_name_layout;
    RadioGroup select_gender_radio_group;
    RadioButton male_radio_btn, female_radio_btn;
    Button next_btn_basic_info;
    private SharedPreferences sharedPreferences;
    private String gender = null;
    private SharedPreferences.Editor editor;
    private ItemViewModel viewModel;
    private static final int step = 2;
    private int checkId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_basic_info, container, false);

        //binding
        binding();

        //sharedPreferences
        sharedPreferences = getContext().getSharedPreferences("teacherInfo", Context.MODE_PRIVATE);

        //view model
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        //get state
        getState();


        next_btn_basic_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkId = select_gender_radio_group.getCheckedRadioButtonId();

                if(teacher_name.getText().toString().isEmpty()){
                    //Toast.makeText(getContext(), "Enter your name first", Toast.LENGTH_SHORT).show();
                    viewModel.setCustomText("Enter your name first");
                    return;
                }
                if(checkId==-1) {
                    //Toast.makeText(getContext(), "Select your gender", Toast.LENGTH_SHORT).show();
                    viewModel.setCustomText("Select your gender");
                    return;
                }

                if(checkId == R.id.radio_btn_male){
                    gender = "Male";
                }
                else if(checkId == R.id.radio_btn_female){
                    gender = "Female";
                }

                //set state
                setState();

                //set form step
                viewModel.setFormStep(step);

            }
        });

        return view;
    }

    private void binding(){
        teacher_name = view.findViewById(R.id.teacher_full_name);
        teacher_name_layout = view.findViewById(R.id.teacher_full_name_layout);
        select_gender_radio_group = view.findViewById(R.id.select_gender_radio_group);
        male_radio_btn = view.findViewById(R.id.radio_btn_male);
        female_radio_btn = view.findViewById(R.id.radio_btn_female);
        next_btn_basic_info = view.findViewById(R.id.next_btn_basic_info);
    }
    private void setState(){
        editor = sharedPreferences.edit();
        editor.putString("name",teacher_name.getText().toString());
        editor.putString("gender", gender);
        editor.putInt("genderId",checkId);
        editor.apply();
    }
    private void getState(){

        String name = sharedPreferences.getString("name",null);
        //String gender = sharedPreferences.getString("gender",null);
        int genderId = sharedPreferences.getInt("genderId",-1);
        if(name!=null){
            teacher_name.setText(name);
        }
        if(genderId!=-1){
            select_gender_radio_group.check(genderId);
        }
    }
}