package com.tur.job1.job_seeker;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.tur.job1.Intro;
import com.tur.job1.R;
import com.tur.job1.others.Connectivity;
import com.tur.job1.others.ConstantsHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class Job_Seeker_Verify_2 extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);
    private String TAG = "Job_Seeker_Verify_2";


    private Dialog dialog;
    private EditText otpCodeInput;
    private Button verifyButton;
    private TextView resendButton;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            verificationCallbacks;
    private PhoneAuthCredential credential;

    private String otpID;
    private FirebaseAuth fbAuth;

    private String userNameLocal;
    private String userPhoneLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_verify_2);


        otpCodeInput = (EditText)findViewById(R.id.otp_code_input);
        verifyButton = (Button)findViewById(R.id.verify_button);
        resendButton = (TextView)findViewById(R.id.resend_button);


        //-- Changing selection effect of input fields
        otpCodeInput.setOnFocusChangeListener((view, b) -> {
            if (view.isFocused()) {
                // Do whatever you want when the EditText is focused
                // Example:
                //editTextFrom.setText("Focused!");
                otpCodeInput.setBackgroundResource(R.drawable.edittext_box_border);
            }
            else{
                otpCodeInput.setBackgroundResource(R.drawable.shape_with_border1);
            }
        });



        //-----------------


        Job_Seeker_Verify_1 jsv = new  Job_Seeker_Verify_1();
        userNameLocal = jsv.userName;
        userPhoneLocal = jsv.userPhone;

        fbAuth = FirebaseAuth.getInstance();

        showLoadingBarAlert();
        send_otp_by_firebase(userPhoneLocal);

        resendButton.setText("Did not get the OTP code? "+ Html.fromHtml("<p><u>Resend</u></p>"));

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyButton.startAnimation(buttonClick);

                if (Connectivity.isConnected(Job_Seeker_Verify_2.this)) {


                    verifyCode();

                } else {

                    Toasty.error(Job_Seeker_Verify_2.this, "You have no internet access! Please turn on your WiFi or mobile data.", Toast.LENGTH_LONG, true).show();

                }

            }
        });

        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendButton.startAnimation(buttonClick);

                if (Connectivity.isConnected(Job_Seeker_Verify_2.this)) {


                    resendCode();

                } else {

                    Toasty.error(Job_Seeker_Verify_2.this, "You have no internet access! Please turn on your WiFi or mobile data.", Toast.LENGTH_LONG, true).show();

                }

            }
        });




    }

    private void showLoadingBarAlert(){


        dialog = new Dialog(Job_Seeker_Verify_2.this);

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

    //-- OTP related task

    //--

    private void send_otp_by_firebase(String number){



        verificationCallBack();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                number,        // Phone number to verify

                60,                 // Timeout duration

                TimeUnit.SECONDS,   // Unit of timeout

                this,               // Activity (for callback binding)

                verificationCallbacks);





        //--





    }



    private void verificationCallBack(){



        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {



            @Override

            public void onVerificationCompleted(PhoneAuthCredential credential) {

                // This callback will be invoked in two situations:

                // 1 - Instant verification. In some cases the phone number can be instantly

                //     verified without needing to send or enter a verification code.

                // 2 - Auto-retrieval. On some devices Google Play services can automatically

                //     detect the incoming verification SMS and perform verification without

                //     user action.

                Log.d(TAG, "onVerificationCompleted:" + credential);



                //credential = PhoneAuthProvider.getCredential(verificationId, code);

                signInWithPhoneAuthCredential(credential);

            }



            @Override

            public void onVerificationFailed(FirebaseException e) {

                // This callback is invoked in an invalid request for verification is made,

                // for instance if the the phone number format is not valid.

                Log.w(TAG, "onVerificationFailed", e);



                //Toast.makeText(Verification.this, "Enter Correct Number.", Toast.LENGTH_SHORT).show();

                Toasty.error(Job_Seeker_Verify_2.this, "Enter a valid phone number!", Toast.LENGTH_LONG, true).show();

                Intent openSecondVerifier = new Intent(Job_Seeker_Verify_2.this,Job_Seeker_Verify_1.class);
                startActivity(openSecondVerifier);
                finish();



                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    // Invalid request

                    // ...

                } else if (e instanceof FirebaseTooManyRequestsException) {

                    // The SMS quota for the project has been exceeded

                    // ...

                }



                // Show a message and update the UI

                // ...

            }



            @Override
            public void onCodeSent(String verificationId,

                                   PhoneAuthProvider.ForceResendingToken token) {

                // The SMS verification code has been sent to the provided phone number, we

                // now need to ask the user to enter the code and then construct a credential

                // by combining the code with a verification ID.

                Log.d(TAG, "onCodeSent:" + verificationId);



                // Save verification ID and resending token so we can use them later



                Log.d(TAG,verificationId+"------------"+token);

                otpID = verificationId;

                Toasty.info(Job_Seeker_Verify_2.this, "OTP code sent to your phone number, please enter the code.", Toast.LENGTH_LONG, true).show();
                hideLoadingBar();






                // ...

            }

        };

    }






    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        fbAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");



                            //FirebaseUser user = task.getResult().getUser();

                            //String temp = wholeNumber;

                            //Toast.makeText(Verification.this, "Log in successful! with the phone "+ temp, Toast.LENGTH_LONG).show();

                            // Toasty.success(JobSeekerSignUp_A.this, "Log in successful!", Toast.LENGTH_LONG, true).show();

                            // after successfully verified
/*
                            hideLoadingBar();
                            //Toasty.success(Job_Seeker_Verify_2.this,"Sign up successful with you provided phone number!",Toast.LENGTH_LONG, true).show();
                            Intent openCVwindow = new Intent(Job_Seeker_Verify_2.this,Job_Seeker_CV_Upload.class);
                            startActivity(openCVwindow);
                            finish();
                            //--
                            */

                            registerUser(userNameLocal,userPhoneLocal);





                        } else {

                            // Sign in failed, display a message and update the UI

                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                // The verification code entered was invalid

                                //Toast.makeText(Verification.this, "Verification code is invalid!", Toast.LENGTH_LONG).show();

                                Toasty.error(Job_Seeker_Verify_2.this, "OTP code is invalid! Please enter correct OTP code.", Toast.LENGTH_LONG, true).show();

                                hideLoadingBar();





                            }

                        }

                    }

                });

    }



    private void verifyCode(){


        String temp = otpCodeInput.getText().toString().trim();

        if(temp.equals("")){



            //Toast.makeText(Verification.this, "You must enter the verification code!", Toast.LENGTH_SHORT).show();

            Toasty.error(Job_Seeker_Verify_2.this, "Enter the verification code first!", Toast.LENGTH_LONG, true).show();



        }else {



            showLoadingBarAlert();



            credential = PhoneAuthProvider.getCredential(otpID, temp);

            signInWithPhoneAuthCredential(credential);

        }



    }



    public void resendCode() {



        String temp = userPhoneLocal;



        if(temp.equalsIgnoreCase("")){

        }else {


            //Toasty.success(Job_Seeker_Verify_2.this, "OTP code was sent to your phone number, please check.", Toast.LENGTH_LONG, true).show();

            send_otp_by_firebase(temp);



        }

    }



    //-------------------------

    //-- Registration
    // this method will store the info of user to  database
    private void registerUser(String userName, String userPhone) {





        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fullName", userName);
            parameters.put("phoneNumbe", userPhone);
            parameters.put("userType",0);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ConstantsHolder.rawServer+ConstantsHolder.signupUser, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        parseSignUpData(respo);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Verify_2.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);
    }

    // if the signup successfull then this method will call and it store the user info in local
    public void parseSignUpData(String loginData){
        try {
            JSONObject jsonObject=new JSONObject(loginData);
            String userExist =jsonObject.optString("status");

            hideLoadingBar();

            if(userExist.equalsIgnoreCase("200")){

               // Toasty.error(Job_Seeker_Verify_1.this, "This phone number already exists! Try log in now.", Toast.LENGTH_LONG, true).show();


                //Toasty.success(Job_Seeker_Verify_2.this,"Sign up successful with you provided phone number!",Toast.LENGTH_LONG, true).show();
                Intent openCVwindow = new Intent(Job_Seeker_Verify_2.this,Job_Seeker_CV_Upload.class);
                startActivity(openCVwindow);
                finish();


            }else {

                /*
                //Toasty.success(Job_Seeker_Verify_1.this, "You can open a new account", Toast.LENGTH_LONG, true).show();
                Intent openSecondVerifier = new Intent(Job_Seeker_Verify_1.this,Job_Seeker_Verify_2.class);
                startActivity(openSecondVerifier);
                finish();
                */
                Toasty.error(Job_Seeker_Verify_2.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();

            }

        } catch (JSONException e) {

            hideLoadingBar();
            Toasty.error(Job_Seeker_Verify_2.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
            e.printStackTrace();
        }

    }



    //---------------

    @Override
    public void onBackPressed() {



        Intent openSecondVerifier = new Intent(Job_Seeker_Verify_2.this,Job_Seeker_Verify_1.class);
        startActivity(openSecondVerifier);
        finish();

    }


}
