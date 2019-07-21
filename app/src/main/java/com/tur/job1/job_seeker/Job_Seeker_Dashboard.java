package com.tur.job1.job_seeker;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tur.job1.Intro;
import com.tur.job1.R;
import com.tur.job1.others.Connectivity;
import com.tur.job1.others.Dialogue_Helper;
import com.tur.job1.others.ImagePickerActivity;
import com.tur.job1.others.Profile_Image_Changer;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Job_Seeker_Dashboard extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    public String TAG = "Job_Seeker_Dashboard";
    public static final int REQUEST_IMAGE = 100;

    private TextView changeProfile;
    private CircleImageView profileImage;



    private ImageView nameInputOpener;
    public EditText nameBox;

    private ImageView emailInputOpener;
    public EditText emailBox;

    private ImageView datelInputOpener;
    private EditText dateBox;

    DatePicker picker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_dashboard);


        changeProfile = (TextView)findViewById(R.id.change_profile);
        profileImage = (CircleImageView)findViewById(R.id.profile_image);

        nameInputOpener = (ImageView)findViewById(R.id.name_input);
        nameBox = (EditText)findViewById(R.id.namebox);

        emailInputOpener = (ImageView)findViewById(R.id.email_input);
        emailBox = (EditText)findViewById(R.id.emailbox);

        datelInputOpener = (ImageView)findViewById(R.id.date_input);
        dateBox = (EditText)findViewById(R.id.datebox);


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

        emailInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openEmailInput();

            }
        });

        datelInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        //----------------------
        nameBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
                setName();
            }
        });

        emailBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
                setEmail();
            }
        });


        //-------------





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

                    // loading profile image from local cache
                    loadProfile(uri.toString());
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
        dh.askingForName(this,nameBox);
    }

    public void setName(){

        //nameBox.setText(name);
        Toasty.success(Job_Seeker_Dashboard.this, "Successfully displayed the name!", Toast.LENGTH_LONG, true).show();

    }

    public void openEmailInput(){

        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForEmail(this,emailBox);
    }

    public void setEmail(){

        Toasty.success(Job_Seeker_Dashboard.this, "Successfully displayed the Email!", Toast.LENGTH_LONG, true).show();
    }



    //-----------------

    //-- Date picker
    public String getCurrentDate(){
        StringBuilder builder=new StringBuilder();;
        builder.append((picker.getMonth() + 1)+"/");//month is 0 based
        builder.append(picker.getDayOfMonth()+"/");
        builder.append(picker.getYear());
        return builder.toString();
    }



    //--------------------------------------

}
