package com.tur.job1.job_seeker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.tur.job1.Intro;
import com.tur.job1.R;
import com.tur.job1.others.Connectivity;

import es.dmoral.toasty.Toasty;

public class Job_Seeker_Dashboard extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_dashboard);


       
    }
}
