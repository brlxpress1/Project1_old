package com.tur.job1.others;



import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.tur.job1.R;
import com.tur.job1.adapters.SkillsSetAdapter;
import com.tur.job1.job_seeker.Job_Seeker_Dashboard;
import com.tur.job1.models.Skill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class Skill_Selector extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public String TAG = "Skill_Selector";
    private Dialog dialog;
    String[] options;

    ArrayList<String> selectedSkills = new ArrayList<>();
    private ListView skillView;

    ArrayList<String> skillIdList = new ArrayList<String>();

    ArrayList<String> skillNameList = new ArrayList<String>();
    ArrayList<String> swappingList = new ArrayList<String>();
    ArrayAdapter<String> skillAdapter;

    private Spinner spinner;
    private int incrementor = 0;

    private Button saveExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skill_input);



        spinner = (Spinner) findViewById(R.id.spinner);
        skillView = (ListView)findViewById(R.id.skill_list);
        saveExit = (Button)findViewById(R.id.skill_save_button);
        /*
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Animals, android.R.layout.simple_spinner_item);
        // Specify layout to be used when list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        options = Skill_Selector.this.getResources().getStringArray(R.array.Animals);
        */



        fetch_skill_list();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Toast.makeText(this, " You select >> "+options[position], Toast.LENGTH_SHORT).show();


        if(incrementor >= 1){



            if(skillIdList.get(position).equalsIgnoreCase("0")){

            }else {

                selectedSkills.add(skillNameList.get(position));

                skillView.setAdapter(new SkillsSetAdapter(this,selectedSkills.size(),selectedSkills));

            }

        }
        incrementor++;

        //skillView.notify();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // this method will store the info of user to  database
    private void fetch_skill_list() {

        showLoadingBarAlert();



        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.fetchSkillList, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                      parseFetchData(response);



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Skill_Selector.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    private void parseFetchData(JSONObject jobj){

        skillIdList.clear();
        skillNameList.clear();

        skillIdList.add("0");
        skillNameList.add("Select a skill...");

        if(jobj != null){

            int status =jobj.optInt("status");
            if(status == 200){

                // Getting the Job Seeker info
                //JSONObject skills = jobj.optJSONObject("skills");
                try {
                    JSONArray skills = jobj.getJSONArray("skills");

                    // Log.d(TAG,skills.toString());
                    for(int i=0; i<skills.length(); i++){

                        JSONObject listData = skills.getJSONObject(i);



                        String id =  listData.optString("id");
                        String name =  listData.optString("name");
                        Log.d(TAG,id+"------------"+name);

                        skillIdList.add(id);
                        skillNameList.add(name);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                skillAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, skillNameList);

                skillAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(skillAdapter);
                spinner.setOnItemSelectedListener(this);

                hideLoadingBar();


            }else {
                // Go to Login
            }
        }else {

            // Go to Login
        }




    }

    private void showLoadingBarAlert(){


        dialog = new Dialog(Skill_Selector.this);

        dialog.setContentView(R.layout.loading);

        dialog.setTitle("Please wait!");

        dialog.setCancelable(false);


/*
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.END);
        */



        dialog.show();

    }



    private void hideLoadingBar(){



        dialog.dismiss();

    }

    public void deleteSkillFromList(String skillName){



          for (int i=0; i<selectedSkills.size(); i++){



              if(selectedSkills.get(i).equalsIgnoreCase(skillName)){

                  selectedSkills.remove(i);

              }else {


              }

          }


        //skillView.setAdapter(null);
        skillView.setAdapter(new SkillsSetAdapter(this,selectedSkills.size(),selectedSkills));

    }

}