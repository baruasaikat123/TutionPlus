package com.example.tutionplus.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutionplus.ProjectClass.ItemViewModel;
import com.example.tutionplus.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;


public class QualificationDetail extends Fragment {

    View view;
    AutoCompleteTextView dropdown_item_degree;
    RadioGroup teacher_profession_radio_group, teacher_experience_radio_group;
    RadioButton teacher_profession_school, teacher_profession_college, teacher_profession_private_tutor, yes_experience, no_experience;
    TextInputEditText subject_specialize, years_of_experience;
    TextView years_of_experience_text;
    Button next_btn_qualification;
    private SharedPreferences sharedPreferences;
    private ItemViewModel viewModel;
    private String profession=null, experience=null, selectedEducationLevel=null, years=null;
    private  SharedPreferences.Editor editor;
    private static final int step = 4;
    private int checkId=-1, id=-1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_qualification_detail, container, false);

        //binding
        binding();

        //sharedPreferences
        sharedPreferences = getContext().getSharedPreferences("teacherInfo", Context.MODE_PRIVATE);

        //view model
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        //get state
        getState();

        //select education level
        dropdown_item_degree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedEducationLevel = adapterView.getItemAtPosition(i).toString();
            }
        });

        //experience radio group
        teacher_experience_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if(i == R.id.yes_experience_radio_btn){

                    years_of_experience.setVisibility(View.VISIBLE);
                    years_of_experience_text.setVisibility(View.VISIBLE);

                    years = years_of_experience.getText().toString();

                    if(years.isEmpty()){
                        //Toast.makeText(getContext(), "Please enter your years of experience.", Toast.LENGTH_SHORT).show();
                        viewModel.setCustomText("Please enter your years of experience.");
                        return;
                    }
                    experience = years + "years of experience";
                }

                else if(i == R.id.no_experience_radio_btn){
                    years_of_experience.setVisibility(View.GONE);
                    years_of_experience_text.setVisibility(View.GONE);
                    viewModel.setCustomText("");
                    experience = "NEW";
                }
            }
        });

        next_btn_qualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkId = teacher_profession_radio_group.getCheckedRadioButtonId();

                if(checkId == -1){
                    //Toast.makeText(getContext(), "Please select your profession", Toast.LENGTH_SHORT).show();
                    viewModel.setCustomText("Please select your profession");
                    return;
                }

                if(checkId == R.id.school_teacher_radio_btn){
                    profession = "School Teacher";
                }

                else if(checkId == R.id.college_professor_radio_btn){
                    profession = "College Professor";
                }

                else if(checkId == R.id.private_tutor_radio_btn){
                    profession = "Private Tutor";
                }

                id = teacher_experience_radio_group.getCheckedRadioButtonId();

                if(id == -1){
                    //Toast.makeText(getContext(), "Please select your experience", Toast.LENGTH_SHORT).show();
                    viewModel.setCustomText("Please select your experience");
                    return;
                }

                if(id== R.id.yes_experience_radio_btn && years_of_experience.getText().toString().isEmpty()){
                    //Toast.makeText(getContext(), "Please enter your years of experience.", Toast.LENGTH_SHORT).show();
                    viewModel.setCustomText("Please enter your years of experience.");
                    return;
                }

                if(selectedEducationLevel==null){
                    //Toast.makeText(getContext(), "Please select your education level.", Toast.LENGTH_SHORT).show();
                    viewModel.setCustomText("Please select your education level.");
                    return;
                }

                //set state
                setState();

                //view model
                viewModel.setFormStep(step);
            }
        });


        return  view;
    }


    private void binding(){
        dropdown_item_degree = view.findViewById(R.id.dropdown_item_degree);
        teacher_experience_radio_group = view.findViewById(R.id.teacher_experience_radio_group);
        teacher_profession_radio_group = view.findViewById(R.id.teacher_profession_radio_group);
        teacher_profession_school = view.findViewById(R.id.school_teacher_radio_btn);
        teacher_profession_college = view.findViewById(R.id.college_professor_radio_btn);
        teacher_profession_private_tutor = view.findViewById(R.id.private_tutor_radio_btn);
        yes_experience = view.findViewById(R.id.yes_experience_radio_btn);
        no_experience = view.findViewById(R.id.no_experience_radio_btn);
        subject_specialize = view.findViewById(R.id.subject_specialize);
        years_of_experience = view.findViewById(R.id.years_of_experience);
        years_of_experience_text = view.findViewById(R.id.years_of_experience_text);
        next_btn_qualification = view.findViewById(R.id.next_btn_qualification);
    }

    private void setState() {
        editor = sharedPreferences.edit();
        editor.putString("profession", profession);
        editor.putString("experience",experience);
        editor.putString("educationLevel", selectedEducationLevel);
        editor.putString("years", years_of_experience.getText().toString());
        editor.putString("subjectSpecialize", subject_specialize.getText().toString());
        editor.putInt("professionId",checkId);
        editor.putInt("experienceId",id);
        editor.apply();
    }

    private void getState(){

        selectedEducationLevel = sharedPreferences.getString("educationLevel",null);
        String subjectSpecialize = sharedPreferences.getString("subjectSpecialize",null);
        String years_experience = sharedPreferences.getString("years",null);
        int professionId = sharedPreferences.getInt("professionId",-1);
        int experienceId = sharedPreferences.getInt("experienceId",-1);

        if(professionId!=-1){
            teacher_profession_radio_group.check(professionId);
        }

        if(experienceId!=-1){
            teacher_experience_radio_group.check(experienceId);
            if(experienceId==R.id.yes_experience_radio_btn){
                years_of_experience.setVisibility(View.VISIBLE);
                years_of_experience_text.setVisibility(View.VISIBLE);
            }
        }

        if(selectedEducationLevel!=null){
            dropdown_item_degree.setText(selectedEducationLevel);
        }

        if(subjectSpecialize!=null){
            subject_specialize.setText(subjectSpecialize);
        }

        if(years_experience!=null){
            years_of_experience.setText(years_experience);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        String[] degree_items = getResources().getStringArray(R.array.degree);
        ArrayAdapter<String> degreeAdapterItems = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, degree_items);
        dropdown_item_degree.setAdapter(degreeAdapterItems);
    }
}