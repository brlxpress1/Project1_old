package com.tur.job1.others;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tur.job1.R;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class Dialogue_Helper {







    //-- Showing name input
    public void askingForName(Activity actv, EditText edt) {



        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(actv);
        View promptsView = li.inflate(R.layout.name_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                actv);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText input_name = (EditText) promptsView
                .findViewById(R.id.name);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                               String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                    Toasty.error(actv, "You have to type your name!", Toast.LENGTH_LONG, true).show();
                                    askingForName(actv,edt);


                                }else {

                                 edt.setText(nameTemp);


                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                }else {

                                }
                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    //-------------------

    //-- Showing Email input
    public void askingForEmail(Activity actv, EditText edt) {



        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(actv);
        View promptsView = li.inflate(R.layout.email_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                actv);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText input_name = (EditText) promptsView
                .findViewById(R.id.name);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                    Toasty.error(actv, "You have to type your Email!", Toast.LENGTH_LONG, true).show();
                                    askingForName(actv,edt);


                                }else {

                                    edt.setText(nameTemp);


                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                }else {

                                }
                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    //-------------------

    //-- Showing Skills input
    public void askingForSkills(Activity actv, EditText edt, ArrayList<String> mString) {



    }



    //-------------------
}
