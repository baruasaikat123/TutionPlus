package com.example.tutionplus.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutionplus.R;
import com.example.tutionplus.ProjectActivity.TeacherInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends Fragment {

    View view;
    FirebaseAuth fAuth;
    TextInputEditText login_email, login_password;
    TextInputLayout login_email_layout, login_password_layout;
    TextView email_verify_text;
    Button login_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        fAuth = FirebaseAuth.getInstance();

        login_email = view.findViewById(R.id.login_email);
        login_password = view.findViewById(R.id.login_password);
        login_btn = view.findViewById(R.id.login_submit_btn);
        login_email_layout = view.findViewById(R.id.text_input_layout_login_email);
        login_password_layout = view.findViewById(R.id.text_input_layout_login_password);
        email_verify_text = view.findViewById(R.id.email_verify_text);

        login_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = login_email.getText().toString();
                String password = login_password.getText().toString();

                if (email.isEmpty()) {
                    login_email_layout.setError("Fill this first");
                    return;
                }

                if (password.isEmpty()) {
                    login_password_layout.setError("Fill this first");
                    return;
                }


                fAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        if (!fAuth.getCurrentUser().isEmailVerified()) {

                            email_verify_text.setVisibility(View.VISIBLE);

                            Context context = getContext();
                            CharSequence text = "Please verify your email first";
                            int duration = Toast.LENGTH_SHORT;
                            Toast myToast = Toast.makeText(context, text, duration);
                            myToast.show();
                            return;
                        }

                        Context context = getContext();
                        CharSequence text = "Logged in successful";
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, text, duration).show();

                        Intent intent = new Intent(getContext(), TeacherInfo.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Context context = getContext();
                        CharSequence text = e.getMessage();
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, text, duration).show();

                        login_email.getText().clear();
                        login_password.getText().clear();

                    }
                });

            }
        });

        return view;
    }


}