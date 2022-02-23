package com.example.tutionplus.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tutionplus.ProjectClass.ItemViewModel;
import com.example.tutionplus.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LocationDetail extends Fragment {

    private ArrayList<String> states = new ArrayList<>();
    private ArrayList<String> cities = new ArrayList<>();
    AutoCompleteTextView dropdown_state_text, dropdown_city_text;
    TextInputEditText teacher_location;
    View view;
    Button next_btn_location;
    private Map<String, String> stateDetails = new HashMap<>();
    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;
    private String selectedState=null, selectedCity=null;
    private ItemViewModel viewModel;
    private static final int step = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_location_detail, container, false);

        //binding
        binding();

        //load states
        loadState();

        //load cities
        loadCities();

        //sharedPreferences
        sharedPreferences = getContext().getSharedPreferences("teacherInfo", Context.MODE_PRIVATE);

        //view model
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        //get state
        getState();

        //select state
        dropdown_state_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedState = adapterView.getItemAtPosition(i).toString();
            }
        });

        //select city
        dropdown_city_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCity = adapterView.getItemAtPosition(i).toString();
            }
        });

        next_btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String address = teacher_location.getText().toString();

                if(address.isEmpty()){
                    //Toast.makeText(getContext(), "Please fill your address", Toast.LENGTH_SHORT).show();
                    viewModel.setCustomText("Please fill your address");
                    return;
                }

                if(selectedState==null){
                    //Toast.makeText(getContext(), "Please select your state", Toast.LENGTH_SHORT).show();
                    viewModel.setCustomText("Please select your state");
                    return;
                }

                if(selectedCity==null){
                    //Toast.makeText(getContext(), "Please select your city", Toast.LENGTH_SHORT).show();
                    viewModel.setCustomText("Please select your city");
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
        dropdown_state_text =  view.findViewById(R.id.dropdown_state_text);
        dropdown_city_text =  view.findViewById(R.id.dropdown_city_text);
        teacher_location = view.findViewById(R.id.teacher_location);
        next_btn_location = view.findViewById(R.id.next_btn_location);
    }

    private void setState(){
        editor = sharedPreferences.edit();
        editor.putString("location",teacher_location.getText().toString());
        editor.putString("state", selectedState);
        editor.putString("city",selectedCity);
        editor.apply();
    }

    private void getState(){
        String location = sharedPreferences.getString("location",null);
        selectedState = sharedPreferences.getString("state",null);
        selectedCity = sharedPreferences.getString("city",null);

        if(location!=null){
            teacher_location.setText(location);
        }
        if(selectedState!=null){
            dropdown_state_text.setText(selectedState);
        }
        if(selectedCity!=null){
            dropdown_city_text.setText(selectedCity);
        }
    }

    private void loadState(){

        String url = "https://api.countrystatecity.in/v1/countries/IN/states/";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for(int i = 0;i<response.length();i++) {
                        JSONObject state = response.getJSONObject(i);

                        //Log.d("data",state.getString("name"));
                        String state_name = (String) state.getString("name");
                        String state_code = (String) state.getString("iso2");
                        states.add(state_name);
                        stateDetails.put((String)state_name, state_code);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.getLocalizedMessage());
                //Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<String, String>();
                headers.put("X-CSCAPI-KEY", "OHA4MXJnWEJQalBFRkIzVkNoUjV2VWxtZ2dOYkJwR082Ym44N20ySA==");

                return headers;
            }

        };

        queue.add(jsonArrayRequest);

    }

    private void loadCities(){

        String url = "https://api.countrystatecity.in/v1/countries/IN/states/WB/cities";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for(int i = 0;i<response.length();i++) {
                        JSONObject city = response.getJSONObject(i);

                        //Log.d("data",state.getString("name"));
                        String city_name = (String) city.getString("name");
                        cities.add( city_name);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("error",error.getLocalizedMessage());
                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<String, String>();
                headers.put("X-CSCAPI-KEY", "OHA4MXJnWEJQalBFRkIzVkNoUjV2VWxtZ2dOYkJwR082Ym44N20ySA==");

                return headers;
            }

        };

        queue.add(jsonArrayRequest);

    }

   @Override
    public void onResume() {
        super.onResume();

        ArrayAdapter<String> stateNamesAdapterItems = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, states);
        dropdown_state_text.setAdapter(stateNamesAdapterItems);

       ArrayAdapter<String> cityNamesAdapterItems = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, cities);
       dropdown_city_text.setAdapter(cityNamesAdapterItems);
    }
}