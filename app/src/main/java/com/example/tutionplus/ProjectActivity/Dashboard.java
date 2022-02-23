package com.example.tutionplus.ProjectActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tutionplus.ProjectAdapter.RecyclerAdapter;
import com.example.tutionplus.ProjectAdapter.RecyclerTeacherAdapter;
import com.example.tutionplus.ProjectClass.StatusBar;
import com.example.tutionplus.ProjectClass.Teacher;
import com.example.tutionplus.ProjectClass.User;
import com.example.tutionplus.R;
import com.example.tutionplus.Interfaces.RecyclerViewClickInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity{

    //Button logout_btn;
    //Button continue_search_btn;
    FirebaseAuth fAuth;
    RecyclerView recyclerView;
    //RecyclerAdapter recyclerAdapter;
    RecyclerTeacherAdapter recyclerTeacherAdapter;
    private ArrayList<Teacher> teacherList;
    private Teacher teacher;
    //private StatusBar statusBar;
    //private String[] subjectList;
    //private Boolean[] checkSelectedSubject;
    //private ArrayList<String> selectedSubjects = new ArrayList<>();
    FirebaseFirestore fStore;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //logout_btn = findViewById(R.id.logout_btn);
        binding();

        //Status Bar color
        new StatusBar().getStatusBarColor(this.getWindow(),getApplicationContext(),R.color.app_light);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        /*String userId = "bR5fnOn848TEaptSMf2D8RvBSL92";
        DocumentReference documentReference = fStore.collection("Teachers").document(userId);

        Map<String, String> params = new HashMap<>();

        params.put("name", "Saikat Barua");
        params.put("mobile", "8597430895");
        params.put("location","cooch behar");
        params.put("gender","M");
        params.put("subject","Mathematics, Physics");
        params.put("pattern","Any Kind");
        params.put("mode","Both Online & Offline");
        params.put("status","NEW");
        params.put("time","Any time");
        params.put("fees","1000");

        documentReference.set(params).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("teacher","Successfully created.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", e.getLocalizedMessage().toString());
            }
        });*/



        //saveTeacher();

        //String name, String mobile, String location, String gender, String subject,
        //                             String pattern, String mode, String status, String time, String fees

        teacherList = new ArrayList<>();

        for(int i= 0; i < 10 ; i++){
            teacher = new Teacher();
            teacher.setUserName("Saikat Barua");
            teacher.setLocation("1 No. Kalighat Road, Neheru Nagar Colony, Cooch Behar, West Bengal");
            teacher.setStatus("5+ years of\n experience");
            //teacher.setStatus("NEW");

            teacherList.add(teacher);
        }



        recyclerTeacherAdapter = new RecyclerTeacherAdapter(teacherList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerTeacherAdapter);

        //divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.icon_color)));
        recyclerView.addItemDecoration(dividerItemDecoration);



        /*subjectList = getResources().getStringArray(R.array.subject);
        checkSelectedSubject = new Boolean[subjectList.length];
        Arrays.fill(checkSelectedSubject, false);

        fAuth = FirebaseAuth.getInstance();

        recyclerAdapter = new RecyclerAdapter(subjectList,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(recyclerAdapter);*/


        /*continue_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!selectedSubjects.isEmpty()){
                    Log.d("subject", selectedSubjects.get(0));
                    Log.d("size", String.valueOf(selectedSubjects.size()));
                }
            }
        });*/


        //FirebaseUser currentUser = fAuth.getCurrentUser();



        /*logout_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                fAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
    }

    private void binding(){

        //continue_search_btn = (Button) findViewById(R.id.continue_search_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //card_subject_list = (CardView) findViewById(R.id.card_subject_list);
        //subject_name = (TextView) findViewById(R.id.subject_name);
    }

    /*private void saveTeacher(){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://my-nodejs-app-server.herokuapp.com/user/teacher/register";

        Map<String, String> params = new HashMap<>();

        params.put("name", "Saikat Barua");
        params.put("mobile", "8597430895");
        params.put("location","cooch behar");
        params.put("gender","M");
        params.put("subject","Mathematics, Physics");
        params.put("pattern","Any Kind");
        params.put("mode","Both Online & Offline");
        params.put("status","NEW");
        params.put("time","Any time");
        params.put("fees","1000");


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
                Log.d("error","" + error.getLocalizedMessage());
            }
        });

        queue.add(stringRequest);
    }*

    //@Override
    /*public void onItemClick(int position, CardView card_subject_list, TextView subject_name) {

        Toast.makeText(this, subjectList[position], Toast.LENGTH_SHORT).show();

        if(checkSelectedSubject[position] == true){

            checkSelectedSubject[position] = false;
            int index = selectedSubjects.indexOf(subjectList[position]);
            if(!selectedSubjects.isEmpty()) selectedSubjects.remove(index);
            card_subject_list.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
            subject_name.setTextColor(ContextCompat.getColor(this, R.color.app_green));
        }

        else {

            checkSelectedSubject[position] = true;
            selectedSubjects.add(subjectList[position]);
            card_subject_list.setCardBackgroundColor(ContextCompat.getColor(this, R.color.radio_select));
            subject_name.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }*/


//    @Override
//    public void onBackPressed() {
//        count++;
//
//        if(count==2){
//            super.onBackPressed();
//            finish();
//        }
//        else{
//            Context context = getApplicationContext();
//            CharSequence text = "Press back again to exit";
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast.makeText(context,text, duration).show();
//        }
//    }
}