package com.tur.job1.job_seeker;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.suke.widget.SwitchButton;
import com.tur.job1.R;
import com.tur.job1.models.DateResponse;
import com.tur.job1.models.LoginInformationResponse;
import com.tur.job1.models.PhoneNumberCheck;
import com.tur.job1.models.UploadFileResponse;
import com.tur.job1.others.API_Retrofit;
import com.tur.job1.others.ConstantsHolder;
import com.tur.job1.others.Dialogue_Helper;
import com.tur.job1.others.ExtraSkillSetFetcher;
import com.tur.job1.others.FileUploadService;
import com.tur.job1.others.ImagePickerActivity;
import com.tur.job1.others.SaveImage;
import com.tur.job1.others.ServiceGenerator;
import com.tur.job1.others.Skill_Selector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Job_Seeker_Dashboard extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    public String TAG = "Job_Seeker_Dashboard";
    public static final int REQUEST_IMAGE = 100;

    private Dialog dialog;

    private TextView changeProfile;
    private CircleImageView profileImage;

    private SwitchButton visible_switch;



    private ImageView nameInputOpener;
    public EditText nameBox;

    private EditText phoneBox;

    private LinearLayout gender_input;
    private Spinner genderBox;

    private ImageView emailInputOpener;
    public EditText emailBox;

    private ImageView datelInputOpener;
    private EditText dateBox;

    private ImageView skillsInputOpener;
    private EditText skillsBox;

    private ImageView experienceInputOpener;
    private EditText experienceBox;

    private ImageView salaryInputOpener;
    private EditText salaryBox;

    private ImageView currentCompanyInputOpener;
    private EditText currentCompanyBox;

    private ImageView designationInputOpener;
    private EditText designationCompanyBox;

    private ImageView preferredInputOpener;
    private EditText preferredBox;

    List<String> skillIdList = new ArrayList<String>();

    List<String> skillNameList = new ArrayList<String>();

    private int genderSpineerFlag = 0;
    private JSONObject newObject;

    ArrayList<String> tempSkillId = new ArrayList<>();
    ArrayList<String> tempSkillList = new ArrayList<>();

    private String userIdLocal = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_dashboard);




        changeProfile = (TextView)findViewById(R.id.change_profile);
        profileImage = (CircleImageView)findViewById(R.id.profile_image);

        visible_switch = (SwitchButton)findViewById(R.id.visible_swith);

        nameInputOpener = (ImageView)findViewById(R.id.name_input);
        nameBox = (EditText)findViewById(R.id.namebox);

        phoneBox = (EditText)findViewById(R.id.phoneBox);

        gender_input = (LinearLayout)findViewById(R.id.gender_input);
        genderBox = (Spinner)findViewById(R.id.genderbox);

        emailInputOpener = (ImageView)findViewById(R.id.email_input);
        emailBox = (EditText)findViewById(R.id.emailbox);

        datelInputOpener = (ImageView)findViewById(R.id.date_input);
        dateBox = (EditText)findViewById(R.id.datebox);

        skillsInputOpener = (ImageView)findViewById(R.id.skill_input);
        skillsBox = (EditText)findViewById(R.id.skillBox);

        experienceInputOpener = (ImageView)findViewById(R.id.experienceInput);
        experienceBox = (EditText)findViewById(R.id.exprienceBox);

        salaryInputOpener = (ImageView)findViewById(R.id.salaryInput);
        salaryBox = (EditText)findViewById(R.id.salaryBox);

        currentCompanyInputOpener = (ImageView)findViewById(R.id.current_company_input);
        currentCompanyBox = (EditText)findViewById(R.id.currentCompanyBox);

        designationInputOpener = (ImageView)findViewById(R.id.designation_input);
        designationCompanyBox = (EditText)findViewById(R.id.designationBox);

        preferredInputOpener = (ImageView)findViewById(R.id.prepared_location_input);
        preferredBox = (EditText) findViewById(R.id.locationbox);




        ImagePickerActivity.clearCache(this);

        //--
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeProfile.startAnimation(buttonClick);

                //new Profile_Image_Changer(Job_Seeker_Dashboard.this,Job_Seeker_Dashboard.this,profileImage);
                onProfileImageClick();
            }
        });



        nameInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openNameInput();

            }
        });



        gender_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                genderBox.performClick();
            }
        });


        genderSpineerFlag = 0;
        genderBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //check if spinner2 has a selected item and show the value in edittext
                //Toasty.success(Job_Seeker_Dashboard.this, parent.getSelectedItem().toString(), Toast.LENGTH_LONG, true).show();
                if(genderSpineerFlag >= 1){
                    UpdateGender(Integer.parseInt(userIdLocal),1,genderBox.getSelectedItem().toString());
                }else {
                    genderSpineerFlag++;
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });


        emailInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openEmailInput();

            }
        });



        datelInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 showDatePickerDialog();
            }
        });

        skillsInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openSkillInput();

            }
        });

        experienceInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                openExperienceInput();
            }
        });

        salaryInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openSalaryInput();
            }
        });

        currentCompanyInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                openCurrentCompanyInput();

            }
        });

        designationInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                openDesignationInput();
            }
        });

        preferredInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openLocationInput();
            }
        });






        //-------------

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);


        userIdLocal = prefs.getString("userid", "null");
        Log.d(TAG,"Trying to fetch user data with the user ID save in shared preference : "+userIdLocal);

        if(userIdLocal != null && !userIdLocal.equalsIgnoreCase("")){

            //fetch_user_info(Integer.parseInt(userID),0);
            fetch_user_info(Integer.parseInt(userIdLocal),0);

        }else{

            //Go to Log in
        }








    }

    @Override
    protected void onResume() {
        super.onResume();
        //genderSpineerFlag = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //genderSpineerFlag = 0;
    }

    //-- Image picker tasks
    void onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);




                    SaveImage saveImage = new SaveImage();
                    File file = saveImage.saveBitMap(getApplicationContext(),this,bitmap,getCurrentTimeStamp());
                    uploadImageWithId(file, getCurrentTimeStamp());
                    //Log.d("11111",file.getAbsolutePath());






                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.default_avatar)
                .into(profileImage);
        //imgProfile.setColorFilter(ContextCompat.getColor(ct, android.R.color.transparent));


    }

    //------------------

    //--
    public void openNameInput(){


        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForName(this,nameBox,this);
    }

    public void setName(){

        //nameBox.setText(name);
        //Toasty.success(Job_Seeker_Dashboard.this, "Successfully displayed the name!", Toast.LENGTH_LONG, true).show();
        UpdateUserName(Integer.parseInt(userIdLocal),1,nameBox.getText().toString());


    }

    public void openEmailInput(){


        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForEmail(this,emailBox,this);

    }

    public void setEmail(){

        //Toasty.success(Job_Seeker_Dashboard.this, "Successfully displayed the Email!", Toast.LENGTH_LONG, true).show();
        UpdateUserEmail(Integer.parseInt(userIdLocal),1,emailBox.getText().toString());
    }

    public void openSkillInput(){

        Intent openCVwindow = new Intent(Job_Seeker_Dashboard.this, Skill_Selector.class);
        startActivity(openCVwindow);
        finish();
    }

    public void openExperienceInput(){

        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForExperience(this,experienceBox,this);
    }

    public void setExperience(){

        UpdateUserExperience(Integer.parseInt(userIdLocal),1,experienceBox.getText().toString());
    }


    public void openSalaryInput(){

        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForSalary(this,salaryBox,this);
    }

    public void setSalary(){

        UpdateUserSalary(Integer.parseInt(userIdLocal),1,salaryBox.getText().toString());
    }

    public void openCurrentCompanyInput(){

        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForCurrentCompany(this,currentCompanyBox,this);
    }

    public void setCompany(){

        UpdateUserJobHistory(Integer.parseInt(userIdLocal),1,currentCompanyBox.getText().toString(),designationCompanyBox.getText().toString(),1);
    }

    public void openDesignationInput(){

        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForDesignation(this,designationCompanyBox,this);
    }

    public void setDesignatiion(){

        UpdateUserJobHistory(Integer.parseInt(userIdLocal),1,currentCompanyBox.getText().toString(),designationCompanyBox.getText().toString(),2);
    }

    public void openLocationInput(){

        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForPreperedLocation(this,preferredBox,this);
    }

    public void setLocation(){

        UpdateUserLocation(Integer.parseInt(userIdLocal),1,preferredBox.getText().toString());
    }






    //-----------------
    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //String date = "month/day/year: " + month + "/" + dayOfMonth + "/" + year;
        String date2 = dayOfMonth+"/"+month+"/"+year;
        //String date3 = dayOfMonth+"_"+month+"_"+year;


        dateBox.setText(date2);

        //-- update date api
        UpdateUserBirthdate(Integer.parseInt(userIdLocal),1,String.valueOf(dayOfMonth),String.valueOf(month),String.valueOf(year));
        //Log.d(TAG,date2);
    }

    // this method will store the info of user to  database
    private void fetch_user_info(int userID,int userType) {

        showLoadingBarAlert();

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("userId", userID);
            parameters.put("userType", userType);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ConstantsHolder.rawServer+ConstantsHolder.fetchUserData, parameters, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d("1212",respo);

                        //Log.d(TAG,respo);


                        parseFetchData(response);



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();

                    }
                }){

            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                //headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    private void parseFetchData(JSONObject jobj){

        if(jobj != null){

            boolean userExists =jobj.optBoolean("userExist");
            if(userExists){

                // Getting the Job Seeker info
                JSONObject jobSeekerModel = jobj.optJSONObject("jobSeekerModel");



                // 1.Showing the image
                String photoUrl = jobSeekerModel.optString("photoUrl");
                if(photoUrl != null || !photoUrl.equalsIgnoreCase("")){


                    Glide.with(this)
                            .load(photoUrl)
                            .centerCrop()
                            .placeholder(R.drawable.default_avatar)
                            .into(profileImage);

                }
                //-----------

                // 2. Showing the user visibility
                boolean visbile = jobSeekerModel.optBoolean("visbile");
                if(visbile){

                    visible_switch.setChecked(true);

                }else {

                    visible_switch.setChecked(false);

                }
                //-----------

                // 3.Printing the first name
                String fullName = jobSeekerModel.optString("fullName");
                if(fullName != null && !fullName.equalsIgnoreCase("") && !fullName.equalsIgnoreCase("null")){

                    nameBox.setText(fullName);
                }
                //-----------

                // 3_1.Printing the gender type
                String gender = jobSeekerModel.optString("gender");
                if(gender != null && !gender.equalsIgnoreCase("") && !gender.equalsIgnoreCase("null")){

                    if(gender.equalsIgnoreCase("male")){

                        genderBox.setSelection(0);

                    }else if(gender.equalsIgnoreCase("female")){

                        genderBox.setSelection(1);

                    }else if(gender.equalsIgnoreCase("other")){

                        genderBox.setSelection(2);


                    }else{

                        //genderBox.setSelection(0);
                    }
                    //nameBox.setText(fullName);
                }
                //-----------

                // 4.Printing the phone number
                String phone = jobSeekerModel.optString("phone");
                if(phone != null && !phone.equalsIgnoreCase("") && !phone.equalsIgnoreCase("null")){

                    phoneBox.setText("+"+phone);
                }
                //-----------

                // 4.Printing the email address
                String email = jobSeekerModel.optString("email");
                if(email != null && !email.equalsIgnoreCase("")  && !email.equalsIgnoreCase("null")){

                    emailBox.setText(email);
                }
                //-----------

                // 5.Printing the Date of Birth
                String dateOfBirth = jobSeekerModel.optString("dateOfBirth");
                if(dateOfBirth != null && !dateOfBirth.equalsIgnoreCase("")  && !dateOfBirth.equalsIgnoreCase("null")){

                    dateBox.setText(dateOfBirth);
                }
                //-----------

                // 6.Printing skills set
                 // to do
                // Getting the Skill Set
                //JSONObject jobsHistories = jobSeekerModel.optJSONObject("jobsHistories");
                //-----------

                // 7.Printing the Experience
                String experience = jobSeekerModel.optString("experience");
                if(experience != null && !experience.equalsIgnoreCase("") && !experience.equalsIgnoreCase("null")){

                    experienceBox.setText(experience);
                }
                //-----------

                // 7.Printing the Expected salary
                String expectedSalary = jobSeekerModel.optString("expectedSalary");
                if(expectedSalary != null && !expectedSalary.equalsIgnoreCase("") && !expectedSalary.equalsIgnoreCase("null")){

                    salaryBox.setText(expectedSalary );
                }
                //-----------


                // Getting the Job history info
                JSONObject jobsHistories = jobSeekerModel.optJSONObject("jobsHistories");

                if(jobsHistories != null){

                    // 8.Printing the Current company
                    String companyName = jobsHistories.optString("companyName");
                    if(companyName != null && !companyName.equalsIgnoreCase("") && !companyName.equalsIgnoreCase("null")){

                        currentCompanyBox.setText(companyName);
                    }
                    //-----------

                    // 9.Printing the Designation
                    String designation = jobsHistories.optString("designation");
                    if(designation != null && !designation.equalsIgnoreCase("") && !designation.equalsIgnoreCase("null")){

                        designationCompanyBox.setText(designation);
                    }
                    //-----------

                }


                // 10.Printing the Prepared location
                String preferLocation = jobSeekerModel.optString("preferLocation");
                if(preferLocation != null && !preferLocation.equalsIgnoreCase("") && !preferLocation.equalsIgnoreCase("null")){


                    preferredBox.setText(preferLocation);
                }
                //-----------

                //6.Printing skills set
                // Getting the Skill Set
                //JSONObject skillSet = jobSeekerModel.optJSONObject("skillsList");
                //-----------


               makeArrayAdapterFromJsonObj(jobSeekerModel);
                hideLoadingBar();




            }else {
                // Go to Login
            }
        }else {

            // Go to Login
        }




    }

    private void showLoadingBarAlert(){


        dialog = new Dialog(Job_Seeker_Dashboard.this);

        dialog.setContentView(R.layout.custom_profile_dashboard_loading1);

        dialog.setTitle("Please wait!");

        dialog.setCancelable(false);



        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.END);



        dialog.show();

    }



    private void hideLoadingBar(){



        dialog.dismiss();

    }



    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();
    }

    public String getCurrentTimeStamp(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhmmss");
        String format = simpleDateFormat.format(new Date());

        return format;
    }

    //--------------------------------------

    //-- Update api calls


    //--

    // Profile image upload
    private void uploadImageWithId(File file1, String shortFilePath) {

        showLoadingBarAlert();








        if(userIdLocal.equalsIgnoreCase("") || userIdLocal == null){

            //-- Go to sign up screen


        }else {

            // create upload service client
            FileUploadService service =
                    ServiceGenerator.createService(FileUploadService.class);

            // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
            // use the FileUtils to get the actual file by uri
            //File file = FileUtils.getFile(this, fileUri);
            File file = file1;//File file = new File(filePath);;//FileUtils.getFile(this, fileUri);
           // Uri myUri = Uri.parse(filePath);



            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse(userIdLocal+"_"+shortFilePath),
                            file
                    );

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            //--
            Call<UploadFileResponse> call = service.uploadImageWithId(body,Integer.parseInt(userIdLocal),"PNG");

            call.enqueue(new Callback<UploadFileResponse>() {
                @Override
                public void onResponse(Call<UploadFileResponse> call,
                                       retrofit2.Response<UploadFileResponse> response) {
                    Log.v(TAG, response.body().getFileName()+"-------- "+response.body().getFileDownloadUri());
                    //Toasty.success(Job_Seeker_CV_Upload.this,response.body().toString(),Toast.LENGTH_LONG, true).show();
                    Log.d(TAG,response.body().getFileDownloadUri());


                    if(response.body().getStatus() == 200){

                        //--success
                        Toasty.success(Job_Seeker_Dashboard.this,"Profile image updated!",Toast.LENGTH_LONG, true).show();

                        // loading profile image from local cache
                        loadProfile(file1.getAbsolutePath());

                    }else{

                        Toasty.error(Job_Seeker_Dashboard.this,"Can't upload profile image at this time! Try again later.",Toast.LENGTH_LONG, true).show();
                    }


                    hideLoadingBar();
                }

                @Override
                public void onFailure(Call<UploadFileResponse> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    hideLoadingBar();
                }
            });


            //-------------------
        }


    }

    //---------

    // Name update
    private void UpdateUserName(int userID,int userType, String userName) {

        showLoadingBarAlert();

        //
        String namePart = String.valueOf(userID)+"/"+userName;

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.updateUserName+namePart, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Name updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else {


                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update name! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    // Gender update
    private void UpdateGender(int userID,int userType, String genderName) {

       // showLoadingBarAlert();

        //
        String namePart = String.valueOf(userID)+"/"+genderName;

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.updateGender+namePart, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                           // Toasty.success(Job_Seeker_Dashboard.this,"Gender updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else {


                          //  Toasty.error(Job_Seeker_Dashboard.this,"Can't update gender! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                       // hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                       // hideLoadingBar();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    // Email update
    private void UpdateUserEmail(int userID,int userType, String userEmail) {

        showLoadingBarAlert();

        //
        String namePart = String.valueOf(userID)+"/"+userEmail;

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.updateUserEmail+namePart, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Email updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else {


                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update Email! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    // Birthdate update
    private void UpdateUserBirthdate(int userID,int userType, String day, String month, String year) {



        showLoadingBarAlert();
        JSONObject parameters = new JSONObject();
        try {



            String day1 = completeNumber(Integer.parseInt(day));
            String month1 = completeNumber(Integer.parseInt(month));


            parameters.put("userId", userID);
            parameters.put("day", day1);
            parameters.put("month",month1);
            parameters.put("year",year);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        //TURZO
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ConstantsHolder.rawServer+ConstantsHolder.updateUserBirthdate, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        int status = response.optInt("status");

                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Birth-date updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else{

                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update Birth-date! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();

                        }

                        hideLoadingBar();



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                }){

            /** Passing some request headers* */
        @Override
        public Map getHeaders() throws AuthFailureError {
            HashMap headers = new HashMap();
            headers.put("Content-Type", "application/json");
            //headers.put("apiKey", "xxxxxxxxxxxxxxx");
            return headers;
        }


    };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);






    }

    private String completeNumber(int data){
        String temp = "";
        if(data < 10 ){

            temp = "0"+String.valueOf(data);
        }else {

            temp = String.valueOf(data);
        }

        return temp;
    }


    // Experience update
    private void UpdateUserExperience(int userID,int userType, String experience) {

        showLoadingBarAlert();

        //
        String extraPart = String.valueOf(userID)+"/"+experience;

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.updateUserExperience+extraPart, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Experience updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else {


                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update experience! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    // Salary update
    private void UpdateUserSalary(int userID,int userType, String salary) {

        showLoadingBarAlert();

        //
        String extraPart = String.valueOf(userID)+"/"+salary;

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.updateUserSalary+extraPart, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Salary updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else {


                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update Salary! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    // Preferred location update
    private void UpdateUserLocation(int userID,int userType, String location) {

        showLoadingBarAlert();

        //
        String extraPart = String.valueOf(userID)+"/"+location;

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.updateUserLocation+extraPart, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Location updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else {


                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update location! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    // Job History Update
    private void UpdateUserJobHistory(int userID,int userType, String companyName, String currentDesignation, int msgType) {

        showLoadingBarAlert();

        JSONObject parameters = new JSONObject();
        try {

            parameters.put("userId", userIdLocal);
            parameters.put("companyName", companyName);
            parameters.put("designation", currentDesignation);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());
        String extraPart = String.valueOf(userID);

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ConstantsHolder.rawServer+ConstantsHolder.updateUserJobHistory, parameters, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            if(msgType == 1){


                                Toasty.success(Job_Seeker_Dashboard.this,"Company updated successfully!",Toast.LENGTH_LONG, true).show();

                            }

                            if(msgType == 2){


                                Toasty.success(Job_Seeker_Dashboard.this,"Designation name updated successfully!",Toast.LENGTH_LONG, true).show();

                            }


                        }else {


                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update location! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                }){

            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                //headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }


    private void makeArrayAdapterFromJsonObj(JSONObject skillSet){

        if(tempSkillId.size() >0){
            tempSkillId.clear();
        }

        try {


            JSONArray array = skillSet.getJSONArray("skillsList");
            if(array.length() > 0) {


                for(int i=0; i<array.length(); i++){

                    JSONObject object = array.getJSONObject(i);

                    String id = object.getString("skillId");

                    tempSkillId.add(id);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(tempSkillId.size()>0){

            Log.d(TAG,tempSkillId.toString());

            ExtraSkillSetFetcher extraSkillSetFetcher = new ExtraSkillSetFetcher();
            extraSkillSetFetcher.fetch_skill_list(tempSkillId,this,this);


        }else {

            Log.d(TAG,"Skill list is empty!");
            //hideLoadingBar();
        }

        /*
        JSONObject firstModel = skillSet.optJSONObject("skillsList");
        Log.d(TAG,firstModel.toString());
        if(firstModel == null){

            Log.d("123456","Array is null------------------------");
            hideLoadingBar();

        }else {

            Log.d(TAG,"Congratulation!!!!!!!!!!!!!!");
            try {


                JSONArray array = skillSet.getJSONArray("skillsList");
                if(array.length() > 0) {


                    for(int i=0; i<array.length(); i++){

                        JSONObject object = array.getJSONObject(i);

                        String id = object.getString("skillId");

                        tempSkillId.add(id);

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if(tempSkillId.size()>0){

                Log.d(TAG,tempSkillId.toString());

                ExtraSkillSetFetcher extraSkillSetFetcher = new ExtraSkillSetFetcher();
                extraSkillSetFetcher.fetch_skill_list(tempSkillId,this,this);


            }else {

                Log.d(TAG,"Skill list is empty!");
            }

        }
        */



        /*

         try {


            JSONArray array = skillSet.getJSONArray("skillsList");
            if(array.length() > 0 && array != null) {


                for(int i=0; i<array.length(); i++){

                    JSONObject object = array.getJSONObject(i);

                    String id = object.getString("skillId");

                    tempSkillId.add(id);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(tempSkillId.size()>0){

            Log.d(TAG,tempSkillId.toString());

            ExtraSkillSetFetcher extraSkillSetFetcher = new ExtraSkillSetFetcher();
            extraSkillSetFetcher.fetch_skill_list(tempSkillId,this,this);
        }else {

            Log.d(TAG,"Skill list is empty!");
        }
         */


    }


    public void fetchExtraSkillSet(String temp){

        skillsBox.setText(temp);
        hideLoadingBar();

    }







    //--------------------------------------


}
