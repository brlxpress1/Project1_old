package com.tur.job1.job_seeker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.tur.job1.Intro;
import com.tur.job1.R;
import com.tur.job1.others.Connectivity;


import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class Job_Seeker_Verify_1 extends AppCompatActivity {

    private String TAG = "Job_Seeker_Verify_1";
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);



    private Button next;
    private EditText name;
    private TextView countryCode;
    private EditText phone_number;
    private LinearLayout countryCodePanel;


    private CountryCodePicker ccp;
    private Dialog dialog;



    //public String otpID;
    public static String userName;
    public static   String userPhone;
    public  static  String userPurePhone;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_verify_1);


        next = (Button)findViewById(R.id.next_button);
        name = (EditText)findViewById(R.id.name_input);
        countryCode = (TextView)findViewById(R.id.country_code_input);
        phone_number = (EditText)findViewById(R.id.phone_number_input);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        countryCodePanel = (LinearLayout)findViewById(R.id.country_code_panel);




        //-- Changing selection effect of input fields
        name.setOnFocusChangeListener((view, b) -> {
            if (view.isFocused()) {
                // Do whatever you want when the EditText is focused
                // Example:
                //editTextFrom.setText("Focused!");
                name.setBackgroundResource(R.drawable.edittext_box_border);
            }
            else{

                name.setBackgroundResource(R.drawable.shape_with_border1);

            }
        });



        phone_number.setOnFocusChangeListener((view, b) -> {
            if (view.isFocused()) {
                // Do whatever you want when the EditText is focused
                // Example:
                //editTextFrom.setText("Focused!");
                phone_number.setBackgroundResource(R.drawable.edittext_box_border);
            }
            else{
                phone_number.setBackgroundResource(R.drawable.shape_with_border1);
            }
        });



        //-----------------



       // showLoadingBarAlert();
        if(userName != null && !userName.equalsIgnoreCase("")){

            name.setText(userName);

        }

        if(userPurePhone != null && !userPurePhone.equalsIgnoreCase("")){

            phone_number.setText(userPurePhone);

        }

        countryCodePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countryCodePanel.startAnimation(buttonClick);

                ccp.launchCountrySelectionDialog();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                next.startAnimation(buttonClick);

                //-- Check if user is connected with the internet
                if (Connectivity.isConnected(Job_Seeker_Verify_1.this)) {


                    //-- Check if name input field is empty
                    String temp1 = name.getText().toString().trim();
                    if(temp1.equalsIgnoreCase("") || temp1 == null){

                        Toasty.error(Job_Seeker_Verify_1.this, "Please enter your name!", Toast.LENGTH_LONG, true).show();
                    }
                    else {

                        //-- Check if phone number input field is empty
                        String temp2 = phone_number.getText().toString().trim();
                        if(temp2.equalsIgnoreCase("") || temp2 == null){

                            Toasty.error(Job_Seeker_Verify_1.this, "Please enter your phone number!", Toast.LENGTH_LONG, true).show();
                        }else {


                            String tempName = name.getText().toString();
                            //String tempPhone = ccp.getSelectedCountryCodeWithPlus()+phone_number.getText().toString().trim();
                            String tempPhone = countryCode.getText().toString().trim()+phone_number.getText().toString().trim();

                            //Toasty.success(Job_Seeker_Verify_1.this, "Name : "+tempName+"\nPhone : "+tempPhone, Toast.LENGTH_LONG, true).show();

                            //showLoadingBarAlert();
                            //-- task
                            userName = tempName;
                            userPhone = tempPhone;
                            userPurePhone = phone_number.getText().toString().trim();

                            Intent openSecondVerifier = new Intent(Job_Seeker_Verify_1.this,Job_Seeker_Verify_2.class);
                            startActivity(openSecondVerifier);
                            finish();
                        }

                    }


                } else {


                    Toasty.error(Job_Seeker_Verify_1.this, "You have no internet access! Please turn on your WiFi or mobile data.", Toast.LENGTH_LONG, true).show();

                }


            }
        });

        //--

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {

            @Override

            public void onCountrySelected() {

                ccp.startAnimation(buttonClick);
                countryCode.setText(ccp.getSelectedCountryCodeWithPlus().toString().trim());



            }

        });

        //-----------------


    }

    private void showLoadingBarAlert(){


        dialog = new Dialog(Job_Seeker_Verify_1.this);

        dialog.setContentView(R.layout.loading);

        dialog.setTitle("Please wait!");

        dialog.setCancelable(false);



        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        dialog.show();

    }



    private void hideLoadingBar(){



        dialog.dismiss();

    }


    @Override
    public void onBackPressed() {



        Intent introOpener = new Intent(Job_Seeker_Verify_1.this,Intro.class);
        startActivity(introOpener);
        finish();

    }



}
