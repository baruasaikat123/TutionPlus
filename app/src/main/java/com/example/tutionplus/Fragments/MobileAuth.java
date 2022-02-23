package com.example.tutionplus.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tutionplus.ProjectActivity.TeacherInfo;
import com.example.tutionplus.ProjectClass.ItemViewModel;
import com.example.tutionplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


public class MobileAuth extends Fragment {

    View view;
    Button get_otp_btn;
    TextInputEditText mobile_no;
    TextInputLayout mobile_no_layout;
    FirebaseAuth fAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks fCallback;
    FrameLayout enter_mobile_frag;
    ProgressBar get_otp_loading;
    private float v = 0;
    private ItemViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_mobile_auth, container, false);


        get_otp_btn = view.findViewById(R.id.get_otp_btn);
        mobile_no = view.findViewById(R.id.enter_mobile_no);
        mobile_no_layout = view.findViewById(R.id.enter_mobile_no_layout);
        enter_mobile_frag = view.findViewById(R.id.enter_mobile_frag);
        get_otp_loading = view.findViewById(R.id.get_otp_loading);

        setLayoutAnimation();

        Bundle bundle = this.getArguments();
        String user_type = bundle.getString("userType");

        fAuth = FirebaseAuth.getInstance();

        get_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getPhone = mobile_no.getText().toString();

                if(getPhone.length()<10){

                    mobile_no_layout.setError("Invalid phone number.");
                    return;
                }

                otpSend(getPhone, user_type);

                viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
                viewModel.setCustomText(getResources().getString(R.string.otp_processing_text));
                viewModel.setAnimation(R.raw.otp_processing);
                get_otp_btn.setVisibility(View.GONE);
                mobile_no.setEnabled(false);
                get_otp_loading.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void otpSend(String phone1, String user_type){
        fCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                get_otp_loading.setVisibility(View.GONE);
                get_otp_btn.setVisibility(View.VISIBLE);
                mobile_no.setEnabled(false);
                viewModel.setCustomText(getResources().getString(R.string.otp_send_error));

                Context context = getContext();
                CharSequence text = e.getLocalizedMessage();
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                get_otp_loading.setVisibility(View.GONE);
                get_otp_btn.setVisibility(View.VISIBLE);

                Bundle bundle = new Bundle();
                bundle.putString("mobileNo", phone1);
                bundle.putString("userType",user_type);
                bundle.putString("verificationId",verificationId);

                VerifyOtp verifyOtp = new VerifyOtp();
                verifyOtp.setArguments(bundle);
                replaceFragment(verifyOtp);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(fAuth)
                        .setPhoneNumber("+91" + phone1)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity) getContext())                // Activity (for callback binding)
                        .setCallbacks(fCallback)          // OnVerificationStateChangedCallbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void setLayoutAnimation(){

        enter_mobile_frag.setTranslationY(900);
        enter_mobile_frag.setAlpha(v);

        enter_mobile_frag.animate().translationY(0).alpha(1).setDuration(900).setStartDelay(0).start();
    }
   private void replaceFragment(Fragment fragment){


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_main_layout, fragment);
        fragmentTransaction.commit();
    }

}