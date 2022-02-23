package com.example.tutionplus.ProjectActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tutionplus.Fragments.AdditionalInfo;
import com.example.tutionplus.Fragments.LocationDetail;
import com.example.tutionplus.Fragments.PreferenceDetail;
import com.example.tutionplus.Fragments.QualificationDetail;
import com.example.tutionplus.ProjectClass.ItemViewModel;
import com.example.tutionplus.ProjectClass.ReplaceFragment;
import com.example.tutionplus.ProjectClass.StatusBar;
import com.example.tutionplus.R;

public class TeacherInfo extends AppCompatActivity {


    TextView form_info_text, form_error_text;
    Button prev_step_btn;
    private int step = 1;
    private static final String total_step = "/4";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ItemViewModel viewModel;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);

        //status bar color
        new StatusBar().getStatusBarColor(this.getWindow(), this, R.color.app_main);

        //binding
        binding();

        //sharedPreferences
        sharedPreferences = getSharedPreferences("teacherInfo",MODE_PRIVATE);

        //view model
        setViewModel();

        //fragment manager
        fragmentManager = getSupportFragmentManager();

        //Basic Info Fragment Load
        new ReplaceFragment().replaceFragment(fragmentManager, new AdditionalInfo(), R.id.teacher_form_layout);

        prev_step_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step--;

                if(step==0) {
                    onBackPressed();
                    return;
                }
                renderStepForm();
            }
        });
    }

    private void binding(){
        prev_step_btn = findViewById(R.id.prev_step_btn);
        form_info_text = findViewById(R.id.form_info_text_view);
        form_error_text = findViewById(R.id.form_error_text_view);
    }

    private void renderStepForm(){

        form_error_text.setVisibility(View.GONE);

        if(step==1){
            new ReplaceFragment().replaceFragment(fragmentManager, new AdditionalInfo(), R.id.teacher_form_layout);
            form_info_text.setText("1.\tEnter your basic details: ");
        }

        else if(step==2){
            form_info_text.setText("2.\tEnter your location details: ");
            new ReplaceFragment().replaceFragment(fragmentManager, new LocationDetail(), R.id.teacher_form_layout);
        }

        else if(step==3){
            form_info_text.setText("3.\tEnter your qualification details: ");
            new ReplaceFragment().replaceFragment(fragmentManager, new QualificationDetail(), R.id.teacher_form_layout);
        }

        else if(step==4){
            form_info_text.setText("4.\tEnter your preference details: ");
            new ReplaceFragment().replaceFragment(fragmentManager, new PreferenceDetail(), R.id.teacher_form_layout);
        }

        else if(step == 5){
            form_info_text.setText("4.\tAdditional info:");
            new ReplaceFragment().replaceFragment(fragmentManager, new AdditionalInfo(), R.id.teacher_form_layout);
        }
    }

    private void setViewModel(){
        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        viewModel.getFormStep().observe(this,(item) -> {
            step = item;
            renderStepForm();
        });

        viewModel.getOtpText().observe(this,(item) -> {

            form_error_text.setVisibility(View.VISIBLE);
            form_error_text.setText(item);
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        finish();
    }
}
