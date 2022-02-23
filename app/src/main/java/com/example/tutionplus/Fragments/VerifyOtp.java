package com.example.tutionplus.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.Telephony;
import android.telephony.TelephonyCallback;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutionplus.ProjectActivity.TeacherInfo;
import com.example.tutionplus.ProjectClass.GetEmoji;
import com.example.tutionplus.ProjectClass.ItemViewModel;
import com.example.tutionplus.ProjectClass.SmsBroadcastReceiver;
import com.example.tutionplus.R;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class VerifyOtp extends Fragment {

    View view;
    TextInputEditText get_otp_no_1, get_otp_no_2, get_otp_no_3, get_otp_no_4, get_otp_no_5, get_otp_no_6;
    TextInputLayout get_otp_no_1_layout, get_otp_no_2_layout, get_otp_no_3_layout, get_otp_no_4_layout, get_otp_no_5_layout, get_otp_no_6_layout;
    Button verify_otp_btn;
    private ItemViewModel viewModel;
    private float v = 0;
    FrameLayout verify_otp_frag;
    TextView verify_otp_mobile_no_text;
    FirebaseAuth fAuth;
    ProgressBar verify_otp_loading;
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            /*if(getTargetRequestCode() == REQ_USER_CONSENT){

                if((result.getResultCode()==RESULT_OK) && (result.getData()!=null)){

                    String message = result.getData().getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    getOtpFromMessage(message);
                }
            }*/
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_verify_otp, container, false);

        get_otp_no_1 = view.findViewById(R.id.otp_no_1);
        get_otp_no_2 = view.findViewById(R.id.otp_no_2);
        get_otp_no_3 = view.findViewById(R.id.otp_no_3);
        get_otp_no_4 = view.findViewById(R.id.otp_no_4);
        get_otp_no_5 = view.findViewById(R.id.otp_no_5);
        get_otp_no_6 = view.findViewById(R.id.otp_no_6);

        get_otp_no_1_layout = view.findViewById(R.id.otp_no_1_layout);
        get_otp_no_2_layout = view.findViewById(R.id.otp_no_2_layout);
        get_otp_no_3_layout = view.findViewById(R.id.otp_no_3_layout);
        get_otp_no_4_layout = view.findViewById(R.id.otp_no_4_layout);
        get_otp_no_5_layout = view.findViewById(R.id.otp_no_5_layout);
        get_otp_no_6_layout = view.findViewById(R.id.otp_no_6_layout);

        verify_otp_btn = view.findViewById(R.id.verify_otp_btn);
        verify_otp_frag = view.findViewById(R.id.verify_otp_frag);
        verify_otp_mobile_no_text = view.findViewById(R.id.verify_otp_mobile_no_text);
        verify_otp_loading = view.findViewById(R.id.verify_otp_loading);

        startSmartUserConsent();

        editTextFocus();

        setLayoutAnimation();

        Bundle bundle = this.getArguments();
        String user_type = bundle.getString("userType");
        String get_mobile_no = bundle.getString("mobileNo");
        String verificationId = bundle.getString("verificationId");

        verify_otp_mobile_no_text.setText("+91 "+get_mobile_no);

        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        viewModel.setCustomText(getResources().getString(R.string.otp_sent_text));
        viewModel.setAnimation(R.raw.otp_sent);

        fAuth = FirebaseAuth.getInstance();

        verify_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String otp1 = get_otp_no_1.getText().toString().trim();
                String otp2 = get_otp_no_2.getText().toString().trim();
                String otp3 = get_otp_no_3.getText().toString().trim();
                String otp4 = get_otp_no_4.getText().toString().trim();
                String otp5 = get_otp_no_5.getText().toString().trim();
                String otp6 = get_otp_no_6.getText().toString().trim();

                if(otp1.isEmpty() || otp2.isEmpty() || otp3.isEmpty() || otp4.isEmpty() ||
                     otp5.isEmpty() || otp6.isEmpty()){

                    Toast.makeText(getContext(), "Please fill all six digit OTP", Toast.LENGTH_SHORT).show();
                    return;
                }

                verify_otp_loading.setVisibility(View.VISIBLE);
                verify_otp_btn.setVisibility(View.GONE);

                String user_input_otp = get_otp_no_1.getText().toString().trim() +
                        get_otp_no_2.getText().toString().trim() +
                        get_otp_no_3.getText().toString().trim() +
                        get_otp_no_4.getText().toString().trim() +
                        get_otp_no_5.getText().toString().trim() +
                        get_otp_no_6.getText().toString().trim();

                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, user_input_otp);
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }
        });


        return view;
    }

    private void startSmartUserConsent() {

        SmsRetrieverClient client = SmsRetriever.getClient(requireActivity());
        client.startSmsUserConsent(null);
    }

    private void registerBroadcastReceiver(){

        smsBroadcastReceiver = new SmsBroadcastReceiver();

        smsBroadcastReceiver.smsBroadcastReceiverListener = new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {
                startForResult.launch(intent);
            }

            @Override
            public void onFailure() {
//                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        };

//        smsBroadcastReceiver.smsBroadcastReceiverLister = new SmsBroadcastReceiver.SmsBroadcastReceiverLister() {
//            @Override
//            public void onSuccess(Intent intent) {
//                //startActivityForResult(intent, REQ_USER_CONSENT);
//                startForResult.launch(intent);
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        };

        /*smsBroadcastReceiver.initListener(new SmsBroadcastReceiver.SmsBroadcastReceiverLister() {
            @Override
            public void onSuccess(String otp) {

                int otp1 = Character.getNumericValue(otp.charAt(0));
                int otp2 = Character.getNumericValue(otp.charAt(1));
                int otp3 = Character.getNumericValue(otp.charAt(2));
                int otp4 = Character.getNumericValue(otp.charAt(3));
                int otp5 = Character.getNumericValue(otp.charAt(4));
                int otp6 = Character.getNumericValue(otp.charAt(5));

                get_otp_no_1.setText(String.valueOf(otp1));
                get_otp_no_2.setText(String.valueOf(otp2));
                get_otp_no_3.setText(String.valueOf(otp3));
                get_otp_no_4.setText(String.valueOf(otp4));
                get_otp_no_5.setText(String.valueOf(otp5));
                get_otp_no_6.setText(String.valueOf(otp6));

            }

            @Override
            public void onFailure() {

                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });*/

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        requireActivity().registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    //@Override
    /*public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_USER_CONSENT){

            if((requestCode == RESULT_OK) && (data!=null)){

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);
            }
        }
    }*/

    private void getOtpFromMessage(String message) {

        Pattern otpPattern = Pattern.compile("\\d{6}");
        Matcher matcher = otpPattern.matcher(message);

        if(matcher.find()){

            String otp = matcher.group(0);

            int otp1 = Character.getNumericValue(otp.charAt(0));
            int otp2 = Character.getNumericValue(otp.charAt(1));
            int otp3 = Character.getNumericValue(otp.charAt(2));
            int otp4 = Character.getNumericValue(otp.charAt(3));
            int otp5 = Character.getNumericValue(otp.charAt(4));
            int otp6 = Character.getNumericValue(otp.charAt(5));

            get_otp_no_1.setText(String.valueOf(otp1));
            get_otp_no_2.setText(String.valueOf(otp2));
            get_otp_no_3.setText(String.valueOf(otp3));
            get_otp_no_4.setText(String.valueOf(otp4));
            get_otp_no_5.setText(String.valueOf(otp5));
            get_otp_no_6.setText(String.valueOf(otp6));

        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        verify_otp_btn.setVisibility(View.VISIBLE);
                        verify_otp_loading.setVisibility(View.GONE);

                        if (task.isSuccessful()) {

                            //FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(getContext(), TeacherInfo.class);
                            startActivity(intent);

                        } else {

                            verify_otp_loading.setVisibility(View.GONE);
                            verify_otp_btn.setVisibility(View.VISIBLE);
                            get_otp_no_1.setText("");
                            get_otp_no_2.setText("");
                            get_otp_no_3.setText("");
                            get_otp_no_4.setText("");
                            get_otp_no_5.setText("");
                            get_otp_no_6.setText("");

                            GetEmoji emojiObj = new GetEmoji();
                            String emoji = emojiObj.getEmoji(0x2639);

                            viewModel.setCustomText("Verification failed " + emoji + "\n Please check again...");
                            //Toast.makeText(getContext(),"Verification failed" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void editTextFocus(){

        get_otp_no_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                get_otp_no_2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        get_otp_no_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                get_otp_no_3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        get_otp_no_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                get_otp_no_4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        get_otp_no_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                get_otp_no_5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        get_otp_no_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                get_otp_no_6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setLayoutAnimation(){
        verify_otp_frag.setTranslationY(900);
        verify_otp_frag.setAlpha(v);

        verify_otp_frag.animate().translationY(0).alpha(1).setDuration(900).setStartDelay(0).start();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    public void onStop() {
        if(smsBroadcastReceiver!=null)
            requireActivity().unregisterReceiver(smsBroadcastReceiver);

        super.onStop();
    }
}