package com.tur.job1.job_seeker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tur.job1.Intro;
import com.tur.job1.R;
import com.tur.job1.others.Connectivity;
import com.tur.job1.others.FileUploadService;
import com.tur.job1.others.ServiceGenerator;
import com.yalantis.ucrop.util.FileUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Job_Seeker_CV_Upload extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);
    private String TAG = "Job_Seeker_CV_Upload";

    private String userNameLocal;
    private String userPhoneLocal;
    private Button uploadButton;

    private LinearLayout cvChooser;
    private EditText filePath;

    private static final int ACTIVITY_CHOOSE_FILE = 3;
    private String masterFilePath = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_cv_upload);

        cvChooser = (LinearLayout)findViewById(R.id.cv_select_panel);
        filePath = (EditText)findViewById(R.id.file_path);
        uploadButton = (Button)findViewById(R.id.upload_button);

        //--

        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("file/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");


        //------------
        cvChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cvChooser.startAnimation(buttonClick);


                startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);


            }
        });


        userNameLocal = Job_Seeker_Verify_1.userName;
        userPhoneLocal = Job_Seeker_Verify_1.userPhone;

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadButton.startAnimation(buttonClick);

                if(masterFilePath.equalsIgnoreCase("")){

                    Toasty.error(Job_Seeker_CV_Upload.this, "Select a file first!", Toast.LENGTH_LONG, true).show();

                }else {

                    uploadImageWithId(masterFilePath);

                }

                //Toasty.success(Job_Seeker_CV_Upload.this,"Your CV uploaded successfully!",Toast.LENGTH_LONG, true).show();
            }
        });




    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        String path     = "";
        if(requestCode == ACTIVITY_CHOOSE_FILE)
        {
            Uri uri = data.getData();
            String FilePath = getRealPathFromURI(uri);

            if(fileExtentionCheck(FilePath)){
                //filePath.setText(fileNameSeperator(FilePath));
                if(fileSizeFinder(FilePath)){

                    filePath.setText(fileNameSeperator(FilePath));
                    masterFilePath = FilePath;

                }else {

                    filePath.setText(fileNameSeperator(""));
                    Toasty.error(Job_Seeker_CV_Upload.this, "File is too large! Maximum file size limit 5MB", Toast.LENGTH_LONG, true).show();
                }
            }else {

                Toasty.error(Job_Seeker_CV_Upload.this, "File type not supported!\n Supported formats : pdf,docx,jpg,png", Toast.LENGTH_LONG, true).show();
                filePath.setText("");
            }



        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj      = {MediaStore.Images.Media.DATA};
        Cursor cursor       = getContentResolver().query( contentUri, proj, null, null,null);
        if (cursor == null) return null;
        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public boolean fileExtentionCheck(String st){

       Boolean tempFlag = false;

       if(st.endsWith(".pdf") || st.endsWith(".docx") || st.endsWith(".jpg") || st.endsWith(".jpeg") || st.endsWith(".png")
         || st.endsWith(".PDF") || st.endsWith(".DOCX") || st.endsWith(".JPG") || st.endsWith(".JPEG") || st.endsWith(".PNG")){

           tempFlag = true;
       }else{

           tempFlag = false;
       }

       return tempFlag;
    }

    public String fileNameSeperator(String st){

        String tempS = "";


        for(int i=st.length()-1; i >= 0; i--){

            if(st.charAt(i) == '/'){
                break;
            }else {
                tempS = tempS + st.charAt(i);
            }
        }

        if(tempS != null || !tempS.equalsIgnoreCase("")){

            String tempS2 = "";
            for(int i=tempS.length()-1; i>=0; i--){


                tempS2 = tempS2 + tempS.charAt(i);

            }

            tempS = tempS2;
        }



        return tempS;

    }

    public boolean fileSizeFinder(String pth){

            boolean tempFlag = false;
            File file = new File(pth);
            int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
            if(file_size <= 5120){

                tempFlag = true;
            }else {
                tempFlag = false;
            }

            return tempFlag;
    }

    /*
    public void uploadMultipart(final Context context) {
        try {
            String uploadId =
                    new MultipartUploadRequest(context, "http://upload.server.com/path")
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
                            .addFileToUpload("/absolute/path/to/your/file", "your-param-name")
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload();
        } catch (Exception exc) {
            Log.e(TAG, exc.getMessage(), exc);
        }
    }
    */

    //--

    private void uploadImageWithId(String filePath) {
        // create upload service client
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        //File file = FileUtils.getFile(this, fileUri);
        File file = new File(filePath);;//FileUtils.getFile(this, fileUri);
        Uri myUri = Uri.parse(filePath);



        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(myUri)),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        /*
        // add another part within the multipart request
        //String userId = "1";
        RequestBody userId =
                RequestBody.create(
                        MultipartBody.FORM, "1");

        RequestBody fileType =
                RequestBody.create(
                        MultipartBody.FORM, "CV");
                        */
/*
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("userId", 1);
            parameters.put("fileType","CV");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, RequestBody> mapObj = new Gson().fromJson(
                parameters.toString(), new TypeToken<HashMap<String, RequestBody>>() {}.getType()
        );
        */

        // finally, execute the request
        //Call<ResponseBody> call = service.upload(description, body);
        Call<ResponseBody> call = service.uploadImageWithId(body,1,"CV");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("112233", response.toString());
                Toasty.success(Job_Seeker_CV_Upload.this,response.toString(),Toast.LENGTH_LONG, true).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("112233", t.getMessage());
            }
        });
    }


    //---------------------------
}


