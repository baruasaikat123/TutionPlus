package com.example.tutionplus.ProjectActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tutionplus.Fragments.WelcomeScreen;
import com.example.tutionplus.ProjectClass.GetEmoji;
import com.example.tutionplus.ProjectClass.ItemViewModel;
import com.example.tutionplus.ProjectClass.StatusBar;
import com.example.tutionplus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    LottieAnimationView lottie_enter_mobile;
    TextView otp_text, or_text_view;
    FloatingActionButton google_btn, facebook_btn, twitter_btn;
    private float v = 0;
    private ItemViewModel viewModel;

    private int count = 0, user_type_count = 0, animation_id;
    GetEmoji emojiObj = new GetEmoji();
    private StatusBar statusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setStatusBar();
        statusBar = new StatusBar();
        statusBar.getStatusBarColor(this.getWindow(),getApplicationContext(),R.color.app_light);


        lottie_enter_mobile = findViewById(R.id.lottie_mobile_otp);
        otp_text = findViewById(R.id.otp_text);
        google_btn = findViewById(R.id.google_btn);
        facebook_btn = findViewById(R.id.facebook_btn);
        twitter_btn = findViewById(R.id.twitter_btn);
        or_text_view = findViewById(R.id.or_text_view);

        animation_id = R.raw.welcome_animation;
        setLottieAnimation();


        String emoji = emojiObj.getEmoji(0x1F603);

        setTextAnimation("Welcome "+emoji+" \n Please select a category to continue.");
        //otp_text.setText("Welcome "+emoji+" \n Please select a category to continue.");

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lottie_enter_mobile.getLayoutParams();
        params.topMargin = 80;

        setViewModel();

        fAuth = FirebaseAuth.getInstance();

        replaceFragment(new WelcomeScreen());

    }

    /*private void setStatusBar() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = this.getWindow();
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.app_light));
        }
    }*/

    private void setViewModel(){

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        viewModel.getAnimationId().observe(this, (item) -> {

            animation_id = item;
            lottie_enter_mobile.animate().translationX(1000).setDuration(1000).setStartDelay(0).start();

            if(count==0){

                setButtonAnimation();
                count++;
            }

            if(item == R.raw.otp_processing){

                google_btn.setEnabled(false);
                facebook_btn.setEnabled(false);
                twitter_btn.setEnabled(false);

                count++;
            }

            if(item == R.raw.otp_sent){

                google_btn.setEnabled(true);
                facebook_btn.setEnabled(true);
                twitter_btn.setEnabled(true);

                count++;
            }

            google_btn.setVisibility(View.VISIBLE);
            facebook_btn.setVisibility(View.VISIBLE);
            twitter_btn.setVisibility(View.VISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    setLottieAnimation();
                    adjustAnimation();
                    adjustText();
                }
            }, 500);

        });

        viewModel.getOtpText().observe(this, item -> {

                if(item == getResources().getString(R.string.student)){

                    String emoji = emojiObj.getEmoji(0x1F393);
                    otp_text.setText(item +" " +emoji);
                }

                else if(item == getResources().getString(R.string.teacher)){

                    otp_text.setText(item);
                }
                else{

                    otp_text.animate().translationX(1000).setDuration(1000).setStartDelay(0).start();

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            setTextAnimation(item);
                        }
                    }, 500);

                    /*handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //if(item == getResources().getString(R.string.otp_send_error)) lottie_enter_mobile.setVisibility(View.GONE);
                            setTextAnimation(item);
                        }
                    },500);*/
                }
            //}
        });

    }

    private void setButtonAnimation(){

        google_btn.setTranslationX(-500);
        facebook_btn.setTranslationY(800);
        twitter_btn.setTranslationX(500);

        google_btn.setAlpha(v);
        facebook_btn.setAlpha(v);
        twitter_btn.setAlpha(v);

        google_btn.animate().translationX(0).alpha(1).setDuration(1200).setStartDelay(1).start();
        facebook_btn.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(1).start();
        twitter_btn.animate().translationX(0).alpha(1).setDuration(1200).setStartDelay(1).start();

        or_text_view.setTranslationX(-500);
        or_text_view.setAlpha(v);
        or_text_view.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(0).start();
        or_text_view.setVisibility(View.VISIBLE);
    }

    private void setTextAnimation(String text){

        otp_text.setTranslationX(-500);
        otp_text.setAlpha(v);
        otp_text.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(0).start();
        otp_text.setText(text);
    }

    private void setLottieAnimation(){

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lottie_enter_mobile.getLayoutParams();
        params.topMargin = 0;

        lottie_enter_mobile.setTranslationX(-500);
        lottie_enter_mobile.setAlpha(v);
        lottie_enter_mobile.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(0).start();
        lottie_enter_mobile.setAnimation(animation_id);

        lottie_enter_mobile.playAnimation();
    }

    private void adjustText(){

        if(animation_id==R.raw.mobile_animate) {
            otp_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.app_red));
        }
        else if(animation_id==R.raw.otp_processing) {
            otp_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.app_blue));
        }
        else if(animation_id==R.raw.otp_sent) {
            otp_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.app_green));
        }
    }
    private void adjustAnimation(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if(animation_id==R.raw.mobile_animate) {
                lottie_enter_mobile.setTooltipText(getResources().getString(R.string.enter_mobile_tooltip_text));
            }
            else if(animation_id==R.raw.otp_processing) {
                lottie_enter_mobile.setTooltipText(getResources().getString(R.string.otp_processing_tooltip_text));
            }
            else if(animation_id==R.raw.otp_sent) {
                lottie_enter_mobile.setTooltipText(getResources().getString(R.string.otp_sent_tooltip_text));
            }
        }
    }


    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_main_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = fAuth.getCurrentUser();

        if(currentUser!=null){
            Intent intent = new Intent(MainActivity.this, Dashboard.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();

    }
}