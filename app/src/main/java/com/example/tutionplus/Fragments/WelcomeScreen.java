package com.example.tutionplus.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.example.tutionplus.ProjectClass.ItemViewModel;
import com.example.tutionplus.R;


public class WelcomeScreen extends Fragment {


    View view;
    private ItemViewModel viewModel;
    Button continue_btn;
    FrameLayout welcome_screen_frag;
    RadioGroup radio_group_user;
    private float v = 0;
    private String get_user_type = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);

        continue_btn = view.findViewById(R.id.continue_btn);
        welcome_screen_frag = view.findViewById(R.id.welcome_screen_frag);
        radio_group_user = view.findViewById(R.id.radio_group_user);

        setLayoutAnimation();

        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);


        radio_group_user.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {

                if(checkId == R.id.radio_student){
                    get_user_type = getResources().getString(R.string.student);
                }
                else if(checkId == R.id.radio_teacher){
                    get_user_type = getResources().getString(R.string.teacher);

                }

                viewModel.setCustomText(get_user_type);
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int getId = radio_group_user.getCheckedRadioButtonId();

                if(getId==-1){

                    int unicode = 0x1F603;
                    String emoji = new String(Character.toChars(unicode));
                    viewModel.setCustomText("Welcome "+emoji+" \n Please select a category to continue.");
                    return;
                }

                viewModel.setAnimation(R.raw.mobile_animate);
                viewModel.setCustomText(getResources().getString(R.string.enter_mobile_text));
                Bundle bundle = new Bundle();
                bundle.putString("userType",get_user_type);
                MobileAuth mobileAuth = new MobileAuth();
                mobileAuth.setArguments(bundle);
                replaceFragment(mobileAuth);
            }
        });

        return view;
    }

//    public int onRadioButtonClicked(int checkId){
//
//        String student = "Student";
//        String teacher = "Teacher";
//
//        switch (checkId){
//
//            case R.id.radio_student: return student;
//
//            case R.id.radio_teacher: return teacher;
//
//        }
//        return "-1";
//    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_main_layout, fragment);
        fragmentTransaction.commit();
    }

    private void setLayoutAnimation(){

        welcome_screen_frag.setTranslationY(900);
        welcome_screen_frag.setAlpha(v);
        welcome_screen_frag.animate().translationY(0).alpha(1).setDuration(900).setStartDelay(0).start();

    }
}