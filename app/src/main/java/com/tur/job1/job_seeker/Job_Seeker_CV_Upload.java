package com.tur.job1.job_seeker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tur.job1.Intro;
import com.tur.job1.R;
import com.tur.job1.others.Connectivity;

import es.dmoral.toasty.Toasty;

public class Job_Seeker_CV_Upload extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    private String userNameLocal;
    private String userPhoneLocal;
    private Button uploadButton;

    private LinearLayout cvChooser;
    private EditText filePath;

    private static final int ACTIVITY_CHOOSE_FILE = 3;



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

                Toasty.success(Job_Seeker_CV_Upload.this,"Your CV uploaded successfully!",Toast.LENGTH_LONG, true).show();
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
                filePath.setText(fileNameSeperator(FilePath));
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
}


