package com.example.tutionplus.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tutionplus.Fragments.MobileAuth;
import com.example.tutionplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup extends Fragment {


    View view;
    FirebaseAuth fAuth;
    Button signup_submit_btn;
    TextInputEditText signup_email, signup_password;
    TextInputLayout signup_email_layout, signup_password_layout;
    TextView signup_phone_text;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup, container, false);

        signup_email = view.findViewById(R.id.signup_email);
        signup_password = view.findViewById(R.id.signup_password);
        signup_submit_btn = view.findViewById(R.id.signup_submit_btn);

        signup_email_layout = view.findViewById(R.id.text_input_layout_signup_email);
        signup_password_layout = view.findViewById(R.id.text_input_layout_signup_password);

        signup_phone_text = view.findViewById(R.id.signup_phone_text);

        signup_phone_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //replaceFragment(new MobileAuth());
            }
        });

        fAuth = FirebaseAuth.getInstance();

        signup_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();


                if(password.length()>=6){
                    signup_password_layout.setHelperText("Strong Password");
                }
                else{
                    signup_password_layout.setError("Minimum 6 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signup_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = signup_email.getText().toString();
                String password = signup_password.getText().toString();

                if(email.isEmpty()){
                    signup_email_layout.setError("Fill this first");
                    return;
                }


                if(password.isEmpty()){
                    signup_password_layout.setError("Fill this first");
                    return;
                }


                fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
//                        Context context = getContext();
//                        CharSequence text = "Registered Successfully";
//                        int duration = Toast.LENGTH_SHORT;
//                        Toast myToast = Toast.makeText(context, text, duration);
//                        myToast.show();

                         fAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 Context context = getContext();
                                 CharSequence text = "Verification Link has been sent to your email";
                                 int duration = Toast.LENGTH_SHORT;
                                 Toast myToast = Toast.makeText(context, text, duration);
                                 myToast.show();

                                 saveData(email,password);

                                 //replaceFragment(new Login());
                             }
                         }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Context context = getContext();
                                 CharSequence text = e.getMessage();
                                 int duration = Toast.LENGTH_SHORT;
                                 Toast myToast = Toast.makeText(context, text, duration);
                                 myToast.show();
                             }
                         });

//                        Intent intent = new Intent(getContext(), Dashboard.class);
//                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Context context = getContext();
                        CharSequence text = e.getMessage();
                        int duration = Toast.LENGTH_SHORT;
                        Toast myToast = Toast.makeText(context, text, duration);
                        myToast.show();

                        signup_email.getText().clear();
                        signup_password.getText().clear();
                    }
                });
            }
        });

        return view;
    }

    /*private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }*/

    private void saveData(String email, String password){

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://my-nodejs-app-server.herokuapp.com/user/register";

        Map<String, String> params = new HashMap<>();

        params.put("email",email);
        params.put("password",password);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("response", response.getString("Message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.getLocalizedMessage());
            }
        });

        queue.add(stringRequest);
    }


}
