package com.example.tutionplus.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Switch;

import com.example.tutionplus.ProjectClass.ItemViewModel;
import com.example.tutionplus.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PreferenceDetail extends Fragment {

    View view;
    MultiAutoCompleteTextView dropdown_subject_list, dropdown_pattern_list;
    AutoCompleteTextView dropdown_type, dropdown_mode;
    Button next_btn_preference;
    Switch free_teaching_switch;
    TextInputEditText fees;
    TextInputLayout fees_layout;
    CheckBox fees_not_specify;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ItemViewModel viewModel;
    private String selectedType = null, selectedMode = null;
    private Boolean availableForFreeTeaching = false, feesNotSpecify = false;
    private static final int step = 5;
    //private String teacherFees = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_preference_detail, container, false);

        //binding
        binding();

        //sharedPreferences
        sharedPreferences = getContext().getSharedPreferences("teacherInfo", Context.MODE_PRIVATE);

        //view model
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        //get state
        getState();

        //check box
        fees_not_specify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()){
                    feesNotSpecify = true;
                    fees_layout.setBoxBackgroundColor(ContextCompat.getColor(getContext(), R.color.app_light));
                    fees.setEnabled(false);
                }
                else {
                    feesNotSpecify = false;
                    fees_layout.setBoxBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    fees.setEnabled(true);
                }
            }
        });

        //fees
        fees.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().isEmpty()){
                    feesNotSpecify = false;
                    //teacherFees = charSequence.toString();
                    //fees_not_specify.setButtonDrawable(R.color.app_light);
                    fees_not_specify.setPaintFlags(fees_not_specify.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    fees_not_specify.setEnabled(false);
                }
                else {
                    //teacherFees = null;
                    //fees_not_specify.setButtonDrawable(R.color.app_main);
                    fees_not_specify.setPaintFlags(fees_not_specify.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    fees_not_specify.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //switch
        free_teaching_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) availableForFreeTeaching = true;

                else availableForFreeTeaching = false;
            }
        });

        //select type
        dropdown_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedType = adapterView.getItemAtPosition(i).toString();
            }
        });

        //select mode
        dropdown_mode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMode = adapterView.getItemAtPosition(i).toString();
            }
        });

        next_btn_preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dropdown_subject_list.getText().toString().isEmpty()){
                    viewModel.setCustomText("Please select your subjects.");
                    return;
                }
                if(dropdown_pattern_list.getText().toString().isEmpty()){
                    viewModel.setCustomText("Please select a board");
                    return;
                }
                if(selectedType==null){
                    viewModel.setCustomText("Please select your tution type.");
                    return;
                }
                if(selectedMode==null){
                    viewModel.setCustomText("Please select your mode of tution.");
                    return;
                }

                if(!fees_not_specify.isChecked() && fees.getText().toString().isEmpty()){
                    viewModel.setCustomText("Please specify your fees detail\nor check the box that you don't want to specify.");
                    return;
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
        dropdown_subject_list = view.findViewById(R.id.dropdown_subject_list);
        dropdown_type = view.findViewById(R.id.dropdown_type);
        dropdown_mode = view.findViewById(R.id.dropdown_mode);
        dropdown_pattern_list = view.findViewById(R.id.dropdown_pattern);
        next_btn_preference = view.findViewById(R.id.next_btn_preferences);
        fees = view.findViewById(R.id.fees);
        free_teaching_switch = view.findViewById(R.id.free_teaching_available_switch);
        fees_not_specify = view.findViewById(R.id.fees_not_specify_check_box);
        fees_layout = view.findViewById(R.id.fees_layout);
    }

    private void setState(){
        editor = sharedPreferences.edit();
        editor.putString("subjects", dropdown_subject_list.getText().toString());
        editor.putString("patterns", dropdown_pattern_list.getText().toString());
        editor.putString("type", selectedType);
        editor.putString("mode",selectedMode);
        editor.putBoolean("available", availableForFreeTeaching);
        editor.putBoolean("feesNotSpecify", feesNotSpecify);
        editor.putString("fees", fees.getText().toString());
        editor.apply();
    }

    private void getState(){

        String subjects = sharedPreferences.getString("subjects",null);
        String patterns = sharedPreferences.getString("patterns",null);
        selectedType = sharedPreferences.getString("type",null);
        selectedMode = sharedPreferences.getString("mode", null);
        availableForFreeTeaching = sharedPreferences.getBoolean("available", false);
        feesNotSpecify = sharedPreferences.getBoolean("feesNotSpecify", false);
        String teacherFees = sharedPreferences.getString("fees", "");

        if(subjects!=null){
            dropdown_subject_list.setText(subjects);
        }

        if(patterns!=null){
            dropdown_pattern_list.setText(patterns);
        }

        if(selectedMode!=null){
            dropdown_mode.setText(selectedMode);
        }

        if(selectedType!=null){
            dropdown_type.setText(selectedType);
        }

        if(availableForFreeTeaching==true){
            free_teaching_switch.setChecked(true);
        }

        if(!teacherFees.isEmpty()){
            fees.setText(teacherFees);
            feesNotSpecify = false;
            fees_not_specify.setPaintFlags(fees_not_specify.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            fees_not_specify.setChecked(false);
            fees_not_specify.setEnabled(false);
            fees_layout.setBoxBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            fees.setEnabled(true);
        }

        if(feesNotSpecify==true){
            fees_not_specify.setChecked(true);
            fees_layout.setBoxBackgroundColor(ContextCompat.getColor(getContext(), R.color.app_light));
            fees.setEnabled(false);
        }
    }

    private void setDropdownType(){
        String[] types = getResources().getStringArray(R.array.type);
        ArrayAdapter<String> typeAdapterItems = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, types);
        dropdown_type.setAdapter(typeAdapterItems);
    }

    private void setDropdownMode(){
        String[] modes = getResources().getStringArray(R.array.mode);
        ArrayAdapter<String> modeAdapterItems = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, modes);
        dropdown_mode.setAdapter(modeAdapterItems);
    }

    private void setDropdownSubject(){
        String[] subjects = getResources().getStringArray(R.array.subject);
        ArrayAdapter<String> subjectAdapterItems = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, subjects);
        dropdown_subject_list.setAdapter(subjectAdapterItems);
        // set threshold value 1 that help us to start the searching from first character
        dropdown_subject_list.setThreshold(1);
        // set tokenizer that distinguish the various substrings by comma
        dropdown_subject_list.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private void setDropdownPattern(){
        String[] patterns= getResources().getStringArray(R.array.pattern);
        ArrayAdapter<String> patternAdapterItems = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, patterns);
        dropdown_pattern_list.setAdapter(patternAdapterItems);
        // set threshold value 1 that help us to start the searching from first character
        dropdown_pattern_list.setThreshold(1);
        // set tokenizer that distinguish the various substrings by comma
        dropdown_pattern_list.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    @Override
    public void onResume() {
        super.onResume();

        setDropdownSubject();
        setDropdownType();
        setDropdownMode();
        setDropdownPattern();
    }
}